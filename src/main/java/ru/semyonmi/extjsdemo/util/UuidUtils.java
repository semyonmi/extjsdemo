package ru.semyonmi.extjsdemo.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.UUID;

public class UuidUtils {

  /**
   * Try to parse {@link String} and return {@link java.util.UUID} otherwise {@code null}.
   *
   * @param string value to parse
   * @return uuid
   */
  public static UUID tryParse(String string) {
    try {
      return UUID.fromString(string);
    } catch (IllegalArgumentException ignore) {
      return null;
    }
  }

  private static Random RANDOM_INSTANCE = new Random();

  public static UUID notSecureRandomUuid() {
    return new UUID(RANDOM_INSTANCE.nextLong(), RANDOM_INSTANCE.nextLong());
  }

  public static String toHex(UUID value) {
    return value.toString().replaceAll("-", "");
  }

  /**
   * Convert {@link java.util.UUID} to byte array. Useful for when {@link java.util.UUID}s are stored in Oracle tables as RAW(16).
   *
   * @param uuid uuid to convert
   * @return byte array
   */
  public static byte[] getBytesFromUuid(UUID uuid) {
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());

    return bb.array();
  }

  /**
   * Restore {@link java.util.UUID} from byte array. Useful for when {@link java.util.UUID}s are stored in Oracle tables as RAW(16).
   *
   * @param bytes byte array
   * @return uuid
   */
  public static UUID getUuidFromBytes(byte[] bytes) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    Long high = byteBuffer.getLong();
    Long low = byteBuffer.getLong();

    return new UUID(high, low);
  }

  /**
   * Escape uuid with double quotes.
   *
   * @param uuid uuid to escape
   * @return escaped string with uuid
   */
  public static String escapeUuid(UUID uuid) {
    return uuid == null
      ? null
      : "\"" + uuid + "\"";
  }

  /**
   * Encodes the given uuid value with base36.
   * The uuid consists of 32 symbols, each withing the range of [0, f] (16 symbols).
   * Therefore it may represent 16**32 different strings.
   * The result string will contain exactly 25 symbols (log36(16**32) ~~ 24.76 < 25)
   * The result string may contain leading zeroes.
   *
   * @param uuid uuid to encode.
   * @return Encoded value, if the uuid is not null, null otherwise.
   */
  public static String encodeToBase36(UUID uuid) {
    if (uuid == null) {
      return null;
    }

    String uuidString = uuid.toString().replace("-", "");
    BigInteger uuidAsNumber = new BigInteger(uuidString, 16);
    String encodedValue = uuidAsNumber.toString(36);

    return org.apache.commons.lang3.StringUtils.leftPad(encodedValue, 25, '0');
  }
}
