base_dir=$(dirname "$PWD")
RUN_JAR_DIR=${base_dir}/target/
JAR_NAME=Coins-1.0-SNAPSHOT.jar
APP_CLASS=com.wanglin.CoinsApplication
CLASS_LIB="${RUN_JAR_DIR}/${JAR_NAME}"

num=`$JAVA_HOME/bin/jps -ml|grep "${APP_CLASS}"|grep server|wc -l`
case $1 in
    start)
        if [ $num -lt 1 ]
        then
            echo "server start!"
            nohup java -jar $CLASS_LIB server ../config.yml > ./server.log 2>&1 &
        else
            echo "server already start!"
        fi;;
    stop)
        if [ $num -lt 1 ]
        then
            echo "server not start!"
        else
            pid=`netstat -nlpt|grep 8080|awk '{print $7}'|awk -F '/' '{print $1}'`
            echo "if you want stop, need to kill this ${pid} first,killing #{pid}"
            kill -9 ${pid}
            echo "server stop!"
        fi;;
    restart)
        if [ $num -lt 1 ]
        then
            echo "no server start!"
        else
            echo "server stop!"
        fi
        pid=`netstat -nlpt|grep 8080|awk '{print $7}'|awk -F '/' '{print $1}'`
        echo "if you want stop, need to kill this ${pid} first,killing #{pid}"
        kill -9 ${pid}
        echo "server start!"
        nohup java -jar $CLASS_LIB server config.yml > ./server.log 2>&1 &;;
    status)
        if [ $num -lt 1 ]
        then
            echo "server not start!"
        else
            echo "server already start!"
        fi;;
    *)
        echo "need argument start|stop|restart|status";;
esac
