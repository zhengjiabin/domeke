package com.domeke.app.file;

public class InputFormatException extends EncoderException {
	private static final long serialVersionUID = 1L;

	InputFormatException() {
	}

	InputFormatException(String message) {
		super(message);
	}
}