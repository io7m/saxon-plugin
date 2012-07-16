package com.io7m.saxon_plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.saxon.s9api.SaxonApiException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Goal which processes an XML file with an XSLT stylesheet.
 * 
 * @goal transform
 * @phase generate-resources
 */

public class SaxonMojo extends AbstractMojo
{
  /**
   * A list of transformations to perform.
   * 
   * @parameter
   */

  private Transformation[] transformations;

  private void announce(
    final Transformation t)
  {
    final Log log = this.getLog();
    final String out =
      t.getOutputDirectory() + File.separatorChar + t.getOutputFile();
    log.info("Transform: "
      + t.getDocumentFile()
      + " using "
      + t.getStylesheetFile()
      + " to "
      + out);
  }

  @Override public void execute()
    throws MojoExecutionException
  {
    try {
      if (this.transformations == null) {
        throw new MojoExecutionException("no transformations specified");
      }

      for (final Transformation config : this.transformations) {
        assert config != null;
        config.check();
        this.announce(config);

        final Transform t = new Transform(config);
        t.transform();
        t.close();
      }
    } catch (final FileNotFoundException e) {
      throw new MojoExecutionException("File not found", e);
    } catch (final SaxonApiException e) {
      throw new MojoExecutionException("Saxon error", e);
    } catch (final IOException e) {
      throw new MojoExecutionException("I/O error", e);
    }
  }
}
