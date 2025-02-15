# Use a lightweight Python image
FROM python:3.9-slim

# Set the working directory
WORKDIR /app

# Copy requirements
COPY requirements.txt /app/requirements.txt

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the code
COPY . /app

# Expose port 80
EXPOSE 80

# By default, run the server
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "80"]
