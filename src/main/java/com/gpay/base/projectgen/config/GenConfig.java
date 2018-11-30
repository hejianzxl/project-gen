package com.gpay.base.projectgen.config;

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
    private String dirPath;

}
