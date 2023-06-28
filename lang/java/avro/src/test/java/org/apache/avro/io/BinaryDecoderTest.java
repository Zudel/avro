package org.apache.avro.io;

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
import org.apache.avro.AvroRuntimeException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;
import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class BinaryDecoderTest {
  byte[] bytes;
  int start;
  int len;
  private BinaryDecoder decoder;

  public BinaryDecoderTest(byte[] bytes, int start, int len) {
    this.bytes = bytes;
    this.start = start;
    this.len = len;
  }

  @Before
  public void setUp() {
    try {
      decoder = DecoderFactory.get().binaryDecoder(bytes, null);
    } catch (NullPointerException e) {
      Assert.assertNull(bytes);
    }
  }

  @Parameterized.Parameters
  public static Collection<Object[]> getParameters() {
    return Arrays.asList(new Object[][] {
        // byte[], int, int
        { "test".getBytes(), 1, 1 },
        { "test".getBytes(), 1, 0 },
        {"test".getBytes(),0,1},
        {"test".getBytes(), -1, 0}, //ArrayIndexOutOfBoundsException
        {"test".getBytes(),-1,-1}, //ArrayIndexOutOfBoundsException
        { "".getBytes(), 0, 0 },
        { "".getBytes(), 1, 0 }, //ArrayIndexOutOfBoundsException
        {"".getBytes(),0,1}, //java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        {"".getBytes(), -1, 0}, //java.lang.ArrayIndexOutOfBoundsException
        {"".getBytes(),-1,-1}, //java.lang.ArrayIndexOutOfBoundsException
        { null, 0, 0 }, //java.lang.NullPointerException: Cannot read the array length because "original" is null
        { null, 1, 0 }, //stessa eccezione per tutti i null
        {null,0,1},
        {null, -1, 0},
        {null,-1,-1},

    });
  }
 /**
  * protected void doReadBytes(byte[] bytes, int start, int length)
  * */
  @Test
  public void doReadBytes() {
    try {
      decoder.doReadBytes(bytes, start, len);
    }catch (AvroRuntimeException e){
      Assert.assertTrue(len < 0);
    }
    catch (NullPointerException e) {
      Assert.assertNull(bytes);

    }catch (IOException e) {
      assertNotNull(e);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      Assert.assertTrue(start < 0 || start > bytes.length);
    }
  }
}
