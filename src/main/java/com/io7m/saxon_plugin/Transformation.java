package com.io7m.saxon_plugin;

import java.io.File;
import java.util.Properties;

public final class Transformation
{
  private final Properties stylesheetParameters = new Properties();
  private String           stylesheetFile;
  private String           documentFile;
  private String           outputFile;
  private String           outputDirectory;
  private String           outputDirectoryParameterName;

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
    if (this.outputDirectoryParameterName == null) {
      if (other.outputDirectoryParameterName != null) {
        return false;
      }
    } else if (!this.outputDirectoryParameterName
      .equals(other.outputDirectoryParameterName)) {
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

  public String getDocumentFile()
  {
    return this.documentFile;
  }

  public String getOutputDirectory()
  {
    return this.outputDirectory;
  }

  public String getOutputDirectoryParameterName()
  {
    return this.outputDirectoryParameterName;
  }

  public String getOutputFile()
  {
    return this.outputFile;
  }

  public String getStylesheetFile()
  {
    return this.stylesheetFile;
  }

  public Properties getStylesheetParameters()
  {
    return this.stylesheetParameters;
  }

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
        + ((this.outputDirectoryParameterName == null)
          ? 0
          : this.outputDirectoryParameterName.hashCode());
    result =
      (prime * result)
        + ((this.outputFile == null) ? 0 : this.outputFile.hashCode());
    result =
      (prime * result)
        + ((this.stylesheetFile == null) ? 0 : this.stylesheetFile.hashCode());
    return result;
  }

  public void setDocumentFile(
    final String documentFile)
  {
    final String file = new File(documentFile).getAbsolutePath();
    this.documentFile = file;
  }

  public void setOutputDirectory(
    final String outputDirectory)
  {
    final String file = new File(outputDirectory).getAbsolutePath();
    this.outputDirectory = file;
  }

  public void setOutputDirectoryParameterName(
    final String outputDirectoryParameterName)
  {
    this.outputDirectoryParameterName = outputDirectoryParameterName;
  }

  public void setOutputFile(
    final String outputFile)
  {
    this.outputFile = outputFile;
  }

  public void setStylesheetFile(
    final String stylesheetFile)
  {
    final String file = new File(stylesheetFile).getAbsolutePath();
    this.stylesheetFile = file;
  }
}
