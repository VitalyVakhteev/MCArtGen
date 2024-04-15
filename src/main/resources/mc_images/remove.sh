#!/bin/bash

# Loop through each file in the current directory
for file in *; do
    # Check if the filename contains the substring "top"
    if [[ "$file" == *"powder"* ]]; then
        echo "Removing file: $file"
        rm "$file"  # Remove the file
    fi
done

echo "Operation completed."
