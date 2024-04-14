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

# Run tests
echo "Running tests..."
mvn test
