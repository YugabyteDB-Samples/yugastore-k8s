#!/bin/bash
kill -9 $(ps -ef | grep java | grep SNAPSHOT.jar | awk '{print $2}')