import com.jactravel.monitoring.client.search.Clientsearch
import com.rabbitmq.client.ConnectionFactory

/**
  * Created by admin on 5/31/17.
  */
object SendProtobuf {
  def main(args: Array[String]): Unit = {
    val send = Clientsearch.clientsearch.newBuilder().setSearchQueryUUID("11232432354355").addAdults(1).build().toByteArray

    val factory = new ConnectionFactory
    factory.setHost("ec2-34-225-142-10.compute-1.amazonaws.com")
    factory.setPort(5672)
    factory.setUsername("guest")
    factory.setPassword("guest")
    val connection = factory.newConnection
    val channel = connection.createChannel

    channel.exchangeDeclare("jactravel.monitoring_direct_exchange", "fanout", true); //queueDeclare(QUEUE_NAME, false, false, false, null);
    channel.queueDeclare("jactravel.monitoring_queue", true, false, false, null)
    channel.queueBind("jactravel.monitoring_queue", "jactravel.monitoring_direct_exchange", "jactravel.monitoring_queue")
    //jactravel.monitoring

    channel.basicPublish("jactravel.monitoring_direct_exchange", "jactravel.monitoring_queue", null, send)

    channel.close()
    connection.close()
  }

}
