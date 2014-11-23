package com.domeke.app.file;

import java.io.Serializable;

public class VideoSize implements Serializable {
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;

	public VideoSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public String toString() {
		return super.getClass().getName() + " (width=" + this.width
				+ ", height=" + this.height + ")";
	}
}