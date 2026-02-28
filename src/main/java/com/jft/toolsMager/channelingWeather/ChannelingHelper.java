package com.jft.toolsMager.channelingWeather;

public class ChannelingHelper {

    private static boolean isFromChannelingFlag = false;

    public static boolean isFromChannelingFlag() {
        return isFromChannelingFlag;
    }

    public static void setChannelingFlag(boolean value){
        isFromChannelingFlag = value;
    }
}
