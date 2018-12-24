package com.yiyi.tang.projectgen.service;

import com.yiyi.tang.projectgen.model.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author tangmingjian 2018-12-01 下午3:07
 **/
@Component
@Slf4j
public class FileServiceFactory {
    private HashMap<FileType, AbstractFileService> fileServices = new HashMap<>();

    public AbstractFileService getService(FileType fileType) {
        return fileServices.get(fileType);
    }

    public void registerService(AbstractFileService service) {
        AbstractFileService fileService = fileServices.putIfAbsent(service.fileType(), service);
        if (fileService != null) {
            log.error("file service already existed");
            throw new RuntimeException("duplicated file service:" + service.fileType());
        }
        log.info("register {} success ", service.fileType());
    }
}
