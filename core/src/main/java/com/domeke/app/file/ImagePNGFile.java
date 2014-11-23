package com.domeke.app.file;

import java.util.List;

import com.google.common.collect.Lists;

public class ImagePNGFile extends FileInterface {
	
	public ImagePNGFile(){
		
	}
	
	public ImagePNGFile(String processPath){
		setProcessPath(processPath);
	}

	@Override
	public List<String> getProcessCommend() {
		List<String> command = Lists.newArrayList();
		command.add(getProcessPath());
		command.add("-ss");
		command.add("3");
		command.add("-i");
		command.add(getOriginalDirectory());
		command.add(getDescDirectory());
		command.add("-r");
		command.add("1");
		command.add("-vframes");
		command.add("1");
		command.add("-an");
		command.add("-vcodec");
		command.add("mjpeg");
		return command;
	}
}
