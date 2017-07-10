## This is rabbitmq streaming to Cassandra with Spark, when messages are in protobuf format

1.  Stream data from Rabbitmq to Cassandra using spark streaming;


The project is submitted on the spark cluster that was set up before and connects to Rabbitmq and cassandra that are set up 
in the reference.conf file : "rabbitmqSparkCassandra/integration/src/main/resources/reference.conf"

#Set up spark cluster.
Use this project to start spark cluster first.
https://github.com/2dmitrypavlov/spark4kube/tree/master/docker

The rabbit server part :
```
amqp {
  # An sequence of known broker addresses (hostname/port pairs)
  # to try in order. A random one will be picked during recovery.
  addresses = [
    { host = "ec2-34-225-142-10.compute-1.amazonaws.com", port = 5672 }
  ]
 ```
 Cassandra cluster server part :
``` 
 db {
  server = "34.230.10.7"
  keyspaceName = "jactravel_monitoring_new"
  username = "user"
  password = "password"
}
```
# To build project you must have jdk 8 and sbt 
*```sbt clean assembly```

Ones you build the project generate your protobuf messages, and do the mapping for them in ProcessMonitoringStream
```def messageBookingHandler(delivery: Delivery): BookRequest = {

    val bookRequestProto = com.jactravel.monitoring.BookRequest.PARSER.parseFrom(delivery.getBody)

    BookRequest(
      bookRequestProto.getQueryUUID
      , bookRequestProto.getSearchQueryUUID
      , bookRequestProto.getPreBookQueryUUID
      , bookRequestProto.getSearchProcessor.getNumber
      , bookRequestProto.getHost
      , bookRequestProto.getStartUtcTimestamp
      , bookRequestProto.getEndUtcTimestamp
      , bookRequestProto.getTradeID
      , bookRequestProto.getBrandID
      , bookRequestProto.getSalesChannelID
      , bookRequestProto.getPropertyID
      , bookRequestProto.getArrivalDate
      , bookRequestProto.getDuration
      , getRoomsInfo(bookRequestProto.getRoomsList)
      , bookRequestProto.getCurrencyID
      , bookRequestProto.getSuccess
      , bookRequestProto.getErrorMessage
      , bookRequestProto.getErrorStackTrace)

  }```

# Run the streaming job you can :
* ```AWS_ACCESS_KEY_ID="" AWS_SECRET_ACCESS_KEY="" nohup spark-submit --packages com.amazonaws:aws-java-sdk-pom:1.10.34,org.apache.hadoop:hadoop-aws:2.6.0 --master spark://52.202.173.248:7077 --executor-memory 10g --driver-memory 10g --num-executors 3 --executor-cores 10 --conf "spark.cores.max=20" --class com.jactravel.monitoring.streaming.ProcessBusiness uber-jactravel-monitoring.jar &```
