package com.gpay.base.projectgen.service;

import com.gpay.base.projectgen.model.FileType;
import com.gpay.base.projectgen.model.MyFile;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        if (myFile.getFileType()==FileType.POM_FILE && myFile.isModuleDir()) {
            targetContent = removeModulesNotSelectedInRootPom(targetContent);
        }

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


    private String removeModulesNotSelectedInRootPom(String content){
        return Arrays.stream(content.split(System.getProperty("line.separator", "\n")))
                .filter(o->!needRemove(o))
                .reduce((l1,l2)->l1+System.getProperty("line.separator", "\n")+l2)
                .get();
    }


    private boolean needRemove(String line){
        List<String> allModules = ParamsHelper.modules();
        List<String> modulesSelected = ParamsHelper.modulesSelected();
        allModules.removeAll(modulesSelected);
        return allModules.stream()
                .map(o -> "<module>" + ParamsHelper.artifactId() + "-" + o+"</module>")
                .anyMatch(o->line.indexOf(o)>0);

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
