package ${groupId}.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author luiz
 * @Title: CrosFilter
 * @ProjectName ${artifactId}-springboot
 * @Description: TODO
 * @date 2018/11/22 16:55
 */
public class CrosFilter  implements Filter {
    private static final Logger logger= LoggerFactory.getLogger(CrosFilter.CrosFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("CrosFilter start.............");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
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
        filterChain.doFilter(req,res);
    }
    @Override
    public void destroy() {

    }
}
