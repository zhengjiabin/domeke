package com.domeke.app.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

class RBufferedReader extends BufferedReader {
	private ArrayList lines = new ArrayList();

	public RBufferedReader(Reader in) {
		super(in);
	}

	public String readLine() throws IOException {
		if (this.lines.size() > 0)
			return ((String) this.lines.remove(0));

		return super.readLine();
	}

	public void reinsertLine(String line) {
		this.lines.add(0, line);
	}
}