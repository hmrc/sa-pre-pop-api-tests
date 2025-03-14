#!/usr/bin/env bash

./run_format_and_deps.sh

sbt -Denvironment=local 'testOnly runners.RunLocal'
