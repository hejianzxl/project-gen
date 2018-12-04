package ${groupId}

import ${groupId}.utils.RedisUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.Resource

@Component
class Scheduler {
    private val log = LoggerFactory.getLogger(javaClass)
    private val cnt = AtomicLong(0)
    private val template = RestTemplate()

    @Resource
    private lateinit var redis: RedisUtil

    @Value("\${server.port:80}")
    var port: Int = 80

    @Scheduled(cron = "0/5 * * * * ?")
    fun schedule() {
        log.info("scheduling...")
        val resp = template.getForObject("http://localhost:$port/proxy/${cnt.get()}", String::class.java)
        log.info("this is a test message. seq is [{}]. \n\t -- and response is [{}]. ", cnt.getAndIncrement(), resp)
    }
}
