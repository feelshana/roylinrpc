package com.roylin.rpc.client.proxy;

import com.roylin.rpc.common.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author roylin
 * @since 2020/11/3 16:41
 */
public class ClientInvocationHandler<T> implements InvocationHandler {

    private String version;
    private Class<T> className;


    public ClientInvocationHandler(String version, Class<T> className) {
        this.version = version;
        this.className = className;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setArgs(args);
        rpcRequest.setMethod(method.getName());





        return null;
    }
}
