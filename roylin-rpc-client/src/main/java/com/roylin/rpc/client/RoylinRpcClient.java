package com.roylin.rpc.client;

import com.alibaba.fastjson.JSON;
import com.roylin.rpc.client.connection.ConnectionManager;
import com.roylin.rpc.client.connection.RpcConnection;
import com.roylin.rpc.client.proxy.ClientInvocationHandler;
import com.roylin.rpc.common.Constants;
import com.roylin.rpc.common.CuratorClient;
import com.roylin.rpc.common.ServerInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * @author roylin
 * @since 2020/11/3 16:26
 */
public class RoylinRpcClient {

    Map<String, ServerInfo> discoveredServerMap;

    ServerInfo choicedServer;
    String zkAddress;

    CuratorClient curatorClient;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RoylinRpcClient(String zkAddress) {
        this.zkAddress = zkAddress;
        curatorClient = new CuratorClient(zkAddress, 60000);
        discoverService();
    }

    /**
     * @Description: 基于zk服务发现
     * @Param:
     * @return:
     * @Date: 2020/11/26
     */
    private void discoverService() {
        try {
            for (String serverPath : curatorClient.getChildren(Constants.ZK_REGISTRY_PATH)) {
                byte[] data = curatorClient.getData(serverPath);
                ServerInfo serverInfo = JSON.parseObject(new String(data), ServerInfo.class);
                RpcConnection connection = buildConnection(serverInfo);
                ConnectionManager.getInstance().add(connection);
            }
            curatorClient.watchPathChildrenNode(Constants.ZK_REGISTRY_PATH, new PathChildrenCacheListener() {
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent)
                        throws Exception {
                    ChildData data = pathChildrenCacheEvent.getData();
                    ServerInfo serverInfo = JSON.parseObject(new String(data.getData()), ServerInfo.class);
                    switch (pathChildrenCacheEvent.getType()) {
                        case CHILD_ADDED: {
                            addServerConnection(serverInfo);
                            break;
                        }
                        case CHILD_UPDATED:
                        case CHILD_REMOVED: {
                            deleteServerConnection(serverInfo);
                        }


                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Description: 服务节点删除，尝试断开连接，内存删除该connection
     * @Param:
     * @return:
     * @Date: 2020/12/2
     */
    private void deleteServerConnection(ServerInfo serverInfo) {
        if(discoveredServerMap.containsKey(serverInfo.getHost())){
            logger.info("server found lost in  zk{},try to release connection", serverInfo.getHost());
            deleteConnection(serverInfo);
            logger.info("server found lost in  zk{},success release connection", serverInfo.getHost());
        }
    }
    private void deleteConnection(ServerInfo serverInfo) {
        discoveredServerMap.remove(serverInfo.getHost());
        ConnectionManager.getInstance().remove(serverInfo);

    }

    /**
     * @Description: 新的服务端增加，建立连接，并更新到内存中
     * @Param:
     * @return:
     * @Date: 2020/12/2
     */
    private void addServerConnection(ServerInfo serverInfo) {
        if (!discoveredServerMap.containsKey(serverInfo.getHost())) {
            logger.info("new Server find in zk{},try to build connection", serverInfo.getHost());
            buildConnection(serverInfo);
            logger.info("new Server{}, build connection success", serverInfo.getHost());

        }
    }



    private RpcConnection buildConnection(ServerInfo serverInfo) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(4);
        RpcClientHandler handler = new RpcClientHandler();
        bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class).handler(handler);
        RpcConnection rpcConnection = new RpcConnection();
        rpcConnection.setServerInfo(serverInfo);
        rpcConnection.setRpcClientHandler(handler);
        return rpcConnection;
    }

    private Map<String, ServerInfo> initServerMap(String zkAddress) {
        return null;

    }

    private ServerInfo findServer() {
        return null;
    }

    public <T> T createClient(Class<T> t, String version) {

        T client = (T) Proxy.newProxyInstance(t.getClassLoader(), new Class<?>[]{t},
                new ClientInvocationHandler<T>(version, t));
        return client;
    }

}
