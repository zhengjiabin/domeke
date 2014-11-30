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
		command.add(" -i ");
		command.add(getOriginalDirectory());
		// 音频码率 32 64 96 128
		// command.add(" --acodec libfaac");
		// 使用codec编解码
		command.add(" -ab 128k");
		command.add(" -ac 2");
		// -ac channels 设置通道,缺省为1
		command.add(" -vcodec libx264");
		// -ar freq 设置音频采样率
		command.add(" -ar 22050");
		// -b bitrate 设置比特率,缺省200kb/s
		command.add(" -crf 22");
		// -r fps 设置帧频,缺省25
		command.add(" -r 30");
		// -qscale 6或4 使用动态码率来设置
		command.add(" -threads 0 ");
		command.add(" -qscale 6");

		command.add(" -y ");
		command.add(getDescDirectory());

		return command;
	}
}
