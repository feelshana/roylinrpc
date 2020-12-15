package com.roylin.rpc.client.connection;

import com.roylin.rpc.client.RpcClientHandler;
import com.roylin.rpc.client.route.AbstractRouteStrategy;
import com.roylin.rpc.common.ServerInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author roylin
 * @since 2020/11/26 19:38
 */
public class ConnectionManager {

    private static  ConnectionManager connectionManager;

    Map<String,RpcConnection> connectionMap=new ConcurrentHashMap<String, RpcConnection>();



    public static ConnectionManager getInstance(){
        if(null==connectionManager){
            synchronized (connectionManager){
                if(null==connectionManager){
                    connectionManager=new ConnectionManager();
                }
            }
        }
        return connectionManager;
    }

    public void  add(RpcConnection connection){
        connectionMap.put(connection.getServerInfo().getHost(),connection);
    }

    public void remove(ServerInfo serverInfo) {
        RpcConnection rpcConnection=connectionMap.get(serverInfo.getHost());
        RpcClientHandler clientHandler=rpcConnection.getRpcClientHandler();
        if(null!=clientHandler){
            clientHandler.close();
            connectionMap.remove(serverInfo.getHost());
        }
    }
    /**
    * @Description: 路由conncetion地址，多个server 连接中选一个
    * @Param:
    * @return:
    * @Date: 2020/12/2
    */
    public RpcConnection routeConnection() {
        return  new AbstractRouteStrategy() {
            public RpcConnection findConnection(Map<String, RpcConnection> connectionMap) {
                return null;
            }
        }.findConnection(this.connectionMap);


    }
}
