
import com.jactravel.monitoring._
import com.rabbitmq.client.ConnectionFactory

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
    channel.queueDeclare("BookRequest", true, false, false, null)
    channel.queueDeclare("SearchRequest", true, false, false, null)
    channel.queueDeclare("PreBookRequest", true, false, false, null)


    // BOOKING
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

    val sendBooking = BookRequest.newBuilder()
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
      .setQueryUUID("101")
      .build()
    .toByteArray

    // PRE-BOOKING
    val sendPreBooking = PreBookRequest.newBuilder()
      .setQueryUUID("102")
      .setSearchQueryUUID("search_uuid")
      .setSearchProcessor(PlatformType.IVector)
      .setHost("localhost")
      .setEndUtcTimestamp("2017-06-06 00:00:00")
      .setStartUtcTimestamp("2017-05-06 00:00:00")
      .setTradeID(1)
      .setBrandID(2)
      .setSalesChannelID(3)
      .setPropertyID(4)
      .setArrivalDate("arrivalDate")
      .setDuration(14)
      .addRooms(bookRoomInfo)
      .setCurrencyID(4)
      .setSuccess("true")
      .setErrorMessage("msg")
      .setErrorStackTrace("error")
      .build()
      .toByteArray

    // SEARCH REQUEST
    val room = RoomRequest.newBuilder()
      .setAdults(3)
      .setChildren(4)

//      .setChildAges(1, 1)

//    val searchRequestInfo = SearchRequestInfo.newBuilder()
//      .setEndUtcTimestamp("2017-06-06 00:00:00")
//      .setStartUtcTimestamp("2017-05-06 00:00:00")
//      .setTradeID(1)
//      .setBrandID(2)
//      .setSalesChannelID(3)
//      .setSearchGeoLevel(GeoLevel.Country)
//      .setGeoLevel1ID(4)
//      .setGeoLevel2ID(8)
////      .setGeoLevel3IDs(0, 1)
////      .setPropertyReferenceIDs(0, 1)
//      .setPropertyIDs(0, 1)
//      .setMinStarRating("low")
//      .setArrivalDate("arrivalDate")
//      .setDuration(14)
//      .setMinStarRating("Zero")
//      .addRooms(room)
//
//    val searchResponseInfo = SearchResponseInfo.newBuilder()
//      .setPropertyReferenceCount(1)
//      .setPropertyCount(1)
//      .setPricedRoomCount(1)
//      .setSuppliersSearched(0, "1")
//      .setSuccess("true")
//      .setErrorMessage("msg")
//      .setErrorStackTrace("msg")
//
//    val sendSearchRequest = SearchRequest.newBuilder()
//      .setQueryUUID("query")
//      .setHost("setHost")
//      .setRequestInfo(searchRequestInfo)
//      .setResponseInfo(searchResponseInfo)
//      .build()
//      .toByteArray
//
//    // SUPPLIER BOOK REQUEST
//    val sendSupplierBokRequest = SupplierBookRequest.newBuilder()
//      .setQueryUUID("query")
//      .setHost("setHost")
//      .setSource("source")
//      .setEndUtcTimestamp("2017-06-06 00:00:00")
//      .setStartUtcTimestamp("2017-05-06 00:00:00")
//      .setTimeout(400)
//      .setPropertyCount(1)
//      .setSuccess("true")
//      .setErrorMessage("msg")
//      .setErrorStackTrace("msg")
//      .setRequestXML("xml")
//      .setResponseXML("xml")
//      .setRequestCount(1)
//      .build()
//      .toByteArray
//
//    // SUPPLIER PRE-BOOK REQUEST
//    val sendSupplierPreBookRequest = SupplierPreBookRequest.newBuilder()
//      .setQueryUUID("query")
//      .setHost("setHost")
//      .setSource("source")
//      .setEndUtcTimestamp("2017-06-06 00:00:00")
//      .setStartUtcTimestamp("2017-05-06 00:00:00")
//      .setTimeout(400)
//      .setPropertyCount(1)
//      .setSuccess("true")
//      .setErrorMessage("msg")
//      .setErrorStackTrace("msg")
//      .setRequestXML("xml")
//      .setResponseXML("xml")
//      .setRequestCount(1)
//      .build()
//      .toByteArray
//
//    // SUPPLIER SEARCH REQUEST
//    val sendSupplierSearchRequest = SupplierSearchRequest.newBuilder()
//      .setQueryUUID("query")
//      .setHost("setHost")
//      .setSource("source")
//      .setEndUtcTimestamp("2017-06-06 00:00:00")
//      .setStartUtcTimestamp("2017-05-06 00:00:00")
//      .setTimeout(400)
//      .setPropertyCount(1)
//      .setSuccess("true")
//      .setErrorMessage("msg")
//      .setErrorStackTrace("msg")
//      .setRequestXML("xml")
//      .setResponseXML("xml")
//      .setRequestCount(1)
//      .build()
//      .toByteArray
//
//    // QUERY PROXY REQUEST
//    val sendQueryProxyRequest = QueryProxyRequest.newBuilder()
//      .setQueryUUID("query")
//      .setClientIP("ip")
//      .setSearchQueryType(QueryType.Book)
//      .setHost("setHost")
//      .setClientRequestUtcTimestamp("2017-05-06 00:00:00")
//      .setClientResponseUtcTimestamp("2017-05-06 00:00:03")
//      .setForwardedRequestUtcTimestamp("2017-05-06 00:00:01")
//      .setForwardedResponseUtcTimestamp("2017-05-06 00:00:02")
//      .setRequestXML("xml")
//      .setResponseXML("xml")
//      .setXmlBookingLogin("xml")
//      .setSuccess("true")
//      .setErrorMessage("msg")
//      .setRequestProcessor(PlatformType.IVector)
//      .setRequestURL("url")
//      .setErrorStackTrace("error")
//      .build()
//      .toByteArray
//
//    // CMI REQUEST
//    val sendCmiRequest = CMIRequest.newBuilder()
//      .setQueryUUID("query")
//      .setSupplierIP("ip")
//      .setCMIQueryType(CMIQueryType.GetAllocation)
//      .setHost("setHost")
//      .setClientRequestUtcTimestamp("2017-05-06 00:00:00")
//      .setClientResponseUtcTimestamp("2017-05-06 00:00:03")
//      .setForwardedRequestUtcTimestamp("2017-05-06 00:00:01")
//      .setForwardedResponseUtcTimestamp("2017-05-06 00:00:02")
//      .setRequestXML("xml")
//      .setResponseXML("xml")
//      .setXmlBookingLogin("xml")
//      .setSuccess("true")
//      .setErrorMessage("msg")
//      .setRequestProcessor(PlatformType.IVector)
//      .setRequestURL("url")
//      .setErrorStackTrace("error")
//
//    // CMI BATCH REQUEST
//    val sendCmiBatchRequest = CMIBatchRequest.newBuilder()
//      .setQueryUUID("query")
//      .setSupplierIP("ip")
//      .setCMIQueryType(CMIQueryType.GetAllocation)
//      .setHost("setHost")
//      .setRequestUtcTimestamp("2017-05-06 00:00:00")
//      .setResponseUtcTimestamp("2017-05-06 00:00:01")
//      .setRequestXML("xml")
//      .setResponseXML("xml")
//      .setSuccess("true")
//      .setErrorMessage("msg")
//      .setErrorStackTrace("error")


    channel.basicPublish("", "BookRequest", null, sendBooking)
    channel.basicPublish("", "PreBookRequest", null, sendPreBooking)


    channel.close()
    connection.close()
  }

}
