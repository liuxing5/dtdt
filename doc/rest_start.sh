#!/bin/sh
cur_dir=$(pwd)
cpu_core=`cat /proc/cpuinfo| grep "processor"| wc -l`
JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xmn512m -XX:ParallelGCThreads=${cpu_core} -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8 -XX:+DisableExplicitGC"
case "$1" in
  start)
 nohup java -Dpath=${cur_dir} -Dserver.port=8088 -Dlogging.config=/root/dtdt/rest/log4j2.xml ${JAVA_OPTS} 	
 -jar dtdt-rest-0.0.1-SNAPSHOT.jar > nohup.log 2>&1 &
    echo $! > service.pid ;;

  stop)
    kill `cat service.pid`
    rm -rf service.pid
    ;;

  restart)
    sh $0 stop
    sleep 1
    sh $0 start
    ;;
  status)
    pidFile="service.pid"
    if [ -f $pidFile ];
    then
    ps -ef|grep `cat service.pid`
    else
    echo "pid not exists"
    fi
    ;;
  remove)
    kill `cat service.pid`
    rm -rf service.pid
    rm -rf log
    rm -rf nohup.log
    ;;
  *)
