// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PlatformType.proto

package com.jactravel.monitoring;

/**
 * Protobuf enum {@code Jactravel.Monitoring.PlatformType}
 */
public enum PlatformType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>UnknownPlatform = 0;</code>
   */
  UnknownPlatform(0, 0),
  /**
   * <code>Grid = 1;</code>
   */
  Grid(1, 1),
  /**
   * <code>IVector = 2;</code>
   */
  IVector(2, 2),
  /**
   * <code>TravelStudio = 3;</code>
   */
  TravelStudio(3, 3),
  /**
   * <code>TravelStudioAdapter = 4;</code>
   */
  TravelStudioAdapter(4, 4),
  /**
   * <code>IVCC = 5;</code>
   */
  IVCC(5, 5),
  /**
   * <code>CMIPrice = 6;</code>
   */
  CMIPrice(6, 6),
  /**
   * <code>IVWeb = 7;</code>
   */
  IVWeb(7, 7),
  /**
   * <code>TSCMQ = 8;</code>
   */
  TSCMQ(8, 8),
  /**
   * <code>IVNative = 9;</code>
   */
  IVNative(9, 9),
  /**
   * <code>IVSM = 10;</code>
   */
  IVSM(10, 10),
  /**
   * <code>IVOTA = 11;</code>
   */
  IVOTA(11, 11),
  ;

  /**
   * <code>UnknownPlatform = 0;</code>
   */
  public static final int UnknownPlatform_VALUE = 0;
  /**
   * <code>Grid = 1;</code>
   */
  public static final int Grid_VALUE = 1;
  /**
   * <code>IVector = 2;</code>
   */
  public static final int IVector_VALUE = 2;
  /**
   * <code>TravelStudio = 3;</code>
   */
  public static final int TravelStudio_VALUE = 3;
  /**
   * <code>TravelStudioAdapter = 4;</code>
   */
  public static final int TravelStudioAdapter_VALUE = 4;
  /**
   * <code>IVCC = 5;</code>
   */
  public static final int IVCC_VALUE = 5;
  /**
   * <code>CMIPrice = 6;</code>
   */
  public static final int CMIPrice_VALUE = 6;
  /**
   * <code>IVWeb = 7;</code>
   */
  public static final int IVWeb_VALUE = 7;
  /**
   * <code>TSCMQ = 8;</code>
   */
  public static final int TSCMQ_VALUE = 8;
  /**
   * <code>IVNative = 9;</code>
   */
  public static final int IVNative_VALUE = 9;
  /**
   * <code>IVSM = 10;</code>
   */
  public static final int IVSM_VALUE = 10;
  /**
   * <code>IVOTA = 11;</code>
   */
  public static final int IVOTA_VALUE = 11;


  public final int getNumber() { return value; }

  public static PlatformType valueOf(int value) {
    switch (value) {
      case 0: return UnknownPlatform;
      case 1: return Grid;
      case 2: return IVector;
      case 3: return TravelStudio;
      case 4: return TravelStudioAdapter;
      case 5: return IVCC;
      case 6: return CMIPrice;
      case 7: return IVWeb;
      case 8: return TSCMQ;
      case 9: return IVNative;
      case 10: return IVSM;
      case 11: return IVOTA;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<PlatformType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static com.google.protobuf.Internal.EnumLiteMap<PlatformType>
      internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<PlatformType>() {
          public PlatformType findValueByNumber(int number) {
            return PlatformType.valueOf(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(index);
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return com.jactravel.monitoring.PlatformTypeProto.getDescriptor().getEnumTypes().get(0);
  }

  private static final PlatformType[] VALUES = values();

  public static PlatformType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    return VALUES[desc.getIndex()];
  }

  private final int index;
  private final int value;

  private PlatformType(int index, int value) {
    this.index = index;
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:Jactravel.Monitoring.PlatformType)
}

