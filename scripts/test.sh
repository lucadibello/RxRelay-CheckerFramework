#!/bin/sh

JAVA_HOME=$(/usr/libexec/java_home -v 1.8) mvn clean test
