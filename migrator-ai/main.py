import os
import subprocess
import tempfile
import shutil
from typing import Optional

import papermill as pm  # Make sure papermill is installed: pip install papermill
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI()


class MigrationRequest(BaseModel):
    source_repo: str   # e.g. "https://github.com/username/source-repo.git"
    target_repo: str   # e.g. "https://github.com/username/target-repo.git"
    source_lang: str   # e.g. "java"
    target_lang: str   # e.g. "python"
    token: Optional[str] = None  # e.g., for GitHub auth


@app.post("/migrate")
def migrate_code(req: MigrationRequest):
    """
    Clone the source repo, run migrator-ai.ipynb with papermill, 
    and push changes to the target repo.
    """
    # 1. Basic input validation
    if not req.source_repo.startswith("http"):
        raise HTTPException(status_code=400, detail="Invalid source_repo URL")

    if not req.target_repo.startswith("http"):
        raise HTTPException(status_code=400, detail="Invalid target_repo URL")

    # 2. Create a temporary directory to clone the source repo
    with tempfile.TemporaryDirectory() as tmp_dir:
        # 3. Clone the source repo locally
        clone_cmd = ["git", "clone", req.source_repo, tmp_dir]
        clone_result = subprocess.run(clone_cmd, capture_output=True, text=True)

        if clone_result.returncode != 0:
            raise HTTPException(
                status_code=500,
                detail=f"Failed to clone source repo: {clone_result.stderr}"
            )

        # 4. Change directory to the cloned repo
        os.chdir(tmp_dir)

        # 5. Run the migrator notebook with papermill, passing in parameters
        #    Note: The notebook itself should handle modifying the code in this repo.
        try:
            pm.execute_notebook(
                input_path="migrator-ai.ipynb",             # Adjust path as needed
                output_path="migrator-ai-out.ipynb",        # Or wherever you want the output
                parameters={
                    "source_repo": req.source_repo,
                    "target_repo": req.target_repo,
                    "source_lang": req.source_lang,
                    "target_lang": req.target_lang,
                }
            )
        except Exception as e:
            raise HTTPException(
                status_code=500,
                detail=f"Error running migrator notebook: {str(e)}"
            )

        # 6. Commit and push all changes to the target repo
        push_result = _push_to_repo(req.target_repo, req.token)
        if push_result["error"]:
            raise HTTPException(status_code=500, detail=push_result["error"])

    return {"detail": "Migration completed successfully."}


def _push_to_repo(target_repo: str, token: Optional[str] = None):
    """
    Helper function: add remote and push changes to the target repo.
    Simplified example. For real usage, handle auth properly.
    """
    try:
        # If you need to inject the token (for private repos), you might do something like:
        # e.g. https://<TOKEN>@github.com/username/target-repo.git
        # But be cautious about printing tokens or storing them in logs.
        if token and "github.com" in target_repo:
            # Only do this for demonstration; real usage might require more robust token handling
            target_repo = target_repo.replace("https://", f"https://{token}@")

        # Add remote called "target"
        subprocess.run(
            ["git", "remote", "add", "target", target_repo],
            check=True,
            capture_output=True,
            text=True,
        )

        # Stage all changes
        subprocess.run(["git", "add", "."], check=True, capture_output=True, text=True)

        # Commit changes
        commit_result = subprocess.run(
            ["git", "commit", "-m", "AI Migration changes"],
            capture_output=True,
            text=True
        )

        # If there's nothing to commit, 'commit' might fail; handle gracefully
        if commit_result.returncode != 0:
            print(f"No new commits: {commit_result.stderr}")

        # Push to the target repo, assuming main branch
        push_cmd = ["git", "push", "target", "main"]
        push_output = subprocess.run(push_cmd, capture_output=True, text=True)

        if push_output.returncode != 0:
            return {"error": f"Push failed: {push_output.stderr}"}

    except subprocess.CalledProcessError as e:
        return {"error": str(e)}

    return {"error": None}
