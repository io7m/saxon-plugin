package com.io7m.saxon_plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
  private final Configuration   config;
  private final Processor       processor;
  private final XsltCompiler    xslt;
  private final XsltExecutable  xslt_exec;

  private final XsltTransformer xslt_trans;
  private final File            style_file;

  private final StreamSource    style_source;
  private final File            input_file;

  private final StreamSource    input_source;
  private final File            output_file;
  private final Serializer      output_serial;
  private final String          output_path;

  private final Transformation  transformation;

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
    this.transformation = transformation;

    Transform.createOutputDirectory(transformation);

    this.style_file = new File(transformation.getStylesheetFile());
    if (this.style_file.isFile() == false) {
      final String path = this.style_file.toString();
      throw new FileNotFoundException(path);
    }
    this.style_source = new StreamSource(this.style_file);

    this.input_file = new File(transformation.getDocumentFile());
    if (this.input_file.isFile() == false) {
      final String path = this.input_file.toString();
      throw new FileNotFoundException(path);
    }
    this.input_source = new StreamSource(this.input_file);

    this.output_path =
      transformation.getOutputDirectory()
        + File.separatorChar
        + transformation.getOutputFile();
    this.output_file = new File(this.output_path);
    this.output_serial = new Serializer(this.output_file);

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

    if (transformation.getOutputDirectoryParameterName() != null) {
      final String path =
        new File(transformation.getOutputDirectory()).toURI().toString();
      final String key = transformation.getOutputDirectoryParameterName();
      this.xslt_trans.setParameter(new QName(key), new XdmAtomicValue(path));
    }
  }

  void announce(
    final OutputStream s)
    throws IOException
  {
    final Writer w = new OutputStreamWriter(s);
    final String out =
      this.transformation.getOutputDirectory()
        + File.separatorChar
        + this.transformation.getOutputFile();

    w.write("Transform document      : "
      + this.transformation.getDocumentFile()
      + "\n");
    w.write("Transform stylesheet    : "
      + this.transformation.getStylesheetFile()
      + "\n");
    w.write("Transform directory     : "
      + this.transformation.getOutputDirectory()
      + "\n");
    w.write("Transform output file   : "
      + this.transformation.getOutputFile()
      + "\n");
    w.write("Transform output actual : " + out + "\n");
    w.write("--\n");
    w.flush();
  }

  public void close()
  {
    try {
      if (this.xslt_trans != null) {
        this.xslt_trans.close();
      }
    } catch (final SaxonApiException e) {
      e.printStackTrace();
    }
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

  @Override public void message(
    final XdmNode node,
    final boolean terminate,
    final SourceLocator locator)
  {
    System.err.println("message: " + node.getStringValue());
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

  @Override public void warning(
    final TransformerException exception)
    throws TransformerException
  {
    System.err.println("warn: " + exception.getMessageAndLocation());
  }
}
