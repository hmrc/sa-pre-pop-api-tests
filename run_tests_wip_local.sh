#!/usr/bin/env bash

sbt -Denvironment=local 'testOnly runners.RunWip'
