package org.example;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
  public static File stream2file(InputStream in, String name, String extension) throws IOException {
    final File tempFile = File.createTempFile(name, extension);
    tempFile.deleteOnExit();
    try (FileOutputStream out = new FileOutputStream(tempFile)) {
      IOUtils.copy(in, out);
    }
    return tempFile;
  }
}
