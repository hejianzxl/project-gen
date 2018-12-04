package ${groupId}.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author luiz
 * @Title: CrosIntercept
 * @ProjectName ${artifactId}-springboot
 * @Description: TODO
 * @date 2018/11/22 17:13
 */
@Component
public class CrosIntercept implements HandlerInterceptor {
    private static final Logger logger= LoggerFactory.getLogger(CrosIntercept.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("CrosIntercept start.............");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String link = req.getRequestURL().toString();
        /**解决跨域问题 begin**/
        String reqOriginRealPath = req.getHeader("Referer");
        String reqPath="*";
        if(reqOriginRealPath!=null){
            int len=reqOriginRealPath.indexOf("/",7);
            reqPath= reqOriginRealPath.substring(0,len);
        }else {
            reqPath=req.getHeader("Origin");
        }
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        res.setHeader("Access-Control-Allow-Origin", reqPath);
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        res.setHeader("Access-Control-Max-Age", "86400");
        res.setHeader("Access-Control-Allow-Headers", "Accept,Authorization,Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,cs_sso_tokenid,userId,token");
        res.setHeader("XDomainRequestAllowed","1");
        /**解决跨域问题 end**/
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}


