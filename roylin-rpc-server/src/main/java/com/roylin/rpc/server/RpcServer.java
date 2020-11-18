package com.roylin.rpc.server;

import com.roylin.rpc.common.RoylinRpcService;
import com.roylin.rpc.server.registry.ZkServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangchao
 * @date: 2020-10-29
 **/
public class RpcServer implements ApplicationContextAware, InitializingBean {

    private String address;

    private Integer port;

    private String  zkAddress;

    private Map<String, Object> serviceMap = new HashMap<String, Object>();

    ZkServiceRegistry zkServiceRegistry;


    public RpcServer(String address, Integer port,String zkAddress) {
        this.address = address;
        this.port = port;
        this.zkAddress=zkAddress;
        zkServiceRegistry=new ZkServiceRegistry(zkAddress);
    }

    public void afterPropertiesSet() throws Exception {

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceMaps = applicationContext.getBeansWithAnnotation(RoylinRpcService.class);

        if (null != serviceMaps && serviceMaps.size() > 0) {
            for (Object service : serviceMaps.values()) {
                RoylinRpcService anotation = service.getClass().getAnnotation(RoylinRpcService.class);
                String version = anotation.version();
                String className = anotation.className();
                addService(className, version, service);
            }
        }
        zkServiceRegistry.registryService(address,port,serviceMap);

    }


    /**
    * @Description: 开启nettyServer,并在zk上注册服务
    * @Param:
    * @return:
    * @Date: 2020/11/3
    */
    public void startServer() {
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap.group(group).channel(NioEventLoopGroup.class)
            .childHandler(new ChannelInitializer<>() {}).option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true).bind(address, port).sync();


        channelFuture.channel().closeFuture().sync();

        

    }

    public void addService(String className, String version, Object object) {
        String key = className + "_" + version;
        serviceMap.put(key, object);
    }

}
