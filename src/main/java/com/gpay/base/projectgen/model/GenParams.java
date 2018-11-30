package com.gpay.base.projectgen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tangmingjian 2018-11-28 下午5:26
 **/
@Data
@ApiModel("GenParams")
public class GenParams {
    /**
     * maven项目groupId
     */
    @ApiModelProperty(value = "groupId", required = true)
    private String groupId;
    /**
     * maven项目artifactId
     */
    @ApiModelProperty(value = "artifactId", required = true)
    private String artifactId;

    /**
     * maven项目模块
     */

    @ApiModelProperty(value = "modules", required = true)
    private List<String> modules;

}
