package com.yiyi.tang.projectgen.service.impl;

import com.yiyi.tang.projectgen.model.FileType;
import com.yiyi.tang.projectgen.service.AbstractFileService;
import org.springframework.stereotype.Service;

/**
 * @author tangmingjian 2018-12-01 下午3:52
 **/
@Service
public class OtherFileService extends AbstractFileService {
    @Override
    public FileType fileType() {
        return FileType.OTHER_FILE;
    }
}
