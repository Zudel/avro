/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.avro.io;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.apache.avro.io.BinaryDecoderIntegrationTest.MAX_ARRAY_SIZE;
import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class BinaryDataTest {
  private static byte[] byteArray2 = { 30 };
  private int lenArray;
  private static byte[] bytes;
  // private static byte[] bytesMax = new byte[MAX_ARRAY_SIZE];
  private static byte[] bytesEmpty = "".getBytes();
  private int start;
  private byte[] bytes2;
  private int start2;
  private Schema schema;
  private int result;
  private static byte[] byteArray = { 40 };
  private static byte[] byteArray3 = { 40, 60 };
  private static byte[] doubleArrayBytes = new byte[2 * Double.BYTES];

  public BinaryDataTest(byte[] bytes, int start, byte[] bytes2, int start2, int lenArray, Schema schema) {
    this.bytes = bytes;
    this.start = start;
    this.bytes2 = bytes2;
    this.start2 = start2;
    this.lenArray = lenArray;
    this.schema = schema;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> getParameters() {
    return Arrays
        .asList(new Object[][] { { bytesEmpty, 0, bytesEmpty, 0, bytesEmpty.length, Schema.create(Schema.Type.INT) },

            { bytesEmpty, -1, bytesEmpty, 0, -1, Schema.create(Schema.Type.INT) },
            { byteArray, 0, byteArray2, 0, byteArray.length, Schema.create(Schema.Type.INT) },
            { byteArray2, 0, byteArray, 0, -1, Schema.create(Schema.Type.INT) },
            { byteArray, 0, byteArray, -1, 0, Schema.create(Schema.Type.INT) },
            { byteArray, 0, byteArray, 0, MAX_ARRAY_SIZE + 10, Schema.create(Schema.Type.INT) },
            { byteArray, 0, byteArray2, 0, byteArray.length, Schema.createMap(Schema.create(Schema.Type.INT)) },

            // miglioramento
            { byteArray3, 0, byteArray2, 0, byteArray3.length, Schema.create(Schema.Type.INT) },
            { byteArray, 0, byteArray, 0, byteArray.length, Schema.create(Schema.Type.LONG) },
            { byteArray, 0, byteArray2, 0, byteArray.length, Schema.create(Schema.Type.NULL) },
            { doubleArrayBytes, 0, doubleArrayBytes, 0, doubleArrayBytes.length, Schema.create(Schema.Type.DOUBLE) },
            { doubleArrayBytes, 0, doubleArrayBytes, 0, doubleArrayBytes.length, Schema.create(Schema.Type.FLOAT) },
            { byteArray, 0, byteArray2, 0, byteArray.length, Schema.createRecord("test", null, null, false) },
            { byteArray, 0, byteArray2, 0, byteArray.length, Schema.createArray(Schema.create(Schema.Type.INT)) },
            { byteArray, 0, byteArray2, 0, byteArray.length, Schema.createUnion(Schema.create(Schema.Type.INT)) }, // overflow
            { byteArray, 0, byteArray2, 0, byteArray.length, Schema.create(Schema.Type.BYTES) },
            { byteArray, 0, byteArray2, 0, byteArray.length, Schema.create(Schema.Type.BOOLEAN) } });
  }
  /**
   * Compare binary encoded data. If equal, return zero. If greater-than, return
   * 1, if less than return -1. If the data is not comparable, throw an exception.
   */
  @Test
  public void compare() {
    try {
      result = BinaryData.compare(bytes, start, bytes2, start2, schema);
    } catch (AvroRuntimeException e) {
      Assert.assertTrue(e.getMessage().contains("java.io.EOFException") || e.getMessage().contains("maps")
          || e.getMessage().contains("Schema fields not set yet")
          || e.getMessage().contains("Unexpected schema to compare!"));
    } catch (IndexOutOfBoundsException e) {
      Assert.assertTrue(start < 0 || start2 < 0 || start2 > bytes2.length || start > bytes.length
          || e.getMessage().contains("Array index out of range"));
    }
    switch (result) {
    case 0:
      assertEquals(0, result);
      break;
    case 1:
      assertEquals(1, result);
      break;
    case -1:
      assertEquals(-1, result);
      break;
    default:
      fail("result is not 0,1,-1");
      break;
    }
  }

  @Test
  public void hashCodeTest() {
    try {
      result = BinaryData.hashCode(bytes, start, lenArray, schema);
    } catch (IndexOutOfBoundsException e) {
      Assert.assertNotNull(e);
    } catch (AvroRuntimeException e) {
      Assert.assertTrue(e.getMessage().contains("maps") || e.getMessage().contains("java.io.EOFException")
          || e.getMessage().contains("Schema fields not set yet"));
    }
    Assert.assertTrue(result >= 0);
  }
}
