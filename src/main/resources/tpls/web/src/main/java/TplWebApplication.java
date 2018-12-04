package ${groupId};
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author luiz
 * @Title: TplWebApplication
 * @ProjectName ${artifactId}-springboot
 * @Description: TODO
 * @date 2018/11/13 15:51
 */
@SpringBootApplication
@ComponentScan(basePackages = "${groupId}")
public class TplWebApplication {
    public static void main(String[] args) {
        SpringApplication.run($.TplWebApplication.class);
    }
}
