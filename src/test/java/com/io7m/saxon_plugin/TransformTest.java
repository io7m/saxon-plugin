package com.io7m.saxon_plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import net.sf.saxon.s9api.SaxonApiException;

import org.junit.Test;

public class TransformTest
{
  private static final String TEST_XSL_NONEXISTENT;
  private static final String TEST_XSL_INVALID;
  private static final String TEST_XSL;
  private static final String TEST_XML;
  private static final String TEST_XML_NONEXISTENT;
  private static final String TEST_XML_INVALID;
  private static final String TEST_OUT;
  private static final String TEST_OUT_DIR;

  static {
    TEST_XSL_NONEXISTENT = "/nonexistent";
    TEST_XSL_INVALID = "src/test/xsl/invalid.xsl";
    TEST_XSL = "src/test/xsl/test.xsl";
    TEST_XML_NONEXISTENT = "/nonexistent";
    TEST_XML_INVALID = "src/test/xml/invalid.xml";
    TEST_XML = "src/test/xml/test.xml";
    TEST_OUT = "output.xml";
    TEST_OUT_DIR = "target/xyz";
  }

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testTransformNoStylesheet()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    new Transform(tr);
  }

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testTransformNoDocument()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL);
    new Transform(tr);
  }

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testTransformNoOutput()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL);
    tr.setDocumentFile(TransformTest.TEST_XML);
    new Transform(tr);
  }

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testTransformNoParameters()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL);
    tr.setDocumentFile(TransformTest.TEST_XML);
    tr.setOutputFile(TransformTest.TEST_OUT);
    new Transform(tr);
  }

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = FileNotFoundException.class) public
    void
    testTransformNonexistentStylesheet()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL_NONEXISTENT);
    tr.setDocumentFile(TransformTest.TEST_XML);
    tr.setOutputFile(TransformTest.TEST_OUT);
    tr.setOutputDirectory(TransformTest.TEST_OUT_DIR);
    new Transform(tr);
  }

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testTransformNoDirectory()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL_NONEXISTENT);
    tr.setDocumentFile(TransformTest.TEST_XML);
    tr.setOutputFile(TransformTest.TEST_OUT);
    new Transform(tr);
  }

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = FileNotFoundException.class) public
    void
    testTransformNonexistentInput()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL);
    tr.setDocumentFile(TransformTest.TEST_XML_NONEXISTENT);
    tr.setOutputFile(TransformTest.TEST_OUT);
    tr.setOutputDirectory(TransformTest.TEST_OUT_DIR);
    new Transform(tr);
  }

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = SaxonApiException.class) public
    void
    testTransformInvalidStylesheet()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL_INVALID);
    tr.setDocumentFile(TransformTest.TEST_XML);
    tr.setOutputFile(TransformTest.TEST_OUT);
    tr.setOutputDirectory(TransformTest.TEST_OUT_DIR);
    new Transform(tr);
  }

  @SuppressWarnings({ "static-method" }) @Test(
    expected = SaxonApiException.class) public
    void
    testTransformInvalidDocument()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL);
    tr.setDocumentFile(TransformTest.TEST_XML_INVALID);
    tr.setOutputFile(TransformTest.TEST_OUT);
    tr.setOutputDirectory(TransformTest.TEST_OUT_DIR);
    final Transform t = new Transform(tr);
    t.transform();
    t.close();
  }

  @SuppressWarnings({ "static-method" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testTransformBadKeyType()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL);
    tr.setDocumentFile(TransformTest.TEST_XML);
    tr.setOutputFile(TransformTest.TEST_OUT);
    tr.setOutputDirectory(TransformTest.TEST_OUT_DIR);

    final Properties p = tr.getStylesheetParameters();
    p.put(Integer.valueOf(23), "value");

    final Transform t = new Transform(tr);
    t.transform();
    t.close();
  }

  @SuppressWarnings({ "static-method" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testTransformBadValueType()
      throws SaxonApiException,
        IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL);
    tr.setDocumentFile(TransformTest.TEST_XML);
    tr.setOutputFile(TransformTest.TEST_OUT);
    tr.setOutputDirectory(TransformTest.TEST_OUT_DIR);

    final Properties p = tr.getStylesheetParameters();
    p.put("key", Integer.valueOf(23));

    final Transform t = new Transform(tr);
    t.transform();
    t.close();
  }

  @SuppressWarnings({ "static-method" }) @Test public void testTransformOK()
    throws SaxonApiException,
      IOException
  {
    final Transformation tr = new Transformation();
    tr.setStylesheetFile(TransformTest.TEST_XSL);
    tr.setDocumentFile(TransformTest.TEST_XML);
    tr.setOutputFile(TransformTest.TEST_OUT);
    tr.setOutputDirectory(TransformTest.TEST_OUT_DIR);

    final Properties p = tr.getStylesheetParameters();
    p.put("key", "value");

    final Transform t = new Transform(tr);
    t.transform();
    t.close();
  }
}
