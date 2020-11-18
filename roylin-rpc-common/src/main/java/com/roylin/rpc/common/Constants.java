package com.roylin.rpc.common;

/**
 * @author roylin
 * @since 2020/11/3 13:59
 */
public class Constants {

    public static int ZK_SESSION_TIMEOUT = 5000;
    public static int ZK_CONNECTION_TIMEOUT = 5000;

    public static String ZK_REGISTRY_PATH = "/registry";
    public static String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

    public static String ZK_NAMESPACE = "roylin-rpc";
}
