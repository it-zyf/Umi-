service_name=eureka_2
main_class_name=com.umi.eureka.EurekaServer
jar_name=eureka_server-1.0-SNAPSHOT.jar
jar -xvf $jar_name

rm -rf *.yml
cp  conf/bootstrap.yml  bootstrap.yml

nohup java -server -classpath .:lib/* -XX:+UseG1GC -Xmx300M -XX:MaxGCPauseMillis=150  -verbose:gc -Xloggc:./logs/${service_name}_gc.log -XX:+PrintGCDetails -XX:-OmitStackTraceInFastThrow $main_class_name > ./logs/$service_name.log 2>&1 &

tail -f ./logs/$service_name.log
