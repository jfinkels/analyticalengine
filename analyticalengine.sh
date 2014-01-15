#!/bin/sh

# Change this to the path where your local Maven repository lives.
#M2_REPO=$HOME/.m2/repository

# Change this to the path where your log4j JAR lives.
#LOG4JPATH=$M2_REPO/log4j/log4j/1.2.17/log4j-1.2.17.jar

java -classpath target/classes:$LOG4JPATH analyticalengine.main.aes "$@"
