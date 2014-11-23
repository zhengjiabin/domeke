package com.domeke.app.file;

import java.io.Serializable;

public class EncodingAttributes implements Serializable {
	private static final long serialVersionUID = 1L;
	private String format = null;
	private Float offset = null;
	private Float duration = null;
	private AudioAttributes audioAttributes = null;
	private VideoAttributes videoAttributes = null;

	String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	Float getOffset() {
		return this.offset;
	}

	public void setOffset(Float offset) {
		this.offset = offset;
	}

	Float getDuration() {
		return this.duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}

	AudioAttributes getAudioAttributes() {
		return this.audioAttributes;
	}

	public void setAudioAttributes(AudioAttributes audioAttributes) {
		this.audioAttributes = audioAttributes;
	}

	VideoAttributes getVideoAttributes() {
		return this.videoAttributes;
	}

	public void setVideoAttributes(VideoAttributes videoAttributes) {
		this.videoAttributes = videoAttributes;
	}

	public String toString() {
		return super.getClass().getName() + "(format=" + this.format
				+ ", offset=" + this.offset + ", duration=" + this.duration
				+ ", audioAttributes=" + this.audioAttributes
				+ ", videoAttributes=" + this.videoAttributes + ")";
	}
}