package ${groupId}.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Descripton：controller index
 * Author: tanggang
 * Date: 2018/11/9
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class BaseApi {
    @RequestMapping("/index")
    public String index(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return "/index";
    }

    @RequestMapping("/socket/demo")
    public String demo(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model) {
        model.addAttribute("orderNo", System.currentTimeMillis() + "");
        return "/demo";
    }

    @RequestMapping("/socket/demo1")
    public String demo1(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        return "/demo1";
    }

    @RequestMapping("/web/pay")
    public String pay(HttpServletRequest request, HttpServletResponse httpServletResponse, Model model) {
        String orderNo = request.getParameter("orderNo");
        String payAmt = request.getParameter("payAmt");
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("payAmt", payAmt);
        model.addAttribute("payRes", "提交支付成功！等待渠道支付结果消息!");
        return "/pay";
    }
}
