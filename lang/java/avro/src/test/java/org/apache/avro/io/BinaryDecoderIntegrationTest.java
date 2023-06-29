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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class BinaryDecoderIntegrationTest {
  private byte[] bytes;
  private int start;
  private static ByteBuffer buf = ByteBuffer.allocate(10);
  private int len;
  private BinaryDecoder binaryDecoder;

  public BinaryDecoderIntegrationTest(byte[] bytes, int start, int len) {
    this.bytes = bytes;
    this.start = start;
    this.len = len;
  }

  @Before
  public void setUp() {
    try {
      binaryDecoder = DecoderFactory.get().binaryDecoder(bytes, null);
    } catch (NullPointerException e) {
      Assert.assertNull(bytes);
    }
  }

  @Parameterized.Parameters
  public static Collection<Object[]> getParameters() {
    return Arrays.asList(new Object[][] {
        // byte[], int, int, Bytebuf
        { "test".getBytes(), 1, 1},
        { "test".getBytes(), 1, 0 },
        {"test".getBytes(),0,1},
        {"test".getBytes(), -1, 0},
        {"test".getBytes(),-1,-1},
        { "".getBytes(), 0, 0 },
        { "".getBytes(), 1, 0 },
        {"".getBytes(),0,1},
        {"".getBytes(), -1, 0},
        {"".getBytes(),-1,-1},
        { null, 0, 0 },
        { null, 1, 0 },
        {null,0,1},
        {null, -1, 0},
        {null,-1,-1}
    });
  }
 /**
  * protected void doReadBytes(byte[] bytes, int start, int length)
  * */
  @Test
  public void doReadBytes() {
    try {
      binaryDecoder.doReadBytes(bytes, start, len);
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

  @Test
  public void testReadBytes() {
    try {
      binaryDecoder.readBytes(buf);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  @Test
  public void testReadFixed() {
    try {
      binaryDecoder.readFixed(buf.array(), start, buf.limit());
    }  catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSkipFixed() {
    try {
      binaryDecoder.skipFixed(buf.limit());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  @Test
  public void testSkipBytes() {
    try {
      binaryDecoder.skipBytes();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  @Test
  public void testSkipString() {
    try {
      binaryDecoder.skipString();
    }  catch (IOException e) {
      e.printStackTrace();
    }
  }

}
