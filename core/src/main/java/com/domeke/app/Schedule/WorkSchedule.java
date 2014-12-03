package com.domeke.app.Schedule;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class WorkSchedule {

	private Logger logger = LoggerFactory.getLogger(WorkSchedule.class);

	public void handleProcess() {
		logger.info("=============定时调度开始============");
		List<Work> workList = Work.dao.getWorkNotHandled();

		if (workList != null && workList.size() > 0) {
			logger.info("=======待压缩视频数量===={}", workList.size());
			Map<String, FileInterface> data = FileHandleScheduleKit
					.handleScheduleFile("comic", workList);
			Map<String, VideoFile> videoFiles = buildVideoFile(data);

			if (MapKit.isBlank(videoFiles)) {
				return;
			}

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
				if (CollectionKit.isNotBlank(imageList)) {
					imageFile = imageList.get(0);
					imgVirtualDirectory = imageFile.getVirtualDirectory();
					work.set("cover", imgVirtualDirectory);
				}
				Db.tx(new IAtom() {
					public boolean run() throws SQLException {
						int count = Db.update("work", work);
						return count == 1;
					}
				});

			}
		}
		logger.info("=============定时调度结束============");

	}

	/**
	 * 获取视频文件
	 * 
	 * @param originalDirectory
	 * @param data
	 * @return
	 */
	private Map<String, VideoFile> buildVideoFile(
			Map<String, FileInterface> data) {
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
			orgignalDirectory = virtualDirectory.replaceFirst(fileName,
					originalFileName);
			videoFiles.put(orgignalDirectory, videoFile);
		}
		return videoFiles;
	}
}
