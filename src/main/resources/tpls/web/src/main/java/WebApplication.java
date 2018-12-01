package ${groupId}.web;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tangmingjian 2018-11-28 下午5:13
 **/
@SpringBootApplication
@ComponentScan(basePackages = "${groupId}")
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }
}
