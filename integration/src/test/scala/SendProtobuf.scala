
import com.jactravel.monitoring._
import com.rabbitmq.client.ConnectionFactory

/**
  * Created by admin on 5/31/17.
  */
object SendProtobuf extends Constants {
  def main(args: Array[String]): Unit = {

    val factory = new ConnectionFactory
    factory.setHost("ec2-34-225-142-10.compute-1.amazonaws.com")
    factory.setPort(5672)
    factory.setUsername("guest")
    factory.setPassword("guest")

    val connection = factory.newConnection
    val channel = connection.createChannel

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
      .setQueryUUID(s"${System.currentTimeMillis()}")
      .setSearchQueryUUID("search_query_uuid_11")
      .setPreBookQueryUUID("preBookingUUID")
      .setSearchProcessor(PlatformType.IVector)
      .setHost("test host")
      .setEndUtcTimestamp("2017-06-06 00:00:00")
      .setStartUtcTimestamp("2017-05-06 00:00:00")
      .setTradeID(11)
      .addRooms(bookRoomInfo)
      .setArrivalDate("arrivalDate")
      .setBrandID(10)
      .setCurrencyID(12)
      .setDuration(14)
      .setPropertyID(15)
      .setSearchProcessor(PlatformType.IVector)
      .setSalesChannelID(16)
      .build()
    .toByteArray

    // PRE-BOOKING
    val sendPreBooking = PreBookRequest.newBuilder()
      .setQueryUUID(s"${System.currentTimeMillis()}")
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
      .addChildAges(5)
      .addChildAges(3)
    val searchRequestInfo = SearchRequestInfo.newBuilder()
      .setEndUtcTimestamp("2017-06-06 00:00:00")
      .setStartUtcTimestamp("2017-05-06 00:00:00")
      .setTradeID(1)
      .setBrandID(2)
      .setSalesChannelID(3)
      .setSearchGeoLevel(GeoLevel.Country)
      .setGeoLevel1ID(4)
      .setGeoLevel2ID(8)
      .setMinStarRating("low")
      .setArrivalDate("arrivalDate")
      .setDuration(14)
      .setMinStarRating("Zero")
      .addRooms(room)
      .addGeoLevel3IDs(4)
      .addPropertyReferenceIDs(5)
      .addPropertyIDs(5)

    val searchResponseInfo = SearchResponseInfo.newBuilder()
      .setPropertyReferenceCount(1)
      .setPropertyCount(1)
      .setPricedRoomCount(1)
      .setSuccess("true")
      .setErrorMessage("msg")
      .setErrorStackTrace("msg")
      .addSuppliersSearched("value")

    val sendSearchRequest = SearchRequest.newBuilder()
      .setQueryUUID(s"${System.currentTimeMillis()}")
      .setHost("setHost")
      .setRequestInfo(searchRequestInfo)
      .setResponseInfo(searchResponseInfo)
      .build()
      .toByteArray

    // SUPPLIER BOOK REQUEST
    val sendSupplierBookRequest = SupplierBookRequest.newBuilder()
      .setQueryUUID(s"${System.currentTimeMillis()}")
      .setHost("setHost")
      .setSource("source")
      .setEndUtcTimestamp("2017-06-06 00:00:00")
      .setStartUtcTimestamp("2017-05-06 00:00:00")
      .setTimeout(400)
      .setPropertyCount(1)
      .setSuccess("true")
      .setErrorMessage("msg")
      .setErrorStackTrace("msg")
      .setRequestXML("xml")
      .setResponseXML("xml")
      .setRequestCount(1)
      .build()
      .toByteArray

    // SUPPLIER PRE-BOOK REQUEST
    val sendSupplierPreBookRequest = SupplierPreBookRequest.newBuilder()
      .setQueryUUID(s"${System.currentTimeMillis()}")
      .setHost("setHost")
      .setSource("source")
      .setEndUtcTimestamp("2017-06-06 00:00:00")
      .setStartUtcTimestamp("2017-05-06 00:00:00")
      .setTimeout(400)
      .setPropertyCount(1)
      .setSuccess("true")
      .setErrorMessage("msg")
      .setErrorStackTrace("msg")
      .setRequestXML("xml")
      .setResponseXML("xml")
      .setRequestCount(1)
      .build()
      .toByteArray

    // SUPPLIER SEARCH REQUEST
    val sendSupplierSearchRequest = SupplierSearchRequest.newBuilder()
      .setQueryUUID(s"${System.currentTimeMillis()}")
      .setHost("setHost")
      .setSource("source")
      .setEndUtcTimestamp("2017-06-06 00:00:00")
      .setStartUtcTimestamp("2017-05-06 00:00:00")
      .setTimeout(400)
      .setPropertyCount(1)
      .setSuccess("true")
      .setErrorMessage("msg")
      .setErrorStackTrace("msg")
      .setRequestXML("xml")
      .setResponseXML("xml")
      .setRequestCount(1)
      .build()
      .toByteArray

    // QUERY PROXY REQUEST
    val sendQueryProxyRequest = QueryProxyRequest.newBuilder()
      .setQueryUUID(s"${System.currentTimeMillis()}")
      .setClientIP("ip")
      .setSearchQueryType(QueryType.Book)
      .setHost("setHost")
      .setClientRequestUtcTimestamp("2017-05-06 00:00:00")
      .setClientResponseUtcTimestamp("2017-05-06 00:00:03")
      .setForwardedRequestUtcTimestamp("2017-05-06 00:00:01")
      .setForwardedResponseUtcTimestamp("2017-05-06 00:00:02")
      .setRequestXML("xml")
      .setResponseXML("xml")
      .setXmlBookingLogin("xml")
      .setSuccess("true")
      .setErrorMessage("msg")
      .setRequestProcessor(PlatformType.IVector)
      .setRequestURL("url")
      .setErrorStackTrace("error")
      .build()
      .toByteArray

    // CMI REQUEST
    val sendCmiRequest = CMIRequest.newBuilder()
      .setQueryUUID(s"${System.currentTimeMillis()}")
      .setSupplierIP("ip")
      .setCMIQueryType(CMIQueryType.GetAllocation)
      .setHost("setHost")
      .setClientRequestUtcTimestamp("2017-05-06 00:00:00")
      .setClientResponseUtcTimestamp("2017-05-06 00:00:03")
      .setForwardedRequestUtcTimestamp("2017-05-06 00:00:01")
      .setForwardedResponseUtcTimestamp("2017-05-06 00:00:02")
      .setRequestXML("xml")
      .setResponseXML("xml")
      .setSuccess("true")
      .setErrorMessage("msg")
      .setRequestProcessor(PlatformType.IVector)
      .setRequestURL("url")
      .setErrorStackTrace("error")
      .build()
      .toByteArray

    // CMI BATCH REQUEST
    val sendCmiBatchRequest = CMIBatchRequest.newBuilder()
      .setQueryUUID(s"${System.currentTimeMillis()}")
      .setSupplierIP("ip")
      .setCMIQueryType(CMIQueryType.GetAllocation)
      .setHost("setHost")
      .setRequestUtcTimestamp("2017-05-06 00:00:00")
      .setResponseUtcTimestamp("2017-05-06 00:00:01")
      .setRequestXML("xml")
      .setResponseXML("xml")
      .setSuccess("true")
      .setErrorMessage("msg")
      .setErrorStackTrace("error")
      .build()
      .toByteArray


    channel.basicPublish(exchanger, searchRequestQueue, null, sendSearchRequest)
    channel.basicPublish(exchanger, supplierBookRequestQueue, null, sendSupplierBookRequest)
    channel.basicPublish(exchanger, supplierPreBookRequestQueue, null, sendSupplierPreBookRequest)
    channel.basicPublish(exchanger, supplierSearchRequestQueue, null, sendSupplierSearchRequest)
    channel.basicPublish(exchanger, queryProxyRequestQueue, null, sendQueryProxyRequest)
    channel.basicPublish(exchanger, cmiRequestQueue, null, sendCmiRequest)
    channel.basicPublish(exchanger, cmiBatchRequestQueue, null, sendCmiBatchRequest)


    channel.close()
    connection.close()
  }

}
