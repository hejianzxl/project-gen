package com.gpay.base.projectgen.service.impl;

import com.gpay.base.projectgen.model.FileType;
import com.gpay.base.projectgen.model.MyFile;
import com.gpay.base.projectgen.service.AbstractFileService;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author tangmingjian 2018-11-30 下午9:51
 **/
@Service
public class JavaFileService extends AbstractFileService {

    @Override
    public FileType fileType() {
        return FileType.JAVA_FILE;
    }

    @Override
    public String targetDirPath(MyFile myFile, String targetContent) {
        String fileName = myFile.getFile().getName();
        File targetFile = new File(myFile.getTargetFilePath());
        File parentDir = targetFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        String[] lines = targetContent.split(System.getProperty("line.separator", "\n"));
        if (lines.length > 0) {
            String pkgPath = lines[0]
                    .replace("package", "")
                    .replace(";", "")
                    .trim()
                    .replace(".", File.separator);
            return parentDir.getAbsolutePath() + File.separator + pkgPath + File.separator + fileName;
        }
        return myFile.getTargetFilePath();
    }
}
