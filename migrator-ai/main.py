import os
import subprocess
import tempfile
import shutil
from typing import Optional

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

# Aider / LLM
from aider.coders import Coder
from aider.models import Model

app = FastAPI()

class MigrationRequest(BaseModel):
    source_repo: str   # e.g. "https://github.com/username/source-repo.git"
    target_repo: str   # e.g. "https://github.com/username/target-repo.git"
    instructions: str  # e.g. "Migrate JBoss code to Spring Boot"
    token: Optional[str] = None  # e.g., for GitHub auth
    use_agent: bool = False      # If True, we'll show an iterative approach


@app.post("/migrate")
def migrate_code(req: MigrationRequest):
    """
    Clone the source repo, run repomix, run `aider` instructions,
    then optionally push changes to the target repo.
    If use_agent = False, it's a single-step approach (like before).
    If use_agent = True, we demonstrate a simplified "agent" approach.
    """
    # 1. Basic input checks
    if not req.source_repo.startswith("http"):
        raise HTTPException(status_code=400, detail="Invalid source_repo URL")

    if not req.target_repo.startswith("http"):
        raise HTTPException(status_code=400, detail="Invalid target_repo URL")

    # 2. Create a temporary directory for the cloned repo
    with tempfile.TemporaryDirectory() as tmp_dir:
        # 3. Clone the source repo
        clone_cmd = ["git", "clone", req.source_repo, tmp_dir]
        clone_result = subprocess.run(clone_cmd, capture_output=True, text=True)

        if clone_result.returncode != 0:
            raise HTTPException(
                status_code=500,
                detail=f"Failed to clone repo: {clone_result.stderr}"
            )

        # 4. (Optional) run repomix to gather data
        os.chdir(tmp_dir)
        repomix_result = subprocess.run(["npx", "repomix"], capture_output=True, text=True)
        repomix_output = repomix_result.stdout
        if repomix_result.returncode != 0:
            # Not necessarily fatal
            print(f"Warning: repomix failed: {repomix_result.stderr}")

        # 5. If use_agent is False, just do a single-run of aider
        if not req.use_agent:
            return _single_step_migration(req, repomix_output)
        else:
            return _agent_migration(req, repomix_output)


def _single_step_migration(req: MigrationRequest, repomix_output: str):
    """
    Single-step approach: Use aider once with the instructions provided.
    """
    model = Model("gpt-4-turbo")
    # Just an example: you might gather filenames or do a more dynamic approach
    fnames = []
    coder = Coder.create(main_model=model, fnames=fnames)

    try:
        coder.run(req.instructions)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Aider error: {str(e)}")

    # (Optional) push
    push_result = _push_to_repo(req.target_repo, req.token)
    if push_result["error"]:
        raise HTTPException(status_code=500, detail=push_result["error"])

    return {
        "detail": "Migration completed successfully (single-step).",
        "repomix_output": repomix_output,
        "aider_output": "Check logs for any further info."
    }


def _agent_migration(req: MigrationRequest, repomix_output: str):
    """
    Illustrative multi-step “agent” approach.
    The agent tries to refine code in multiple steps or until success criteria.
    """
    model = Model("gpt-4-turbo")
    fnames = []

    coder = Coder.create(main_model=model, fnames=fnames)

    # For demonstration, we define a trivial "iteration" loop:
    # 1. Run the instructions
    # 2. (In real usage, check code/test results)
    # 3. If fails, refine instructions
    # We'll do 2 attempts here, just as an example.
    max_iterations = 2
    iteration = 0
    success = False

    while iteration < max_iterations and not success:
        iteration += 1
        try:
            # "Agent" might adapt or refine instructions each iteration,
            # but here we'll keep it simple.
            current_instructions = f"Iteration {iteration}: {req.instructions}"

            print(f"Agent iteration {iteration} with instructions: {current_instructions}")
            coder.run(current_instructions)

            # Here you might run tests or validation. If everything is good, set success=True
            # We'll just assume success after the first iteration for demonstration.
            success = True

        except Exception as e:
            # The agent might refine instructions or do other logic here
            print(f"Agent iteration {iteration} failed: {e}")
            # Minimal example: try again with the same instructions or refine them
            pass

    # (Optional) push if success
    if success:
        push_result = _push_to_repo(req.target_repo, req.token)
        if push_result["error"]:
            raise HTTPException(status_code=500, detail=push_result["error"])

        return {
            "detail": "Agent-based migration completed successfully.",
            "repomix_output": repomix_output,
            "iterations": iteration
        }
    else:
        raise HTTPException(
            status_code=500,
            detail="Agent migration failed after max iterations."
        )


def _push_to_repo(target_repo: str, token: Optional[str] = None):
    """
    Helper function: add remote and push changes to the target repo.
    Simplified example. For real usage, handle auth properly.
    """
    try:
        subprocess.run(
            ["git", "remote", "add", "target", target_repo],
            check=True,
            capture_output=True,
            text=True,
        )
        # For private repos, you might do something like:
        # target_repo = target_repo.replace("https://", f"https://{token}@github.com/")

        subprocess.run(["git", "add", "."], check=True, capture_output=True, text=True)
        commit_result = subprocess.run(
            ["git", "commit", "-m", "AI Migration changes"],
            capture_output=True,
            text=True
        )

        # If there's nothing to commit, 'commit' will fail. You can skip if needed:
        if commit_result.returncode != 0:
            # Not necessarily an error if there's no change to commit
            print(f"No new commits: {commit_result.stderr}")

        push_cmd = ["git", "push", "target", "main"]
        push_output = subprocess.run(push_cmd, capture_output=True, text=True)

        if push_output.returncode != 0:
            return {"error": f"Push failed: {push_output.stderr}"}

    except subprocess.CalledProcessError as e:
        return {"error": str(e)}

    return {"error": None}
