package com.gpay.base.projectgen.controller;

import com.gpay.base.projectgen.config.GenConfig;
import com.gpay.base.projectgen.model.GenParams;
import com.gpay.base.projectgen.util.ZipUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tangmingjian 2018-11-28 下午5:20
 **/
@Controller
@RequestMapping(value = "project")
@Api("project tool")
@Slf4j
public class GenController {
    @Autowired
    private GenConfig genConfig;

    private final ThreadLocal<String> ARTIFACT_ID = new ThreadLocal<>();
    private final ThreadLocal<String> GROUP_ID = new ThreadLocal<>();


    @GetMapping
    @ApiOperation(value = "gen a project init")
    public String preGen(Model model) {
        model.addAttribute("modules", modules());
        return "gen/init";
    }

    private List<String> modules() {
        URL tplUrl = this.getClass().getResource("/tpls");
        log.info("template url:{}",tplUrl);
        File file = new File(tplUrl.getFile());
        if (!file.exists()) {
            log.info("template url:{} not exist");
            return null;
        }
        return Arrays.stream(file.listFiles())
                .filter(o -> o.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ApiOperation(value = "gen a project", produces = "application/octet-stream")
    public void genProject(@ModelAttribute GenParams params, HttpServletResponse response) {
        try {
            log.info("gen project params:{}", params);
            //check params
            checkParams(params);

            //set params
            setParams(params);

            //gen project
            genProject();

            //zip project
            String filePath = ZipUtils.zip(targetDir());

            //download project
            writeFileIntoResponse(filePath, response);

            log.info("gen project finished");
        } catch (Exception e) {
            log.error("gen project error", e);
        }
    }

    private void setParams(GenParams params) {
        ARTIFACT_ID.set(params.getArtifactId());
        GROUP_ID.set(params.getGroupId());
    }

    private void checkParams(GenParams params) throws Exception {
        if (StringUtils.isEmpty(params.getArtifactId()) || StringUtils.isEmpty(params.getGroupId())) {
            throw new IllegalArgumentException("params error");
        }
    }

    private void genProject() throws Exception {
        URL tplUrl = this.getClass().getResource("/tpls");
        String srcDir = tplUrl.getFile();
        File srcFile = new File(srcDir);
        if (!srcFile.exists()) {
            log.error("tpl files not existed");
            return;
        }
        String targetDir = targetDir();
        File targetFile = new File(targetDir);
        if (!targetFile.exists()) {
            log.info("make target dir");
            targetFile.mkdirs();
        }

        makeDirectoryAndFileRecursive(srcDir, targetDir);
    }

    private void makeDirectoryAndFileRecursive(final String srcDir, final String targetDir) throws Exception {
        File srcFile = new File(srcDir);
        File[] srcSubFiles = srcFile.listFiles();
        for (File f : srcSubFiles) {
            if (f.isDirectory()) {
                log.info("deal dir:{}", f);
                final String dirName = f.getName();
                String newDirName = dirName;
                if (modules().stream().anyMatch(o -> dirName.equals(o))) {
                    newDirName = ARTIFACT_ID.get() + "-" + dirName;
                }
                File targetDirFile = new File(targetDir + File.separator + newDirName);
                if (!targetDirFile.exists()) {
                    log.info("create target dir:{}", targetDirFile);
                    targetDirFile.mkdirs();
                }
                makeDirectoryAndFileRecursive(srcDir + File.separator + dirName, targetDirFile.getAbsolutePath());
            } else {
                log.info("deal file:{}", f.getAbsolutePath());
                String fileName = f.getName();
                String sourceContent = readContentFromSourceFile(f.getAbsolutePath());
                String targetContent = sourceContent.replace("${groupId}", GROUP_ID.get())
                        .replace("${artifactId}", ARTIFACT_ID.get());

                String eachTargetDir = targetDir;
                if (fileName.endsWith(".java")) {
                    String[] lines = targetContent.split(System.getProperty("line.separator", "\n"));
                    if (!ArrayUtils.isEmpty(lines)) {
                        String pkgPath = lines[0]
                                .replace("package", "")
                                .replace(";", "")
                                .trim()
                                .replace(".", File.separator);
                        //同级目录下有多个文件时要避免创建错误文件
                        if (!targetDir.endsWith(pkgPath)) {
                            eachTargetDir = targetDir + File.separator + pkgPath;
                        }
                    }
                }
                writeFile(fileName, eachTargetDir, targetContent);
            }
        }
    }

    private void writeFile(String fileName, String targetDir, String content) throws IOException {
        File file = new File(targetDir + File.separator + fileName);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (file.exists()) {
            log.info("delete existed file:{}", file);
            file.delete();
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

    private String readContentFromSourceFile(String filePath) {
        File file = new File(filePath);
        Long fileLen = file.length();
        byte[] fileContent = new byte[fileLen.intValue()];
        try (FileInputStream in = new FileInputStream(file)){
            in.read(fileContent);
            return new String(fileContent, "utf-8");
        } catch (Exception e) {
            log.info("read file content error", e);
        }
        return null;
    }

    private String targetDir() {
        String dirPath = genConfig.getDirPath();
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        dirPath = dirPath + ARTIFACT_ID.get();
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    private void writeFileIntoResponse(String filePath, HttpServletResponse response) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            log.info("file:{} not exist", filePath);
            return;
        }
        String filename = file.getName();
        try (InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
             OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "utf-8"));
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            out.write(buffer);
            out.flush();
        } catch (Exception e) {
            log.info("download file error", e);
        }
    }
}
