package ${groupId}.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

/**
 * @author luiz
 * @Title: TimeJob
 * @ProjectName ${artifactId}-springboot
 * @Description: TODO
 * @date 2018/11/15 16:16
 */
@JobHandler("timeJob")
public class TimeJob extends IJobHandler {

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        //
        System.out.println("开始业务处理。。。。。。。。。。");
        return ReturnT.SUCCESS;
    }
}
