package com.yiyi.tang.projectgen.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author tangmingjian 2018-11-28 下午9:31
 **/

@ConfigurationProperties(prefix = "gen")
@Configuration
@Data
public class GenConfig {
    /**
     * 生成项目的目录
     */
    private String dirPath;
    /**
     * 模版文件的目录
     */
    private String templatesPath;

}
