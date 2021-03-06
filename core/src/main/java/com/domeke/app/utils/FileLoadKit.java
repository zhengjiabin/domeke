package com.domeke.app.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.cos.multipart.FilePart;
import com.domeke.app.file.FileInstance;
import com.domeke.app.file.FileInterface;
import com.domeke.app.file.ImageFile;
import com.domeke.app.file.VideoFile;
import com.domeke.app.model.User;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;

public class FileLoadKit {
    
    private volatile static FileLoadKit fileUploadKit;
    
    /** 日志 */
    private static Logger logger = LoggerFactory.getLogger(FileLoadKit.class);
    
    /** 文件上传的本地根目录 */
    private String baseDirectory;
    
    /** 文件上传的本地备份根目录 */
    private String backupsDirectory;
    
    /** 图片上传的临时目录 */
    private String imageDirectory;
    
    /** 视频上传的临时目录 */
    private String videoDirectory;
    
    /** 文件上传的临时目录 */
    private String fileDirectory;
    
    private FileLoadKit() {
        
    }
    
    /**
     * 单例模式初始化
     * 
     * @return
     */
    public static FileLoadKit getInstance() {
        if (fileUploadKit == null) {
            synchronized (FileLoadKit.class) {
                if (fileUploadKit == null) {
                    fileUploadKit = new FileLoadKit();
                    
                    String baseDirectory = PropKit.getString("basePath");
                    fileUploadKit.setBaseDirectory(baseDirectory);
                    
                    String backupsDirectory = PropKit.getString("backupsPath");
                    fileUploadKit.setBackupsDirectory(backupsDirectory);
                    
                    String imageDirectory = PropKit.getString("imagePath");
                    fileUploadKit.setImageDirectory(imageDirectory);
                    
                    String videoDirectory = PropKit.getString("videoPath");
                    fileUploadKit.setVideoDirectory(videoDirectory);
                    
                    String fileDirectory = PropKit.getString("filePath");
                    fileUploadKit.setFileDirectory(fileDirectory);
                }
            }
        }
        return fileUploadKit;
    }
    
    /**
     * 图片上传
     * 
     * @param ctrl
     * @param parameterName
     * @param saveFolderName
     * @param maxPostSize
     * @param encoding
     * @return
     */
    public static String uploadImg(Controller ctrl, String parameterName, String saveFolderName, Integer maxPostSize, String encoding) {
        Map<String, String> imgDirectory = uploadImgs(ctrl, saveFolderName, maxPostSize, encoding);
        if (MapKit.isBlank(imgDirectory)) {
            return null;
        }
        return imgDirectory.get(parameterName);
    }
    
    /**
     * 一次上传多张图片 （若parameterName对应多张图片，则返回父级路径）
     * 
     * @param ctrl
     * @param saveFolderName
     * @param maxPostSize
     * @param encoding
     * @return (parameterName,directory)
     */
    public static Map<String, String> uploadImgs(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding) {
        Map<String, Map<String, ImageFile>> imgDirectory = uploadImgsAll(ctrl, saveFolderName, maxPostSize, encoding);
        if (MapKit.isBlank(imgDirectory)) {
            return null;
        }
        Map<String, String> imgPath = new HashMap<String, String>();
        Map<String, ImageFile> directorys = null;
        String virtualDirectory = null;
        for (String key : imgDirectory.keySet()) {
            directorys = imgDirectory.get(key);
            virtualDirectory = fileUploadKit.getVirtualDirectory(directorys);
            imgPath.put(key, virtualDirectory);
        }
        return imgPath;
    }
    
    /**
     * 一次上传多张图片
     * 
     * @return (parameterName,fileName-directory)
     */
    public static Map<String, Map<String, ImageFile>> uploadImgsAll(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding) {
        FileLoadKit fileUploadKit = FileLoadKit.getInstance();
        fileUploadKit.initProgress(ctrl);
        String imgDirectory = fileUploadKit.getImageDirectory();
        String serverDirectory = fileUploadKit.getServerDirectory(imgDirectory, saveFolderName, ctrl);
        String tempDirectory = fileUploadKit.getTempDirectory(serverDirectory);
        String discDirectory = fileUploadKit.getDiscDirectory(serverDirectory);
        
        List<UploadFile> uploadFiles = ctrl.getFiles(tempDirectory, maxPostSize, encoding);
        if (CollectionKit.isBlank(uploadFiles)) {
            return null;
        }
        Map<String, Map<String, ImageFile>> fileDirectory = new HashMap<String, Map<String, ImageFile>>();
        Map<String, ImageFile> filePaths = null, virtualPaths = null;
        File file = null;
        String parameterName = null;
        for (UploadFile uploadFile : uploadFiles) {
            file = uploadFile.getFile();
            boolean isImage = FileKit.isImage(uploadFile.getFileName().toLowerCase());
            if (!isImage) {
                FileKit.delete(file);
                continue;
            }
            virtualPaths = fileUploadKit.handleImg(file, discDirectory, serverDirectory);
            parameterName = uploadFile.getParameterName();
            
            filePaths = fileDirectory.get(parameterName);
            if (filePaths == null) {
                filePaths = new HashMap<String, ImageFile>();
                fileDirectory.put(parameterName, filePaths);
            }
            filePaths.putAll(virtualPaths);
        }
        return fileDirectory;
    }
    
    /**
     * 视频上传
     * 
     * @return 只返回视频虚拟路径
     * @throws InterruptedException
     * @throws IOException
     */
    public static String uploadVideo(Controller ctrl, String parameterName, String saveFolderName, Integer maxPostSize, String encoding) throws IOException, InterruptedException {
        Map<String, String> videoDirectory = uploadVideos(ctrl, saveFolderName, maxPostSize, encoding);
        if (MapKit.isBlank(videoDirectory)) {
            return null;
        }
        return videoDirectory.get(parameterName);
    }
    
    /**
     * 视频上传
     * 
     * @param ctrl
     * @param parameterName
     * @param saveFolderName
     * @param maxPostSize
     * @param encoding
     * @return (fileName,videoDirectory)返回视频虚拟路径、图片虚拟路径
     * @throws InterruptedException
     * @throws IOException
     */
    public static Map<String, VideoFile> uploadVideo(String parameterName, Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding) throws IOException, InterruptedException {
        Map<String, Map<String, VideoFile>> videoDirectory = uploadVideosAll(ctrl, saveFolderName, maxPostSize, encoding);
        if (MapKit.isBlank(videoDirectory)) {
            return null;
        }
        return videoDirectory.get(parameterName);
    }
    
    /**
     * 一次上传多个视频 （若parameterName对应多个视频，则返回父级虚拟路径，否则只返回视频路径）
     * 
     * @param ctrl
     * @param saveFolderName
     * @param maxPostSize
     * @param encoding
     * @return (parameterName,videoDirectory)
     * @throws InterruptedException
     * @throws IOException
     */
    public static Map<String, String> uploadVideos(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding) throws IOException, InterruptedException {
        Map<String, Map<String, VideoFile>> videoDirectory = uploadVideosAll(ctrl, saveFolderName, maxPostSize, encoding);
        if (MapKit.isBlank(videoDirectory)) {
            return null;
        }
        Map<String, String> videoPath = new HashMap<String, String>();
        Map<String, VideoFile> directorys = null;
        String virtualDirectory = null;
        for (String key : videoDirectory.keySet()) {
            directorys = videoDirectory.get(key);
            virtualDirectory = fileUploadKit.getVirtualDirectory(directorys);
            videoPath.put(key, virtualDirectory);
        }
        return videoPath;
    }
    
    /**
     * 一次上传多个视频
     * 
     * @return (parameterName,filename-directory(截图虚拟路径、视频虚拟路径))
     * @throws InterruptedException
     * @throws IOException
     */
    public static Map<String, Map<String, VideoFile>> uploadVideosAll(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding) throws IOException, InterruptedException {
        FileLoadKit fileUploadKit = FileLoadKit.getInstance();
        fileUploadKit.initProgress(ctrl);
        String videoDirectory = fileUploadKit.getVideoDirectory();
        String serverDirectory = fileUploadKit.getServerDirectory(videoDirectory, saveFolderName, ctrl);
        String tempDirectory = fileUploadKit.getTempDirectory(serverDirectory);
        String discDirectory = fileUploadKit.getDiscDirectory(serverDirectory);
        
        List<UploadFile> uploadFiles = ctrl.getFiles(tempDirectory, maxPostSize, encoding);
        if (CollectionKit.isBlank(uploadFiles)) {
            return null;
        }
        
        Map<String, Map<String, VideoFile>> fileDirectory = new HashMap<String, Map<String, VideoFile>>();
        Map<String, VideoFile> filePaths = null, virtualPaths = null;
        File file = null;
        String parameterName = null;
        for (UploadFile uploadFile : uploadFiles) {
            file = uploadFile.getFile();
            boolean isVideo = FileKit.isVideo(uploadFile.getFileName().toLowerCase());
            if (!isVideo) {
                FileKit.delete(file);
                continue;
            }
            virtualPaths = fileUploadKit.handleVideo(file, discDirectory);
            parameterName = uploadFile.getParameterName();
            
            filePaths = fileDirectory.get(parameterName);
            if (filePaths == null) {
                filePaths = new HashMap<String, VideoFile>();
                fileDirectory.put(parameterName, filePaths);
            }
            filePaths.putAll(virtualPaths);
        }
        return fileDirectory;
    }
    
    /**
     * 文件上传
     * 
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public static String uploadFile(Controller ctrl, String parameterName, String saveFolderName, Integer maxPostSize, String encoding) throws IOException, InterruptedException {
        Map<String, String> fileDirectory = uploadFiles(ctrl, saveFolderName, maxPostSize, encoding);
        if (MapKit.isBlank(fileDirectory)) {
            return null;
        }
        return fileDirectory.get(parameterName);
    }
    
    /**
     * 一次上传多个文件（多个文件类型的同时上传） （若parameterName对应多个文件，则返回父级路径）
     * 
     * @param ctrl
     * @param saveFolderName
     * @param maxPostSize
     * @param encoding
     * @return (parameterName,directory)
     * @throws InterruptedException
     * @throws IOException
     */
    public static Map<String, String> uploadFiles(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding) throws IOException, InterruptedException {
        Map<String, Map<String, FileInterface>> fileDirectory = uploadFilesAll(ctrl, saveFolderName, maxPostSize, encoding);
        if (MapKit.isBlank(fileDirectory)) {
            return null;
        }
        Map<String, String> filePath = new HashMap<String, String>();
        Map<String, FileInterface> fileInstance = null;
        String virtualDirectory = null;
        for (String key : fileDirectory.keySet()) {
            fileInstance = fileDirectory.get(key);
            virtualDirectory = fileUploadKit.getVirtualDirectory(fileInstance);
            filePath.put(key, virtualDirectory);
        }
        return filePath;
    }
    
    /**
     * 一次上传多个文件（多个文件类型的同时上传）
     * 
     * @return (parameterName,directory)
     * @throws InterruptedException
     * @throws IOException
     */
    public static Map<String, Map<String, FileInterface>> uploadFilesAll(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding) throws IOException, InterruptedException {
        FileLoadKit fileUploadKit = FileLoadKit.getInstance();
        fileUploadKit.initProgress(ctrl);
        String fileDirectory = fileUploadKit.getFileDirectory();
        String serverDirectory = fileUploadKit.getServerDirectory(fileDirectory, saveFolderName, ctrl);
        String tempDirectory = fileUploadKit.getTempDirectory(serverDirectory);
        
        List<UploadFile> uploadFiles = ctrl.getFiles(tempDirectory, maxPostSize, encoding);
        if (CollectionKit.isBlank(uploadFiles)) {
            return null;
        }
        
        Map<String, Map<String, FileInterface>> directorys = new HashMap<String, Map<String, FileInterface>>();
        Map<String, FileInterface> filePaths = null, virtualPaths = null;
        File file = null;
        String parameterName = null;
        for (UploadFile uploadFile : uploadFiles) {
            file = uploadFile.getFile();
            virtualPaths = fileUploadKit.handleFile(file, saveFolderName, ctrl);
            parameterName = uploadFile.getParameterName();
            
            filePaths = directorys.get(parameterName);
            if (filePaths == null) {
                filePaths = new HashMap<String, FileInterface>();
                directorys.put(parameterName, filePaths);
            }
            filePaths.putAll(virtualPaths);
        }
        return directorys;
    }
    
    /**
     * 处理临时图片
     * 
     * @return
     */
    private Map<String, ImageFile> handleImg(File tempFile, String discDirectory, String serverDirectory) {
        ImageFile imageFile = null;
        if (FileKit.isDevMode()) {
            imageFile = buildImageFile(tempFile);
        } else {
            File newFile = FileKit.fileCopy(discDirectory, tempFile);
            imageFile = buildImageFile(newFile);
        }
        imageFile.setOriginalDirectory(tempFile.getAbsolutePath());
        String virtualPath = FileKit.getVirtualDirectory(imageFile.getDescDirectory());
        imageFile.setVirtualDirectory(virtualPath);
        
        Map<String, ImageFile> imageFiles = new HashMap<String, ImageFile>();
        String fileName = imageFile.getFileName();
        imageFiles.put(fileName, imageFile);
        return imageFiles;
    }
    
    /**
     * 处理临时视频
     * 
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    private Map<String, VideoFile> handleVideo(File tempFile, String discDirectory) throws IOException, InterruptedException {
        VideoFile videoFile = buildVideoFile(tempFile, discDirectory);
        String fileName = videoFile.getFileName();
        Map<String, VideoFile> videoPaths = new HashMap<String, VideoFile>();
        videoPaths.put(fileName, videoFile);
        return videoPaths;
    }
    
    /**
     * 处理（非图片、视频）文件
     * 
     * @return
     */
    private Map<String, FileInterface> handleFile(File tempFile, String discDirectory, String serverDirectory) {
        FileInterface fileInstance = null;
        if (!FileKit.isDevMode()) {
            File newFile = FileKit.fileCopy(discDirectory, tempFile);
            fileInstance = buildFileInstance(newFile);
        }
        String virtualPath = FileKit.getVirtualDirectory(fileInstance.getDescDirectory());
        fileInstance.setVirtualDirectory(virtualPath);
        
        Map<String, FileInterface> virtualPaths = new HashMap<String, FileInterface>();
        String fileName = fileInstance.getFileName();
        virtualPaths.put(fileName, fileInstance);
        return virtualPaths;
    }
    
    /**
     * 处理临时文件
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    private Map<String, FileInterface> handleFile(File tempFile, String saveFolderName, Controller ctrl) throws IOException, InterruptedException {
        Map<String, FileInterface> virtualPaths = new HashMap<String, FileInterface>();
        String fileName = tempFile.getName();
        String serverDirectory = getServerDirectoryAuto(fileName, saveFolderName, ctrl);
        if (FileKit.isDevMode()) {
            String fileDirectory = getFileDirectory();
            serverDirectory = getServerDirectory(fileDirectory, saveFolderName, ctrl);
        }
        String discDirectory = getDiscDirectory(serverDirectory);
        if (FileKit.isImage(fileName)) {
            Map<String, ImageFile> imagePath = handleImg(tempFile, discDirectory, serverDirectory);
            virtualPaths.putAll(imagePath);
        } else if (FileKit.isVideo(fileName)) {
            Map<String, VideoFile> videoPaths = handleVideo(tempFile, discDirectory);
            virtualPaths.putAll(videoPaths);
        } else {
            Map<String, FileInterface> filePath = handleFile(tempFile, discDirectory, serverDirectory);
            virtualPaths.putAll(filePath);
        }
        return virtualPaths;
    }
    
    /**
     * 初始化文件信息
     * 
     * @return
     */
    private FileInterface buildFileInstance(File file) {
        String fileName = file.getName();
        FileInterface fileInstance = new FileInstance();
        fileInstance.setFileName(fileName);
        fileInstance.setFileType(FileKit.getFileType(fileName));
        fileInstance.setDescDirectory(file.getAbsolutePath());
        return fileInstance;
    }
    
    /**
     * 初始化图片信息
     * 
     * @return
     */
    private ImageFile buildImageFile(File file) {
        String fileName = file.getName();
        ImageFile imageFile = new ImageFile();
        imageFile.setFileName(fileName);
        imageFile.setFileType(FileKit.getFileType(fileName));
        imageFile.setDescDirectory(file.getAbsolutePath());
        return imageFile;
    }
    
    /**
     * 初始化视频信息
     * 
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    private VideoFile buildVideoFile(File file, String discDirectory) throws IOException, InterruptedException {
        return FileHandleKit.handleLoadVideo(file, discDirectory);
    }
    
    /**
     * 获取虚拟上传的路径--(若存在多张，则返回父级路径)
     * 
     * @param <T>
     * 
     * @return
     */
    private <T> String getVirtualDirectory(Map<String, T> directorys) {
        if (MapKit.isBlank(directorys)) {
            return null;
        }
        
        int videos = 0;
        FileInterface file = null;
        String directory = null;
        String videoDirectory = null;
        for (String fileName : directorys.keySet()) {
            file = (FileInterface)directorys.get(fileName);
            directory = file.getVirtualDirectory();
            if (FileKit.isVideo(fileName)) {
                if (videos > 0) {
                    return directory.substring(0, directory.lastIndexOf("/"));
                }
                videoDirectory = directory;
                videos = videos + 1;
            }
        }
        if (directorys.size() == 1) {
            return directory;
        } else if (StrKit.notBlank(videoDirectory)) {
            return videoDirectory;
        } else {
            return directory.substring(0, directory.lastIndexOf("/"));
        }
    }
    
    /**
     * 获取临时上传路径
     */
    private String getTempDirectory(String directory) {
        // 开发模式下，本地服务器作为临时根目录
        if (!FileKit.isDevMode()) {
            String baseDirectory = getBackupsDirectory();
            directory = FileKit.getDirectory(baseDirectory, directory);
        }
        return directory;
    }
    
    /**
     * 获取物理上传路径
     */
    private String getDiscDirectory(String directory) {
        String baseDirectory = getBaseDirectory();
        directory = FileKit.getDirectory(baseDirectory, directory);
        return directory;
    }
    
    /**
     * 动态获取服务器存储路径
     */
    private String getServerDirectoryAuto(String fileName, String saveFolderName, Controller ctrl) {
        String directory = null;
        if (FileKit.isImage(fileName)) {
            directory = getImageDirectory();
        } else if (FileKit.isVideo(fileName)) {
            directory = getVideoDirectory();
        } else {
            directory = getFileDirectory();
        }
        String serverDirectory = getServerDirectory(directory, saveFolderName, ctrl);
        return serverDirectory;
    }
    
    /**
     * 获取服务器存储路径
     * 
     * @return
     */
    private String getServerDirectory(String directory, String saveFolderName, Controller ctrl) {
        User user = ctrl.getSessionAttr("user");
        String userName = user.getStr("username");
        directory = directory.replaceAll("<username>", userName);
        directory = FileKit.getDirectory(directory, saveFolderName);
        return directory;
    }
    
    /**
     * 初始化进度条
     */
    private void initProgress(Controller ctrl) {
        final int totalsize = ctrl.getRequest().getContentLength();
        FilePart.progresss.set(new FilePart.Progress() {
            public void progress(long currentSize) {
                double csize = (double)currentSize;
                double s = csize / totalsize * 100;
                // logger.info("文件上传百分之{}", String.format("%.2f", s));
            }
            
            public void start() {
                logger.info("文件上传开始~");
            }
            
            public void end() {
                logger.info("文件上传结束~");
                FilePart.progresss.remove();
            }
        });
    }
    
    private void setLogger(Logger logger) {
        this.logger = logger;
    }
    
    private String getImageDirectory() {
        return imageDirectory;
    }
    
    private void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }
    
    private String getVideoDirectory() {
        return videoDirectory;
    }
    
    private void setVideoDirectory(String videoDirectory) {
        this.videoDirectory = videoDirectory;
    }
    
    private String getBaseDirectory() {
        return baseDirectory;
    }
    
    private void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
    
    private String getFileDirectory() {
        return fileDirectory;
    }
    
    private void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }
    
    private String getBackupsDirectory() {
        return backupsDirectory;
    }
    
    private void setBackupsDirectory(String backupsDirectory) {
        this.backupsDirectory = backupsDirectory;
    }
    
}
