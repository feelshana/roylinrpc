package com.roylin.rpc.client.connection;

import com.roylin.rpc.client.RpcClientHandler;
import com.roylin.rpc.common.ServerInfo;

/**
 * @author roylin
 * @since 2020/11/26 19:33
 */
public class RpcConnection {

    private ServerInfo serverInfo;
    private RpcClientHandler rpcClientHandler;

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public RpcClientHandler getRpcClientHandler() {
        return rpcClientHandler;
    }

    public void setRpcClientHandler(RpcClientHandler rpcClientHandler) {
        this.rpcClientHandler = rpcClientHandler;
    }
}
