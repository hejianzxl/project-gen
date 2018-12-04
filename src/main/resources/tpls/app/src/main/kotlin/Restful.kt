package ${groupId}

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class Restful {
    private val template = RestTemplate()

    @Value("\${server.port:80}")
    var port: Int = 80

    @GetMapping(path = ["/proxy/{i}"])
    @ResponseBody
    fun proxy(@PathVariable(name = "i") i: Long): String {
        return template.getForObject("http://localhost:$port/$i", String::class.java) ?: "null response"
    }

    @GetMapping(path = ["/{i}"])
    @ResponseBody
    fun get(@PathVariable(name = "i") i: Long): String {
        return "this is response for $i th request."
    }
}
