package com.gpay.base.projectgen.service.impl;

import com.gpay.base.projectgen.model.FileType;
import com.gpay.base.projectgen.service.AbstractFileService;
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
