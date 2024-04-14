#!/bin/sh
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
echo "JAVA_HOME is set to $JAVA_HOME"

# Ensure that the Java version is 1.8
if [ -z "$JAVA_HOME" ]; then
  echo "Java 1.8 is not installed. Please install Java 1.8 and try again."
  exit 1
fi

# Install dependencies
echo "Installing dependencies..."
mvn clean install -DskipTests

# Check if failed
if [ $? -ne 0 ]; then
  echo "Failed to install dependencies."
  exit 1
fi

# Use lastest version of checkerframework
mvn versions:use-latest-versions -Dincludes="org.checkerframework:*"

# Run tests
echo "Running tests..."
mvn test

# Check if failed
if [ $? -ne 0 ]; then
  echo "Failed to run tests."
  exit 1
fi
