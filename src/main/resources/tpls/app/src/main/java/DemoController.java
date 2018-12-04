package ${groupId}.controller;

import com.alibaba.fastjson.JSON;
import ${groupId}.dao.mapper.DemoMapper;
import ${groupId}.dao.model.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luiz
 * @Title: DemoController
 * @ProjectName ${artifactId}-springboot
 * @Description: TODO
 * @date 2018/11/22 16:33
 */
@RestController
@RequestMapping("/app/demo")
public class DemoController {
    @Autowired
    DemoMapper demoMapper;

    @RequestMapping("/findDemo/{id}")
    public String findDemo(@PathVariable(name ="id")long id){
        Demo demo=demoMapper.selectByPrimaryKey(id);
        if(demo==null) return "未找到";
        return JSON.toJSONString(demo);
    }
}
