package com.domeke.app.file;

import java.util.List;

import com.google.common.collect.Lists;

public class VideoMP4File extends VideoFile {
	
	public VideoMP4File(){
		
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
		command.add("-ab");
		command.add("64");
		// 设置声道数,缺省为1
		command.add("-ac");
		command.add("2");
		// 设置音频采样率
		command.add("-ar");
		command.add("22050");
		// -b bitrate 设置比特率,缺省200kb/s
		command.add("-b");
		command.add("250");
		// 设置帧频,缺省25
		command.add("-r");
		command.add("30");
		// 指定转换的质量 6 4
		command.add("-qscale");
		command.add("6");
		// 指定将覆盖已存在的文件
		command.add("-y");
		command.add(getDescDirectory());

		return command;
	}
}
