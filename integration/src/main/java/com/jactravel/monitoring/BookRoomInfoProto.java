// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: BookRoomInfo.proto

package com.jactravel.monitoring;

public final class BookRoomInfoProto {
  private BookRoomInfoProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_Jactravel_Monitoring_BookRoomInfo_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Jactravel_Monitoring_BookRoomInfo_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022BookRoomInfo.proto\022\024Jactravel.Monitori" +
      "ng\"\311\001\n\014BookRoomInfo\022\016\n\006adults\030\001 \001(\005\022\020\n\010c" +
      "hildren\030\002 \001(\005\022\021\n\tchildAges\030\003 \003(\005\022\023\n\013meal" +
      "BasisID\030\004 \001(\005\022\024\n\014bookingToken\030\005 \001(\t\022\032\n\022p" +
      "ropertyRoomTypeID\030\006 \001(\005\022\021\n\tpriceDiff\030\007 \001" +
      "(\t\022\021\n\troomCount\030\010 \001(\005\022\027\n\017preBookingToken" +
      "\030\t \001(\tB/\n\030com.jactravel.monitoringB\021Book" +
      "RoomInfoProtoP\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_Jactravel_Monitoring_BookRoomInfo_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_Jactravel_Monitoring_BookRoomInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Jactravel_Monitoring_BookRoomInfo_descriptor,
              new java.lang.String[] { "Adults", "Children", "ChildAges", "MealBasisID", "BookingToken", "PropertyRoomTypeID", "PriceDiff", "RoomCount", "PreBookingToken", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
