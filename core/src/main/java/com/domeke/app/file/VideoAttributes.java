package com.domeke.app.file;

import java.io.Serializable;

public class VideoAttributes implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String DIRECT_STREAM_COPY = "copy";
	private String codec = null;
	private String tag = null;
	private Integer bitRate = null;
	private Integer frameRate = null;
	private VideoSize size = null;

	String getCodec() {
		return this.codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	Integer getBitRate() {
		return this.bitRate;
	}

	public void setBitRate(Integer bitRate) {
		this.bitRate = bitRate;
	}

	Integer getFrameRate() {
		return this.frameRate;
	}

	public void setFrameRate(Integer frameRate) {
		this.frameRate = frameRate;
	}

	VideoSize getSize() {
		return this.size;
	}

	public void setSize(VideoSize size) {
		this.size = size;
	}

	public String toString() {
		return super.getClass().getName() + "(codec=" + this.codec
				+ ", bitRate=" + this.bitRate + ", frameRate=" + this.frameRate
				+ ", size=" + this.size + ")";
	}
}