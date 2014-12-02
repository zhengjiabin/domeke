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
		command.add("-ab");
		command.add("56");
		command.add("-ac");
		command.add("2");
		command.add("-vcodec");
		command.add("libx264");
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
		command.add(getDescDirectory());

		return command;
	}
	
	public String getProcessCommendStr(){
		StringBuffer buffer = new StringBuffer(getProcessPath());
		buffer.append("-i");
		buffer.append(getOriginalDirectory());
		buffer.append("-ab");
		buffer.append("128k");
		buffer.append("-ac");
		buffer.append("2");
		buffer.append("-vcodec");
		buffer.append("libx264");
		buffer.append("-ar");
		buffer.append("22050");
		buffer.append("-crf");
		buffer.append("22");
		buffer.append("-r");
		buffer.append("22");
		buffer.append("-r");
		buffer.append("30");
		buffer.append("-threads");
		buffer.append("0");
		buffer.append("-qscale");
		buffer.append("6");
		buffer.append("-y");
		buffer.append(getDescDirectory());
		
		return buffer.toString();
	}
}
