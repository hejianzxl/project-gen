package com.yiyi.tang.projectgen.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;

/**
 * @author tangmingjian 2018-11-28 下午11:17
 **/
public class ZipUtils {

    /**
     * 将sourcePath目录打成zip包
     *
     * @param srcPath 目录
     * @return zip包路径
     */
    public static String zip(String srcPath) {
        File srcDir = new File(srcPath);
        if (!srcDir.exists())
            throw new RuntimeException(srcPath + "不存在！");
        String fileName = srcDir.getName();
        File parentDir = srcDir.getParentFile();
        String absoluteDir = parentDir.getAbsolutePath();
        String desPath = absoluteDir + "/" + fileName + ".zip";
        File zipFile = new File(desPath);

        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(srcDir);
        zip.addFileset(fileSet);
        zip.execute();
        return desPath;
    }
}
