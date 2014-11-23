package com.domeke.app.file;

public abstract class FFMPEGLocator
{
  protected abstract String getFFMPEGExecutablePath();

  FFMPEGExecutor createExecutor()
  {
    return new FFMPEGExecutor(getFFMPEGExecutablePath());
  }
}