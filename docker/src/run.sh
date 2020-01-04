#!/bin/bash
ifconfig eth0 promisc
java -jar /opt/capanda-0.0.1-SNAPSHOT.jar \
  --logging.file=/opt/logs/capanda.log