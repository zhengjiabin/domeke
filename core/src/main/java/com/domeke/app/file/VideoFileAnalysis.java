package com.domeke.app.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.utils.FileKit;
import com.jfinal.kit.StrKit;

public class VideoFileAnalysis {
	/** 日志 */
	private Logger logger;

	private Pattern patternDuration;

	private Pattern patternVideo;

	private Pattern patternAudio;

	private static VideoFileAnalysis analysis = null;

	private VideoFileAnalysis() {
	}

	/**
	 * 单例模式初始化
	 * 
	 * @return
	 */
	public static VideoFileAnalysis getInstance() {
		if (analysis == null) {
			synchronized (VideoFileAnalysis.class) {
				if (analysis == null) {
					analysis = new VideoFileAnalysis();
					analysis.setLogger(LoggerFactory.getLogger(VideoFileAnalysis.class));
					String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
					String regexVideo = "Video: (.*?), (.*?), (.*?)[,\\s]";
					String regexAudio = "Audio: (\\w*), (\\d*) Hz";
					PatternCompiler compiler = new Perl5Compiler();
					try {
						Pattern patternDuration = compiler.compile(regexDuration, Perl5Compiler.CASE_INSENSITIVE_MASK);
						Pattern patternVideo = compiler.compile(regexVideo, Perl5Compiler.CASE_INSENSITIVE_MASK);
						Pattern patternAudio = compiler.compile(regexAudio, Perl5Compiler.CASE_INSENSITIVE_MASK);
						analysis.setPatternDuration(patternDuration);
						analysis.setPatternVideo(patternVideo);
						analysis.setPatternAudio(patternAudio);
					} catch (MalformedPatternException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return analysis;
	}

	/**
	 * 视频解析
	 * 
	 * @param descDirectory
	 *            原文件路径
	 * @param videoPath
	 *            目标文件路径
	 * @param command
	 *            指令
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static VideoFile videoProcess(FileInterface fileInstance) throws IOException, InterruptedException {
		VideoFileAnalysis analysis = VideoFileAnalysis.getInstance();
		String infos = analysis.process(fileInstance.getProcessCommendStr());
		if (StrKit.isBlank(infos)) {
			return null;
		}
		return analysis.getVideoFileInfo(infos, fileInstance);
	}

	/**
	 * 获取视频信息
	 * 
	 * @param infos
	 * @param descDirectory
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private VideoFile getVideoFileInfo(String infos, FileInterface fileInstance) throws IOException,
			InterruptedException {
		String descDirectory = fileInstance.getDescDirectory();
		String fileName = FileKit.getFileName(descDirectory);
		String virtualDirectory = FileKit.getVirtualDirectory(descDirectory);
		String fileType = FileKit.getFileType(fileName);
		VideoFile videoFile = analysis.buildVideoFile(infos);
		videoFile.setDescDirectory(descDirectory);
		videoFile.setFileName(fileName);
		videoFile.setFileType(fileType);
		videoFile.setOriginalDirectory(fileInstance.getOriginalDirectory());
		videoFile.setVirtualDirectory(virtualDirectory);
		videoFile.setHandled(true);
		return videoFile;
	}

	/**
	 * 构建视频信息
	 * 
	 * @return
	 */
	private VideoFile buildVideoFile(String infos) {
		VideoFile videoFile = new VideoFile();
		Logger logger = getLogger();
		PatternMatcher matcher = new Perl5Matcher();
		if (matcher.contains(infos, getPatternDuration())) {
			MatchResult re = matcher.getMatch();
			videoFile.setPlayTime(re.group(1));
			logger.info("播放时间 {}", re.group(1));
			logger.info("开始时间 {}", re.group(2));
			logger.info("码率单位{}", re.group(3));
		}
		if (matcher.contains(infos, getPatternVideo())) {
			MatchResult re = matcher.getMatch();
			videoFile.setResolution(re.group(3));
			logger.info("编码格式 {}", re.group(1));
			logger.info("视频格式 {}", re.group(2));
			logger.info("分辨率{}", re.group(3));
		}
		if (matcher.contains(infos, getPatternAudio())) {
			MatchResult re = matcher.getMatch();
			logger.info("音频编码 {}", re.group(1));
			logger.info("音频采样频率 {}", re.group(2));
		}
		return videoFile;
	}

	/**
	 * 执行视频指令
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private String process(List<String> command) throws IOException, InterruptedException {
		logger.info("视频压缩开始");
		printCommandInfo(command);
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line = buf.readLine();
		while (line != null) {
			sb.append(line);
			line = buf.readLine();
		}
		p.waitFor();
		logger.info("视频压缩结束");
		return sb.toString();
	}

	private String process(String command) {
		StringBuffer info = new StringBuffer();
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			logger.info("==============<ERROR>=============");
			while ((line = br.readLine()) != null) {
				info.append(line);
			}
			logger.info(info.toString());
			logger.info("==============<ERROR>=============");
			int exitVal = proc.waitFor();
			logger.info("Process exitValue:{} ", exitVal);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return info.toString();
	}

	/**
	 * 
	 * @param commend
	 */
	private void printCommandInfo(List<String> commandList) {
		StringBuffer commands = new StringBuffer();
		for (String command : commandList) {
			commands.append(" ");
			commands.append(command);
			commands.append(" ");
		}
		logger.info("压缩指令{}", commands.toString());
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	private Pattern getPatternDuration() {
		return patternDuration;
	}

	private void setPatternDuration(Pattern patternDuration) {
		this.patternDuration = patternDuration;
	}

	private Pattern getPatternVideo() {
		return patternVideo;
	}

	private void setPatternVideo(Pattern patternVideo) {
		this.patternVideo = patternVideo;
	}

	private Pattern getPatternAudio() {
		return patternAudio;
	}

	private void setPatternAudio(Pattern patternAudio) {
		this.patternAudio = patternAudio;
	}
}
