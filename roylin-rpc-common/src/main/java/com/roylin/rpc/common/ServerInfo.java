package com.roylin.rpc.common;/**
 * @author roylin
 * @since 2020/11/3 13:27
 */

import java.util.List;
import java.util.Map;

/**
 * @author: wangchao
 * @date: 2020-11-03 
 **/
public class ServerInfo {

    private String host;

    private Integer port;

    private Map<String,Object> serviceMap;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Map<String, Object> getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public ServerInfo(String host, Integer port, Map<String, Object> serviceMap) {
        this.host = host;
        this.port = port;
        this.serviceMap = serviceMap;
    }
}
