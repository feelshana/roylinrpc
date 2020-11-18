package com.roylin.rpc.client;

import com.roylin.rpc.client.proxy.ClientInvocationHandler;
import com.roylin.rpc.common.ServerInfo;
import com.sun.security.ntlm.Server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author roylin
 * @since 2020/11/3 16:26
 */
public class RoylinRpcClient {

    Map<String, ServerInfo> discoveredServerMap;

    ServerInfo choicedServer ;





  public <T> T createClient(Class<T> t,String version){

      T client= (T) Proxy.newProxyInstance(t.getClassLoader(), new Class<?>[]{t}, new ClientInvocationHandler<T>(version,t));
      return client;
  }


}
