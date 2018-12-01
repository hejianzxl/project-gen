package com.gpay.base.projectgen.controller;

import com.alibaba.fastjson.JSON;
import com.gpay.base.projectgen.config.GenConfig;
import com.gpay.base.projectgen.model.FileType;
import com.gpay.base.projectgen.model.GenParams;
import com.gpay.base.projectgen.model.MyFile;
import com.gpay.base.projectgen.service.AbstractFileService;
import com.gpay.base.projectgen.service.FileServiceFactory;
import com.gpay.base.projectgen.service.ParamsHelper;
import com.gpay.base.projectgen.util.ZipUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
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
    @Autowired
    private FileServiceFactory fileServiceFactory;

    @GetMapping
    @ApiOperation(value = "gen a project init")
    public String preGen(Model model) {
        model.addAttribute("modules", modules());
        return "gen/init";
    }

    private List<String> modules() {
        URL tplUrl = this.getClass().getResource("/tpls");
        log.info("template url:{}", tplUrl);
        File file = new File(tplUrl.getFile());
        if (!file.exists()) {
            log.info("template url:{} not exist");
            return null;
        }
        return Arrays.stream(file.listFiles())
                .filter(File::isDirectory)
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

            List<MyFile> myFiles = collectFiles();

            makeDirectoryOrFile(myFiles);
            //zip project
            String filePath = ZipUtils.zip(targetDir());

            //download project
            writeFileIntoResponse(filePath, response);

            log.info("gen project finished");
        } catch (Exception e) {
            log.error("gen project error", e);
        }
    }

    /**
     * 校验用户填写参数
     *
     * @param params 参数
     * @throws Exception
     */
    private void checkParams(GenParams params) throws Exception {
        if (StringUtils.isEmpty(params.getArtifactId()) || StringUtils.isEmpty(params.getGroupId())) {
            throw new IllegalArgumentException("params error");
        }
    }

    /**
     * 设置ArtifactId GroupId ModulesSelected到当前线程中
     *
     * @param params 用户填写参数
     */
    private void setParams(GenParams params) {
        ParamsHelper.artifactId(params.getArtifactId());
        ParamsHelper.groupId(params.getGroupId());
        List<String> allModules = modules();
        ParamsHelper.modules(allModules);
        if (CollectionUtils.isEmpty(params.getModules())) {
            //default generate all modules
            params.setModules(allModules);
        }
        ParamsHelper.modulesSelected(params.getModules());
    }


    /**
     * 获取所有待生成的文件或目录
     *
     * @return allFiles to make
     */
    private List<MyFile> collectFiles() {
        URL tmpUrl = this.getClass().getResource("/tpls");//todo 解决jar包读取不到的问题
        if (tmpUrl == null) {
            log.info("can't find templates dir");
            throw new RuntimeException("can't find templates dir");
        }
        File tmpDir = new File(tmpUrl.getFile());
        List<MyFile> allFiles = new ArrayList();
        List<MyFile> rootFiles = Arrays.stream(tmpDir.listFiles())
                .filter(this::selected)
                .map(this::mavenModuleFile)
                .collect(Collectors.toList());
        allFiles.addAll(rootFiles);

        rootFiles.stream().forEach(o -> addFileRecursive(o, allFiles));

        log.info("all files:{}", JSON.toJSONString(allFiles));
        return allFiles;
    }

    private boolean selected(File file) {
        return ParamsHelper.modulesSelected().contains(file.getName()) || file.isFile();
    }

    /**
     * 将maven模块文件构造成自定义文件
     *
     * @param file file
     * @return myfile
     */
    private MyFile mavenModuleFile(File file) {
        String targetFilePath;
        String fileName = file.getName();
        String targetDir = targetDir();
        if (file.isDirectory()) {
            targetFilePath = targetDir + File.separator + ParamsHelper.artifactId() + "-" + fileName;
        } else {
            targetFilePath = targetDir + File.separator + file.getName();
        }
        return buildMyFile(file, true, targetFilePath);
    }

    /**
     * 将非maven模块文件构造成自定义文件
     *
     * @param file           file
     * @param parentFilePath parent file path
     * @return myfile
     */
    private MyFile nonMavenModuleFile(File file, String parentFilePath) {
        String targetFilePath = parentFilePath + File.separator + file.getName();
        return buildMyFile(file, false, targetFilePath);
    }

    private MyFile buildMyFile(File file, boolean isModuleDir, String targetFilePath) {
        return MyFile.builder().file(file).isModuleDir(isModuleDir).targetFilePath(targetFilePath).build();
    }

    /**
     * 递归添加所有子文件
     *
     * @param file     file
     * @param allFiles collections
     */
    private void addFileRecursive(MyFile file, List<MyFile> allFiles) {
        if (FileType.DIRECTORY == file.getFileType()) {
            Arrays.stream(file.getFile().listFiles())
                    .map(o -> nonMavenModuleFile(o, file.getTargetFilePath()))
                    .forEach(o -> addFileRecursive(o, allFiles));
        } else {
            allFiles.add(file);
        }
    }

    /**
     * 创建所有文件
     *
     * @param files allFiles
     */
    private void makeDirectoryOrFile(List<MyFile> files) {
        files.stream()
                .sorted((o1, o2) -> Boolean.compare(o1.isModuleDir(), o2.isModuleDir()))
                .forEach(this::makeDirectoryOrFile);
    }

    private void makeDirectoryOrFile(MyFile file) {
        if (file.getFile().isDirectory()) {
            File targetDir = new File(file.getTargetFilePath());
            if (!targetDir.exists()) {
                boolean result = targetDir.mkdirs();
                log.info("create dir:{} result:{}", file.getPath(), result);
            } else {
                log.info("dir:{} already existed", file.getPath());
            }
        } else {
            AbstractFileService fileService = fileServiceFactory.getService(file.getFileType());
            try {
                fileService.dealFile(file);
            } catch (Exception e) {
                log.error("deal file error", e);
            }
        }
    }

    private String targetDir() {
        String dirPath = genConfig.getDirPath();
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        dirPath = dirPath + ParamsHelper.artifactId();
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
