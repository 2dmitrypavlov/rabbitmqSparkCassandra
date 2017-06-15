

Start Application on docker

nohup spark/bin/spark-submit --class com.jactravel.monitoring.streaming.ProcessLogging \
--master spark://52.202.173.248:7077 \
--conf spark.cassandra.connection.host="ec2-34-225-142-10.compute-1.amazonaws.com" \
uber-jactravel-monitoring.jar & > logs/jactravel_monitoring.log