package ${groupId}.thread;


import ${groupId}.controller.WebSocketDemoServer;

import java.util.Random;

/**
 * Descripton：业务处理thread
 * Author: tanggang
 * Date: 2018/11/16
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class BizHandleThread implements Runnable {
    private WebSocketDemoServer server;
    private String orderNo;
    private String payAmt;

    /**
     * 构造方法
     *
     * @param server
     * @param orderNo
     * @param payAmt
     */
    public BizHandleThread(WebSocketDemoServer server, String orderNo, String payAmt) {
        this.server = server;
        this.orderNo = orderNo;
        this.payAmt = payAmt;
    }

    public void run() {
        try {
            int r = 0;
            int max = 5;
            int min = 2;
            Random random = new Random();
            r = random.nextInt(max) % (max - min + 1) + min;
            Thread.sleep(r * 1000); //模拟业务逻辑  线程等待2-5秒
            r = random.nextInt(10);
            String payRes = "支付成功!";
            if (r >= 6)
                payRes = "支付失败!";
            String returnMsg = "订单号：" + orderNo + ",支付金额" + payAmt + "元," + payRes;
            server.currentSession.getBasicRemote().sendText(returnMsg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
