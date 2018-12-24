package com.yiyi.tang.projectgen.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.Objects;

/**
 * @author tangmingjian 2018-11-30 下午9:29
 **/
@Data
@AllArgsConstructor
public class MyFile {
    /**
     * file
     */
    private File file;
    /**
     * 是否是空目录／空文件
     */
    private boolean empty;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 是否是maven模块目录
     */
    private boolean isModuleDir;

    /**
     * 文件类型{@link FileType}
     */
    private FileType fileType;

    /**
     * 生成项目文件路径
     */
    private String targetFilePath;


    public static MyFile.MyFileBuilder builder() {
        return new MyFile.MyFileBuilder();
    }

    public static class MyFileBuilder {
        private File file;
        private boolean empty;
        private String path;
        private boolean isModuleDir;
        private FileType fileType;
        private String targetFilePath;

        MyFileBuilder() {
        }

        public MyFile.MyFileBuilder file(File file) {
            Objects.requireNonNull(file);
            this.file = file;
            this.path = file.getAbsolutePath();
            if (file.isDirectory()) {
                this.empty = file.listFiles().length == 0;
            } else {
                this.empty = file.length() == 0;
            }
            this.fileType = FileType.getFileType(file);
            return this;
        }


        public MyFile.MyFileBuilder isModuleDir(boolean isModuleDir) {
            this.isModuleDir = isModuleDir;
            return this;
        }

        public MyFile.MyFileBuilder targetFilePath(String targetFilePath) {
            this.targetFilePath = targetFilePath;
            return this;
        }

        public MyFile build() {
            return new MyFile(this.file, this.empty, this.path, this.isModuleDir, this.fileType, this.targetFilePath);
        }

    }
}
