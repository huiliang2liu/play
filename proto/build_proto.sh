#!/bin/bash
protoc -I=proto --java_out=src/main/java `find proto -type f -name '*.proto'`