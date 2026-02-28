package com.jft.toolsMager.riptideTouchingWeather;

public class TridentRiptideHelper {
    static boolean jft$isOnTridentRiptideUsingOrStopUsing = ThreadLocal.withInitial(() -> false).get();


    public static void jft$setIsOnTridentRiptideUsingOrStopUsing(boolean value) {
        jft$isOnTridentRiptideUsingOrStopUsing = value;
    }

    public static boolean jft$getIsOnTridentRiptideUsingOrStopUsing(){
        return jft$isOnTridentRiptideUsingOrStopUsing;
    };
}
