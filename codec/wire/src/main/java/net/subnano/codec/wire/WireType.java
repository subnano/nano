package net.subnano.codec.wire;

/**
 * Notes ...
 * - 4 bits required to encode type
 * - Is char required or can short be used?
 * - Length of String will be 1 byte (255 characters)
 * - Length of LongString will be 2 bytes (65,636)
 * - no other types such as arrays, dates or times have been considered
 * - If longer strings are required then JSON should be considered ;-p
 */
public enum WireType {
    Byte,
    Short,
    Int,
    Long,
    Double,
    String,
    Text
}
