#!/bin/bash

echo "================================================"
echo "  SoupModMaker2 - Minecraft Mod Generator"
echo "================================================"
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo
    echo "Please install Java 17 or higher from:"
    echo "https://adoptium.net/"
    echo
    exit 1
fi

echo "Starting SoupModMaker2..."
echo

# Run the JAR with any command-line arguments passed to this script
java -jar soupmodmaker2-all-0.1.0-SNAPSHOT.jar "$@"

echo
echo "================================================"
echo "  SoupModMaker2 finished!"
echo "================================================"
