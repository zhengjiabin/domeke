package com.domeke.app.Schedule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.file.FileInterface;
import com.domeke.app.file.ImageFile;
import com.domeke.app.file.VideoFile;
import com.domeke.app.model.Work;
import com.domeke.app.utils.CollectionKit;
import com.domeke.app.utils.FileHandleScheduleKit;
import com.domeke.app.utils.FileKit;
import com.domeke.app.utils.MapKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;

public class WorkSchedule {

	private Logger logger = LoggerFactory.getLogger(WorkSchedule.class);

	/**
	 * 压缩视频
	 * 
	 * @param workId
	 */
	public void handleVideoFile(String workId) {
		Work work = Work.dao.getWorkNotHandled(workId);
		if (work == null) {
			return;
		}
		List<Work> workList = new ArrayList<Work>();
		workList.add(work);
		handleScheduleFile(workList);
	}

	/**
	 * 
	 */
	public void handleProcess() {
		logger.info("=============定时调度开始============");
		List<Work> workList = Work.dao.getWorkNotHandled();
		handleScheduleFile(workList);
		logger.info("=============定时调度结束============");

	}

	/**
	 * 处理定时任务
	 * 
	 * @param workList
	 */
	private void handleScheduleFile(List<Work> workList) {
		if (CollectionKit.isBlank(workList)) {
			return;
		}
		logger.info("=======待压缩视频数量===={}", workList.size());
		Map<String, FileInterface> data = FileHandleScheduleKit.handleScheduleFile("comic", workList);
		Map<String, VideoFile> videoFiles = buildVideoFile(data);

		if (MapKit.isBlank(videoFiles)) {
			return;
		}

		handelVideoFile(workList, videoFiles);
	}

	/**
	 * 处理压缩视频
	 * 
	 * @param workList
	 * @param videoFiles
	 */
	private void handelVideoFile(List<Work> workList, Map<String, VideoFile> videoFiles) {
		String comic = null, virtualDirectory = null, imgVirtualDirectory = null;
		VideoFile videoFile = null;
		List<ImageFile> imageList = null;
		ImageFile imageFile = null;
		Object workid = null;
		for (Work work : workList) {
			comic = work.getStr("comic");
			videoFile = videoFiles.get(comic);
			if (videoFile == null) {
				continue;
			}
			if (!videoFile.isHandled()) {
				continue;
			}
			workid = work.get("workid");
			work.clear();
			work.set("workid", workid);
			work.set("status", "20");

			virtualDirectory = videoFile.getVirtualDirectory();
			work.set("comic", virtualDirectory);
			imageList = videoFile.getImageFiles();
			String cover = null;
			if (CollectionKit.isNotBlank(imageList)) {
				imageFile = imageList.get(0);
				cover = imgVirtualDirectory = imageFile.getVirtualDirectory();
				work.set("cover", imgVirtualDirectory);
			}
			updateWork(Long.parseLong(workid.toString()), comic, cover);
		}
	}

	/**
	 * 获取视频文件
	 * 
	 * @param originalDirectory
	 * @param data
	 * @return
	 */
	private Map<String, VideoFile> buildVideoFile(Map<String, FileInterface> data) {
		if (MapKit.isBlank(data)) {
			return null;
		}
		Map<String, VideoFile> videoFiles = new HashMap<String, VideoFile>();
		String orgignalDirectory = null, virtualDirectory = null, originalFileName = null;
		VideoFile videoFile = null;
		for (String fileName : data.keySet()) {
			videoFile = (VideoFile) data.get(fileName);
			orgignalDirectory = videoFile.getOriginalDirectory();
			if (StrKit.isBlank(orgignalDirectory)) {
				continue;
			}
			originalFileName = FileKit.getFileName(orgignalDirectory);
			virtualDirectory = videoFile.getVirtualDirectory();
			orgignalDirectory = virtualDirectory.replaceFirst(fileName, originalFileName);
			videoFiles.put(orgignalDirectory, videoFile);
		}
		return videoFiles;
	}

	private void updateWork(Long workid, String comic, String cover) {

		try {
			// 加载MySql的驱动类
			Class.forName(PropKit.get("jdbc.driverClass"));
		} catch (ClassNotFoundException e) {
			logger.error("找不到驱动程序类 ，加载驱动失败！");
			e.printStackTrace();
		}

		// 连接MySql数据库，用户名和密码都是root
		String url = PropKit.get("jdbc.url");
		String username = PropKit.get("jdbc.username");
		String password = PropKit.get("jdbc.password");
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException se) {
			logger.error("数据库连接失败！" + se);
			se.printStackTrace();
		}

		try {
			con.setAutoCommit(false);
			StringBuffer bufferSql = new StringBuffer(" update work set status = '20' ,comic = ? where workid =  ? ");
			PreparedStatement ptst = con.prepareStatement(bufferSql.toString());
			ptst.setString(1, comic);
			ptst.setLong(2, workid);
			ptst.execute();
			if (StringUtils.isNoneBlank(cover)) {
				ptst.execute(" update work set cover = " + cover + " where workid =  '" + workid + "'");
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
