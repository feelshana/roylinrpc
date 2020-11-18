package com.roylin.rpc.server.registry;

import com.alibaba.fastjson.JSON;
import com.roylin.rpc.common.Constants;
import com.roylin.rpc.common.CuratorClient;
import com.roylin.rpc.common.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author roylin
 * @since 2020/11/3 13:51
 */
public class ZkServiceRegistry {

    Logger logger= LoggerFactory.getLogger(this.getClass());

    private CuratorClient curatorClient;

    public ZkServiceRegistry(String zkAddress) {
        this.curatorClient =new CuratorClient(zkAddress);
    }

    /**
    * @Description: 服务节点注册到ZK
    * @Param: server的ip,port,server包含的服务
    * @return:
    * @Date: 2020/11/3
    */
    public void  registryService(String host, Integer port, Map<String,Object> serviceMap){
        ServerInfo serverInfo=new ServerInfo(host,port,serviceMap);
        String key=host+"_"+port;
        String serverInfoString= JSON.toJSONString(serverInfo);
        try {
            curatorClient.createPathData(Constants.ZK_DATA_PATH+"_"+key,serverInfoString.getBytes());
            logger.info("服务端{},端口{}ZK上注册节点成功",host,port);
        } catch (Exception e) {
            logger.error("服务端{},端口{}ZK上注册节点失败",host,port,e);
            e.printStackTrace();
        }
    }




}
