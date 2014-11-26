package com.domeke.app.file;

import java.io.BufferedReader;
import java.io.IOException;
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

public class ImageFileAnalysis {
	
	/** 日志 */
	private Logger logger;
    private Pattern patternImage;

	private static ImageFileAnalysis analysis = null;

	/**
	 * 单例模式初始化
	 * 
	 * @return
	 */
	public static ImageFileAnalysis getInstance() {
		if (analysis == null) {
			synchronized (ImageFileAnalysis.class) {
				if (analysis == null) {
					analysis = new ImageFileAnalysis();
					analysis.setLogger(LoggerFactory.getLogger(ImageFileAnalysis.class));
					
					String regexImage = "Video: (.*?), (.*?), (.*?)[,\\s]";  
					PatternCompiler compiler =new Perl5Compiler(); 
					try {
						Pattern patternImage = compiler.compile(regexImage, Perl5Compiler.CASE_INSENSITIVE_MASK);
						
						analysis.setPatternImage(patternImage);
					} catch (MalformedPatternException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return analysis;
	}
	
	/**
	 * 根据视频截图
	 * @param descDirectory 视频物理地址
	 * @param imagePath 图片物理地址
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static ImageFile imageProcess(FileInterface fileInstance) throws IOException, InterruptedException{
		ImageFileAnalysis analysis = ImageFileAnalysis.getInstance();
		String infos = analysis.process(fileInstance.getProcessCommend());
		if(StrKit.isBlank(infos)){
			return null;
		}
		return analysis.getImageFileInfo(infos,fileInstance);
	}

	
	/**
	 * 获取图片信息
	 */
	private ImageFile getImageFileInfo(String infos,FileInterface fileInstance){
		String descDirectory = fileInstance.getDescDirectory();
		String fileName = FileKit.getFileName(descDirectory);
		String fileType = FileKit.getFileType(fileName);
		ImageFile imageFile = analysis.buildImageFile(infos);
		
		imageFile.setDescDirectory(descDirectory);
		imageFile.setFileName(fileName);
		imageFile.setFileType(fileType);
		imageFile.setOriginalDirectory(fileInstance.getOriginalDirectory());
		return imageFile;
	}
	
	/**
	 * 构建图片信息
	 * @return
	 */
	private ImageFile buildImageFile(String infos){
		ImageFile imageFile = new ImageFile();
		Logger logger = getLogger();
		PatternMatcher matcher = new Perl5Matcher();
		if (matcher.contains(infos, getPatternImage())) {
			MatchResult re = matcher.getMatch();
			imageFile.setResolution(re.group(3));
			
			logger.info("图片类型 {}", re.group(1));
			logger.info("分辨率{}", re.group(3));
		}
		return imageFile;
	}
	
	/**
	 * 执行图片处理指令
	 * @param commend
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private String process(List<String> commend) throws IOException, InterruptedException{
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(commend);
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
		return sb.toString();
	}

	private Logger getLogger() {
		return logger;
	}

	private void setLogger(Logger logger) {
		this.logger = logger;
	}

	private Pattern getPatternImage() {
		return patternImage;
	}

	private void setPatternImage(Pattern patternImage) {
		this.patternImage = patternImage;
	}
	
}
