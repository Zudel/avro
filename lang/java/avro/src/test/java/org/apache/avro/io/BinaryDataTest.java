package org.apache.avro.io;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class BinaryDataTest {
  private byte[] bytes ;
  private int start;
  private byte[] bytes2;
  private int start2;

  private Schema schema ;
  private int result;

  public BinaryDataTest(byte[] bytes, int start, byte[] bytes2, int start2, Schema schema) {
    this.bytes = bytes;
    this.start = start;
    this.bytes2 = bytes2;
    this.start2 = start2;
    this.schema = schema;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> getParameters() {
    return Arrays.asList(new Object[][] {
      {"tes".getBytes(),0, "tes".getBytes(),0, Schema.create(Schema.Type.NULL)},
      {"".getBytes(),0, "test".getBytes(),0, Schema.create(Schema.Type.STRING)},
      {"test".getBytes(),0, "test".getBytes(),0, Schema.create(Schema.Type.BYTES)},
      {"test".getBytes(),0, "test".getBytes(),0, Schema.create(Schema.Type.INT)},
      {"test".getBytes(),0, "test".getBytes(),0, Schema.create(Schema.Type.LONG)},
      {"test".getBytes(),0, "test".getBytes(),0, Schema.create(Schema.Type.FLOAT)},
      {"test".getBytes(),0, "test".getBytes(),0, Schema.create(Schema.Type.DOUBLE)},
      {"test".getBytes(),0, "test".getBytes(),0, Schema.create(Schema.Type.BOOLEAN)},



    });
  }

  @Test
  public void compare() {
    try {
      result = BinaryData.compare(bytes, start, bytes2, start2, schema);
    } catch (AvroRuntimeException e) {
      throw new RuntimeException(e);
    }

    //assertEquals(0, result);
  }
}
