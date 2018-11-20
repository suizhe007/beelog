#!/usr/bin/env bash
cur=`dirname "$0"`
cur=`cd "$cur"; pwd`
cur=`dirname "$cur"`
#config
app="monitor"
appMainClass=com.zero.tech.monitor.MonitorApplication

export MONITOR_HOME="$cur"
APP_HOME="$MONITOR_HOME"
javaHome=$JAVA_HOME
if [[ -z "$javaHome" ]]; then
    echo "JAVA_HOME not set in environment"
    exit 1
fi


if [[ ! -e "${APP_HOME}/logs" ]]; then
    mkdir ${APP_HOME}/logs
fi

GC_OPTS="-Xloggc:${APP_HOME}/logs/$app-gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGC -XX:+HeapDumpOnOutOfMemoryError"

CLASSPATH=${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar:${APP_HOME}/conf
for f in `ls ${APP_HOME}/lib`
do
        f=${APP_HOME}/lib/$f
        USERPATH=$USERPATH:$f
done
export CLASSPATH=$CLASSPATH$USERPATH

if [[ $# == 1 ]];  then
    MaxMemory=512
    PROFILE="-Dspring.profiles.active=prd"
    JAVA_OPTS="$JAVA_OPTS $PROFILE"
elif [[ $# == 2 ]]; then
    environment=$1
    shift
    if [[ "$environment" = "test" ]];then
        PROFILE="-Dspring.profiles.active=test"
        DEBUG="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=11182"
        JAVA_OPTS="$JAVA_OPTS $DEBUG $PROFILE"
        MaxMemory=256
    else
        echo "usage: $0 [test (default prd environment)] start|stop|status|restart"
        exit 0
    fi
else
    echo "usage: $0 [test (default prd environment)] start|stop|status|restart"
    exit 0
fi

command=$1

run()
{
    cd ${APP_HOME}
    nohup ${JAVA_HOME}/bin/java -Xmx${MaxMemory}M $GC_OPTS $JAVA_OPTS -classpath $CLASSPATH $appMainClass >  ${APP_HOME}/logs/$app-start.log 2>&1 &
    echo $!
}

pid=`ps -ef |grep -v grep| grep $app-gc.log | awk '{print $2}'`

if [[ "$command" = "stop" ]]; then
  if [[ -n $pid ]]; then
    echo "stoping $app....."
    kill $pid
  else
    echo "$app has stopped"
  fi
elif [[ "$command" = "restart" ]]; then
  echo "stoping $app....."
  if [[ -n $pid ]]; then
    kill $pid
  fi
  count=0
  pdc=`ps -ef |grep -v grep| grep $app-gc.log | awk '{print $2}'|wc -l`
  while(( $count<=30 )) && ((pdc>0))
  do
   let 'count++'
   pdc=`ps -ef |grep -v grep| grep $app-gc.log | awk '{print $2}'|wc -l`
   echo $pdc
   sleep 1
  done
  sleep 1
  echo "start $app....."
  run
elif [[ "$command" = "start" ]]; then
    if [[ -n $pid ]]; then
        echo "$app has started"
        exit 0
    fi
    echo "start $app....."
    run
elif [[ "$command" = "status" ]]; then
    if [[ -n $pid ]]; then
        echo "$app is running"
    else
        echo "$app is not running"
    fi
else
  echo "usage: $0 [test (default prd environment)] start|stop|status|restart"
fi
