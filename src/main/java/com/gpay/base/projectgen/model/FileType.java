package com.gpay.base.projectgen.model;

import lombok.Getter;

import java.io.File;
import java.util.Arrays;

/**
 * @author tangmingjian 2018-11-30 下午9:35
 **/
@Getter
public enum FileType {
    DIRECTORY("directory", "Directory"),
    JAVA_FILE(".java", "Java文件"),
    POM_FILE(".xml", "Xml文件"),
    OTHER_FILE("other", "其他文件");

    private String type;
    private String desc;

    FileType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static FileType getFileType(final File file) {
        if (file.isDirectory()) {
            return DIRECTORY;
        }
        return Arrays.stream(FileType.values())
                .filter(o -> file.getName().endsWith(o.getType()))
                .findAny()
                .orElse(FileType.OTHER_FILE);
    }

    public static FileType getFileTypeByPath(final String path) {
        boolean isFile = path.matches(".*\\.java");
        if (!isFile) {
            return DIRECTORY;
        }
        return Arrays.stream(FileType.values())
                .filter(o -> o == DIRECTORY)
                .filter(o -> path.endsWith(o.getType()))
                .findAny()
                .orElse(FileType.OTHER_FILE);
    }
}
