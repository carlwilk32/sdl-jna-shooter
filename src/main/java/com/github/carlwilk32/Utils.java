package com.github.carlwilk32;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

@Slf4j
public class Utils {
  public static File stream2file(InputStream in, String name, String extension) throws IOException {
    final File tempFile = File.createTempFile(name, extension);
    tempFile.deleteOnExit();
    try (FileOutputStream out = new FileOutputStream(tempFile)) {
      IOUtils.copy(in, out);
    }
    return tempFile;
  }

  public static String getFullPath(String fileName) {
    try {
      var resourceAsStream =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
      var split = fileName.split("\\.");
      var tempFile = stream2file(resourceAsStream, "temp", split[1]);
      return tempFile.toURI().getPath();
    } catch (IOException e) {
      log.error("Unable to process file {}", fileName);
      throw new RuntimeException(e);
    }
  }
}
