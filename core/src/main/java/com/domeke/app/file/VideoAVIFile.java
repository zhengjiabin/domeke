package com.domeke.app.file;

import java.util.List;

import com.google.common.collect.Lists;

public class VideoAVIFile extends VideoFile {
	
	public VideoAVIFile(){
		
	}

	public VideoAVIFile(String mencoderPath) {
		setProcessPath(mencoderPath);
	}

	@Override
	public List<String> getProcessCommend() {
		List<String> command = Lists.newArrayList();
		command.add(getProcessPath());
		command.add(getOriginalDirectory());
		command.add("-o");
		command.add(getDescDirectory());
		command.add("-of");
		command.add("avi");
		
		command.add("-oac");
		command.add("mp3lame");
		command.add("-lameopts");
		command.add("abr:br=56");
		command.add("-ovc");
		command.add("xvid");
		command.add("-xvidencopts");
		command.add("bitrate=600");
		return command;
	}
}
