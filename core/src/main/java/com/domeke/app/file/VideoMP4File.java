package com.domeke.app.file;

import java.util.List;

import com.google.common.collect.Lists;

public class VideoMP4File extends VideoFile {

	public VideoMP4File() {

	}

	public VideoMP4File(String ffmepgPath) {
		setProcessPath(ffmepgPath);
	}

	@Override
	public List<String> getProcessCommend() {
		List<String> command = Lists.newArrayList();
		command.add(getProcessPath());
		command.add("-i");
		command.add(getOriginalDirectory());
		// 音频码率 32 64 96 128
		// command.add(" --acodec libfaac");
		// 使用codec编解码
		command.add(" -ab");
		command.add("128k");
		command.add("-ac");
		// command.add("-vcodec");
		// command.add("libx264");
		command.add("-ar");
		command.add("22050");
		command.add("-crf");
		command.add("22");
		command.add("-r");
		command.add("22");
		command.add("-r");
		command.add("30");
		command.add("-threads");
		command.add("0");
		command.add("-qscale");
		command.add("6");
		command.add("-y");
		command.add("0");
		command.add(getDescDirectory());

		return command;
	}
}
