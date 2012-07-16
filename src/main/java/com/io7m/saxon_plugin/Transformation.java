package com.io7m.saxon_plugin;

import java.util.Properties;

public final class Transformation
{
  private final Properties stylesheetParameters = new Properties();
  private String           stylesheetFile;
  private String           documentFile;
  private String           outputFile;
  private String           outputDirectory;

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result =
      (prime * result)
        + ((this.documentFile == null) ? 0 : this.documentFile.hashCode());
    result =
      (prime * result)
        + ((this.outputDirectory == null) ? 0 : this.outputDirectory
          .hashCode());
    result =
      (prime * result)
        + ((this.outputFile == null) ? 0 : this.outputFile.hashCode());
    result =
      (prime * result)
        + ((this.stylesheetFile == null) ? 0 : this.stylesheetFile.hashCode());
    return result;
  }

  @Override public boolean equals(
    final Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Transformation other = (Transformation) obj;
    if (this.documentFile == null) {
      if (other.documentFile != null) {
        return false;
      }
    } else if (!this.documentFile.equals(other.documentFile)) {
      return false;
    }
    if (this.outputDirectory == null) {
      if (other.outputDirectory != null) {
        return false;
      }
    } else if (!this.outputDirectory.equals(other.outputDirectory)) {
      return false;
    }
    if (this.outputFile == null) {
      if (other.outputFile != null) {
        return false;
      }
    } else if (!this.outputFile.equals(other.outputFile)) {
      return false;
    }
    if (this.stylesheetFile == null) {
      if (other.stylesheetFile != null) {
        return false;
      }
    } else if (!this.stylesheetFile.equals(other.stylesheetFile)) {
      return false;
    }
    return true;
  }

  public void setStylesheetFile(
    final String stylesheetFile)
  {
    this.stylesheetFile = stylesheetFile;
  }

  public void setDocumentFile(
    final String documentFile)
  {
    this.documentFile = documentFile;
  }

  public void setOutputFile(
    final String outputFile)
  {
    this.outputFile = outputFile;
  }

  public void setOutputDirectory(
    final String outputDirectory)
  {
    this.outputDirectory = outputDirectory;
  }

  /**
   * Check this Transformation for completeness. That is, check that all
   * required parameters have been given.
   */

  public void check()
  {
    if (this.stylesheetFile == null) {
      throw new IllegalArgumentException("stylesheet not specified");
    }
    if (this.documentFile == null) {
      throw new IllegalArgumentException("input document not specified");
    }
    if (this.outputFile == null) {
      throw new IllegalArgumentException("output document not specified");
    }
    if (this.stylesheetParameters == null) {
      throw new IllegalArgumentException("parameters not specified");
    }
    if (this.outputDirectory == null) {
      throw new IllegalArgumentException("output directory not specified");
    }
  }

  public String getStylesheetFile()
  {
    return this.stylesheetFile;
  }

  public String getDocumentFile()
  {
    return this.documentFile;
  }

  public String getOutputFile()
  {
    return this.outputFile;
  }

  public String getOutputDirectory()
  {
    return this.outputDirectory;
  }

  public Properties getStylesheetParameters()
  {
    return this.stylesheetParameters;
  }
}
