
import com.jactravel.monitoring.{BookRequest, BookRoomInfo, PlatformType}
import com.rabbitmq.client.ConnectionFactory

import scala.collection.JavaConverters._

/**
  * Created by admin on 5/31/17.
  */
object SendProtobuf {
  def main(args: Array[String]): Unit = {
//    val send = Clientsearch.clientsearch.newBuilder()
//      .setSearchQueryUUID("111222")
//      .addAllAdults(List[Integer](25, 35).asJava)
//      .addAllChildAges(List[Integer](5, 15).asJava)
//      .addAllChildren(List[Integer](2, 1).asJava)
//      .addChildren(2)
//      .addAdults(2)
//      .setBrandID(1234)
//      .setArrivalDate("11-11-2011")
//      .setClientIP("192.168.1.1")
//      .build().toByteArray

    val factory = new ConnectionFactory
    factory.setHost("ec2-34-225-142-10.compute-1.amazonaws.com")
    factory.setPort(5672)
    factory.setUsername("guest")
    factory.setPassword("guest")
    val connection = factory.newConnection
    val channel = connection.createChannel

    channel.exchangeDeclare("jactravel.monitoring_direct_exchange_test", "fanout", true); //queueDeclare(QUEUE_NAME, false, false, false, null);
    channel.queueDeclare("jactravel.monitoring_queue_test", true, false, false, null)
    channel.queueBind("jactravel.monitoring_queue_test", "jactravel.monitoring_direct_exchange_test", "jactravel.monitoring_queue_test")
    //jactravel.monitoring

    val bookRoomInfo = BookRoomInfo.newBuilder()
      .addChildAges(10)
      .addChildAges(5)
      .setAdults(2)
      .setBookingToken("bookingToken")
      .setChildren(2)
    .setMealBasisID(3)
    .setPriceDiff("priceDiff")
    .setPropertyRoomTypeID(101)
    .build()

    val send = BookRequest.newBuilder()
      .addRooms(bookRoomInfo)
      .setArrivalDate("arrivalDate")
      .setBrandID(10)
      .setTradeID(11)
      .setCurrencyID(12)
      .setEndUtcTimestamp("2017-06-06 00:00:00")
      .setStartUtcTimestamp("2017-05-06 00:00:00")
      .setHost("test host")
      .setDuration(14)
      .setPreBookQueryUUID("preBookingUUID")
      .setPropertyID(15)
      .setSearchProcessor(PlatformType.IVector)
      .setSalesChannelID(16)
      .build()
    .toByteArray

    channel.basicPublish("jactravel.monitoring_direct_exchange_test", "jactravel.monitoring_queue_test", null, send)

    channel.close()
    connection.close()
  }

}
