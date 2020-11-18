package com.roylin.rpc.server.service;

import com.roylin.rpc.common.RoylinRpcService;
import com.roylin.rpc.service.HelloService;

/**
 * @author roylin
 * @since 2020/11/3 16:05
 */

@RoylinRpcService(version= "1.0.0.0",className = "com.roylin.rpc.server.service.HelloService")
class HelloServiceImpl  implements HelloService {


    public String sayHello(String name) {
        return "hello"+name;
    }
}
