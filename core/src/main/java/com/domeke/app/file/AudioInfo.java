package com.domeke.app.file;

public class AudioInfo {
	private String decoder;
	private int samplingRate = -1;
	private int channels = -1;
	private int bitRate = -1;

	public String getDecoder() {
		return this.decoder;
	}

	void setDecoder(String format) {
		this.decoder = format;
	}

	public int getSamplingRate() {
		return this.samplingRate;
	}

	void setSamplingRate(int samplingRate) {
		this.samplingRate = samplingRate;
	}

	public int getChannels() {
		return this.channels;
	}

	void setChannels(int channels) {
		this.channels = channels;
	}

	public int getBitRate() {
		return this.bitRate;
	}

	void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}

	public String toString() {
		return super.getClass().getName() + " (decoder=" + this.decoder
				+ ", samplingRate=" + this.samplingRate + ", channels="
				+ this.channels + ", bitRate=" + this.bitRate + ")";
	}
}