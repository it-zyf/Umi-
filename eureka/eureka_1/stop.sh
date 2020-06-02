
for id in `ps axu|grep eureka_1 | grep -v 'grep' | awk '{print $2}'`
do
  kill -9 $id
done
