package org.oj.server.service;

import org.oj.server.config.OJConfig;
import org.oj.server.enums.FilePathEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.util.LanguageUtil;
import org.oj.server.util.UnPackUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:20
 */
@Service
public class UploadService {
    private final OJConfig ojConfig;

    public UploadService(OJConfig ojConfig) {
        this.ojConfig = ojConfig;
    }

    public String uploadImage(MultipartFile file) {
        return upload(file, FilePathEnum.IMAGE);
    }

    public String uploadVideo(MultipartFile file) {
        return upload(file, FilePathEnum.VIDEO);
    }

    public String uploadAudio(MultipartFile file) {
        return upload(file, FilePathEnum.AUDIO);
    }

    public String uploadRecord(MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        // 文件类型后缀
        String type = originFileName.substring(originFileName.lastIndexOf('.'));

        // 解压路径  本地路径/数据文件夹/系统当前时间/
        String path = ojConfig.getBase() + FilePathEnum.RECORD.getPath() + System.currentTimeMillis() + '/';
        File dest = new File(path + originFileName);
        try {
            if (!dest.getParentFile().mkdirs()) {
                throw new ErrorException(StatusCodeEnum.MKDIR_ERROR);
            }
            file.transferTo(dest);
            if (type.equalsIgnoreCase(".zip")) {
                UnPackUtil.unPackZip(dest, null, path);
            } else if (type.equalsIgnoreCase(".rar")) {
                UnPackUtil.unPackRar(dest, path);
            } else {
                throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorException(StatusCodeEnum.FAIL);
        } finally {
            dest.delete();
        }

        return ojConfig.getUrlBase() + FilePathEnum.RECORD.getPath() + System.currentTimeMillis() + '/';
    }

    public String upload(MultipartFile file, FilePathEnum filePath) {
        if (file.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        String originFileName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + originFileName.substring(originFileName.lastIndexOf('.'));

        File dest = new File(ojConfig.getBase() + filePath.getPath() + fileName);

        if (!dest.getAbsoluteFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new ErrorException(StatusCodeEnum.FAIL);
        }

        return ojConfig.getUrlBase() + filePath.getPath() + fileName;
    }

    public String upload(String code, Integer language) {
        // 解压路径  本地路径/数据文件夹/系统当前时间/
        String path = ojConfig.getBase() + FilePathEnum.JUDGE.getPath() + System.currentTimeMillis() + LanguageUtil.fileSuffix(language);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            out.write(code);
            out.close();
        } catch (IOException e) {
            throw new ErrorException(StatusCodeEnum.WRITE_ERROR);
        }

        return path;
    }

    public List<String> list(Integer pathEnum) {
        String path = ojConfig.getBase() + FilePathEnum.valueOf(pathEnum).getPath();
        return travel(path);
    }

    public Object list(String recordPath) {
        String path = ojConfig.getBase() + FilePathEnum.RECORD.getPath() + recordPath;
        if (!path.endsWith("/")) {
            path += "/";
        }
        return travel(path);
    }

    private List<String> travel(String path) {
        List<String> res = new ArrayList<>();
        try {
            Files.walkFileTree(Path.of(path), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    File file = path.toFile();
                    if (!file.isDirectory()) {
                        // 添加路径， 并替换base
                        res.add(file.getAbsolutePath().replace(ojConfig.getBase(), ojConfig.getUrlBase()));
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
                    return FileVisitResult.SKIP_SUBTREE;
                }
            });
        } catch (IOException e) {
            throw new ErrorException(StatusCodeEnum.SYSTEM_ERROR);
        }
        return res;
    }
}
