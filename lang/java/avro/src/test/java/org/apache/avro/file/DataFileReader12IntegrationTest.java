package org.apache.avro.file;

import org.apache.avro.InvalidAvroMagicException;
import org.apache.avro.io.DatumReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataFileReader12IntegrationTest {
  private DataFileReader12 dataFileReader12;
  private long position;

  //in thi test class, i do an integration test with the DataFileReader12 class and the BinaryDecoder class to see if they are compatible with each other and if they work together
  //i do this by testing the seek() and sync() methods of the DataFileReader12 class

  @Before
  public void setUp() throws IOException {
    SeekableInput seekableInputMock = mock(SeekableInput.class);
    DatumReader datumReaderMock = mock(DatumReader.class);
    DataFileReader12 dataFileReader12 = mock(DataFileReader12.class);

    SeekableInput sin = new SeekableByteArrayInput(Arrays.copyOf("test".getBytes(), 10));
    InvalidAvroMagicException mockEx = mock(InvalidAvroMagicException.class);

    doNothing().when(dataFileReader12).seek(position);

    dataFileReader12 = new DataFileReader12(sin, datumReaderMock);


  }

  @Test
  public void sync() {
    try{
      dataFileReader12.sync(position);
    }catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
