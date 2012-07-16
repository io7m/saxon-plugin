package com.io7m.saxon_plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.s9api.MessageListener;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

public final class Transform implements MessageListener, ErrorListener
{
  private final Configuration    config;
  private final Processor        processor;
  private final XsltCompiler     xslt;
  private final XsltExecutable   xslt_exec;
  private final XsltTransformer  xslt_trans;

  private final FileInputStream  style_stream;
  private final StreamSource     style_source;
  private final FileInputStream  input_stream;
  private final StreamSource     input_source;
  private final FileOutputStream output_stream;
  private final Serializer       output_serial;
  private final String           output_file;

  private static void createOutputDirectory(
    final Transformation transform)
    throws IOException
  {
    final File dir = new File(transform.getOutputDirectory());
    if (dir.exists()) {
      if (dir.isDirectory()) {
        return;
      }
      throw new IOException("could not create directory: "
        + transform.getOutputDirectory()
        + " - already exists (and is not a directory)");
    }

    final boolean ok = dir.mkdirs();
    if (ok == false) {
      throw new IOException("could not create directory: "
        + transform.getOutputDirectory());
    }
  }

  public Transform(
    final Transformation transformation)
    throws SaxonApiException,
      IOException
  {
    assert transformation != null;
    transformation.check();

    Transform.createOutputDirectory(transformation);

    this.style_stream =
      new FileInputStream(transformation.getStylesheetFile());
    this.style_source = new StreamSource(this.style_stream);

    this.input_stream = new FileInputStream(transformation.getDocumentFile());
    this.input_source = new StreamSource(this.input_stream);

    this.output_file =
      transformation.getOutputDirectory()
        + File.separatorChar
        + transformation.getOutputFile();
    this.output_stream = new FileOutputStream(this.output_file);
    this.output_serial = new Serializer(this.output_stream);

    assert this.style_stream == this.style_source.getInputStream();
    assert this.input_stream == this.input_source.getInputStream();
    assert this.output_stream == this.output_serial.getOutputDestination();

    this.config = new Configuration();
    this.config.setXIncludeAware(true);
    this.config.setValidation(false);
    this.config.setLineNumbering(true);
    this.config.setValidationWarnings(true);
    this.processor = new Processor(this.config);

    this.xslt = this.processor.newXsltCompiler();
    this.xslt_exec = this.xslt.compile(this.style_source);
    this.xslt_trans = this.xslt_exec.load();
    this.xslt_trans.setDestination(this.output_serial);
    this.xslt_trans.setMessageListener(this);
    this.xslt_trans.setErrorListener(this);
    this.xslt_trans.setSource(this.input_source);

    final Properties parameters = transformation.getStylesheetParameters();
    for (final Entry<Object, Object> e : parameters.entrySet()) {
      final Object okey = e.getKey();
      if ((okey instanceof String) == false) {
        throw new IllegalArgumentException("Key "
          + okey
          + " is not a string (is "
          + okey.getClass().getCanonicalName()
          + ")");
      }
      final String key = (String) e.getKey();
      final Object oval = e.getValue();
      if ((oval instanceof String) == false) {
        throw new IllegalArgumentException("Key "
          + key
          + " has non-string value (is "
          + oval.getClass().getCanonicalName()
          + ")");
      }
      final String val = (String) e.getValue();
      this.xslt_trans.setParameter(new QName(key), new XdmAtomicValue(val));
    }
  }

  @Override public void message(
    final XdmNode node,
    final boolean terminate,
    final SourceLocator locator)
  {
    System.err.println("message: " + node.getStringValue());
  }

  @Override public void error(
    final TransformerException exception)
    throws TransformerException
  {
    System.err.println("error: " + exception.getMessageAndLocation());
  }

  @Override public void fatalError(
    final TransformerException exception)
    throws TransformerException
  {
    System.err.println("fatal: " + exception.getMessageAndLocation());
  }

  @Override public void warning(
    final TransformerException exception)
    throws TransformerException
  {
    System.err.println("warn: " + exception.getMessageAndLocation());
  }

  /**
   * @throws IOException
   */

  public void transform()
    throws SaxonApiException,
      IOException
  {
    this.xslt_trans.transform();
  }

  public void close()
  {
    try {
      if (this.xslt_trans != null) {
        this.xslt_trans.close();
      }
      if (this.input_stream != null) {
        this.input_stream.close();
      }
      if (this.output_stream != null) {
        this.output_stream.close();
      }
      if (this.style_stream != null) {
        this.style_stream.close();
      }
    } catch (final SaxonApiException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
