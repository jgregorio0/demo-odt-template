package dev.jgregorio.demo.demo_template.service;

import java.io.FilterOutputStream;
import java.io.OutputStream;

/**
 * A FilterOutputStream that ignores the close() call. Useful when passing a OutputStream to a
 * library that attempts to close the stream.
 */
public class NonClosingOutputStream extends FilterOutputStream {
  public NonClosingOutputStream(final OutputStream out) {
    super(out);
  }

  @Override
  public void close() {
    // Do not close the underlying stream
  }
}
