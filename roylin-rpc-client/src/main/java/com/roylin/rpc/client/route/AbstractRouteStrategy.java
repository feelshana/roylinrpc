package com.roylin.rpc.client.route;

import com.roylin.rpc.client.connection.RpcConnection;

import java.util.Map;

/**
 * @author roylin
 * @since 2020/12/2 16:25
 */
public abstract class AbstractRouteStrategy {

    public  abstract  RpcConnection findConnection(Map<String,RpcConnection> connectionMap);


}
