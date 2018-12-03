package com.gpay.base.projectgen.service;

import com.gpay.base.projectgen.model.FileType;
import com.gpay.base.projectgen.model.MyFile;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author tangmingjian 2018-11-30 下午9:49
 **/
@Slf4j
public abstract class AbstractFileService {

    public final void dealFile(MyFile myFile) throws Exception {
        String targetContent;
        if (myFile.isEmpty()) {
            targetContent = "";
            log.info("empty file, need't read source file");
        } else {
            String sourceContent = readFileContent(myFile.getPath());
            targetContent = replaceMacro(sourceContent);
        }

        targetContent = specialDealWithFileContent(myFile, targetContent);

        writeFile(targetDirPath(myFile, targetContent), targetContent);
    }


    private String readFileContent(String filePath) {
        File file = new File(filePath);
        Long fileLen = file.length();
        byte[] fileContent = new byte[fileLen.intValue()];
        try (FileInputStream in = new FileInputStream(file)) {
            in.read(fileContent);
            return new String(fileContent, "utf-8");
        } catch (Exception e) {
            log.info("read file content error", e);
        }
        return null;
    }

    private String replaceMacro(String sourceContent) {
        return sourceContent.replace("${groupId}", ParamsHelper.groupId())
                .replace("${artifactId}", ParamsHelper.artifactId());

    }

    protected String targetDirPath(MyFile myFile, String targetContent) {
        return myFile.getTargetFilePath();
    }


    protected String specialDealWithFileContent(MyFile myFile, String content) {
        return content;
    }

    private void writeFile(String targetDir, String content) throws IOException {
        File file = new File(targetDir + File.separator);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        boolean create = file.createNewFile();
        log.info("create file:{} result:{}", file, create);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
            fileWriter.flush();
        } catch (Exception e) {
            log.info("write content to file error", e);
        }
    }

    public abstract FileType fileType();

}
