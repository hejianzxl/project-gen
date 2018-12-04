package ${groupId}.controller;

import com.alibaba.fastjson.JSON;
import ${groupId}.thread.BizHandleThread;
import ${groupId}.util.Constant;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

/**
 * Descripton：web socket demo controller
 * Author: tanggang
 * Date: 2018/11/16
 * Time: 10:09
 * To change this template use File | Settings | File Templates.
 */
@ServerEndpoint(value = "/web/socket/pay/{orderNo}",configurator = WebSocketConfig.class)
@Component
public class WebSocketDemoServer {
    public Session currentSession;

    @OnOpen
    public void onOpen(Session session, @PathParam("orderNo") String orderNo) throws IOException {
        //预连接 前置处理
        this.currentSession = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        //连接关闭时 逻辑处理
    }

    @OnMessage()
    @SuppressWarnings("unchecked")
    public void onMessage(String json) {
        HashMap<String, String> map = JSON.parseObject(json, HashMap.class);
        String orderNo = map.get("orderNo");
        String payAmt = map.get("payAmt");
        //异步线程池实现  多客户端同时连接
        BizHandleThread bizHandleThread = new BizHandleThread(this, orderNo, payAmt);
        Constant.threadPool.execute(bizHandleThread);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}
