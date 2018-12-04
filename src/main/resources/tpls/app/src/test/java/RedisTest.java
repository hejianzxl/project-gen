package ${groupId}.test;

import ${groupId}.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author luiz
 * @Title: RedisTest
 * @ProjectName ${artifactId}-springboot
 * @Description: TODO
 * @date 2018/11/22 16:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    RedisUtil redisUtil;

    @Test
    public void testSet(){
        redisUtil.set("aaa","test");
        System.err.println(redisUtil.get("aaa"));
    }
}
