// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SupplierSearchRequest.proto

package com.jactravel.monitoring;

public final class SupplierSearchRequestProto {
  private SupplierSearchRequestProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_Jactravel_Monitoring_SupplierSearchRequest_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Jactravel_Monitoring_SupplierSearchRequest_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\033SupplierSearchRequest.proto\022\024Jactravel" +
      ".Monitoring\"\243\002\n\025SupplierSearchRequest\022\021\n" +
      "\tqueryUUID\030\001 \001(\t\022\014\n\004host\030\002 \001(\t\022\016\n\006source" +
      "\030\003 \001(\t\022\031\n\021startUtcTimestamp\030\004 \001(\t\022\027\n\017end" +
      "UtcTimestamp\030\005 \001(\t\022\017\n\007timeout\030\006 \001(\005\022\025\n\rp" +
      "ropertyCount\030\007 \001(\005\022\017\n\007success\030\010 \001(\t\022\024\n\014e" +
      "rrorMessage\030\t \001(\t\022\027\n\017errorStackTrace\030\n \001" +
      "(\t\022\022\n\nrequestXML\030\013 \001(\t\022\023\n\013responseXML\030\014 " +
      "\001(\t\022\024\n\014requestCount\030\r \001(\005B8\n\030com.jactrav" +
      "el.monitoringB\032SupplierSearchRequestProt",
      "oP\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_Jactravel_Monitoring_SupplierSearchRequest_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_Jactravel_Monitoring_SupplierSearchRequest_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Jactravel_Monitoring_SupplierSearchRequest_descriptor,
              new java.lang.String[] { "QueryUUID", "Host", "Source", "StartUtcTimestamp", "EndUtcTimestamp", "Timeout", "PropertyCount", "Success", "ErrorMessage", "ErrorStackTrace", "RequestXML", "ResponseXML", "RequestCount", });
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
