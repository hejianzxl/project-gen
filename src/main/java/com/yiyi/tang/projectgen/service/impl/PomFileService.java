package com.yiyi.tang.projectgen.service.impl;

import com.yiyi.tang.projectgen.model.FileType;
import com.yiyi.tang.projectgen.model.MyFile;
import com.yiyi.tang.projectgen.service.AbstractFileService;
import com.yiyi.tang.projectgen.service.ParamsHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author tangmingjian 2018-12-01 下午3:52
 **/
@Service
public class PomFileService extends AbstractFileService {

    @Override
    protected String specialDealWithFileContent(MyFile myFile, String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        String targetContent = content;
        if (myFile.isModuleDir()) {
            targetContent = removeModulesNotSelectedInRootPom(content);
        }
        return targetContent;
    }

    private String removeModulesNotSelectedInRootPom(String content) {
        return Arrays.stream(content.split(System.getProperty("line.separator", "\n")))
                .filter(o -> !needRemove(o))
                .reduce((l1, l2) -> l1 + System.getProperty("line.separator", "\n") + l2)
                .get();
    }


    private boolean needRemove(String line) {
        List<String> allModules = ParamsHelper.modules();
        List<String> modulesSelected = ParamsHelper.modulesSelected();
        allModules.removeAll(modulesSelected);
        return allModules.stream()
                .map(o -> "<module>" + ParamsHelper.artifactId() + "-" + o + "</module>")
                .anyMatch(o -> line.indexOf(o) > 0);

    }

    @Override
    public FileType fileType() {
        return FileType.POM_FILE;
    }
}
