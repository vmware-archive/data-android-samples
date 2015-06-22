#!/bin/bash
set -e
set -x

./gradlew --refresh-dependencies clean assemble

