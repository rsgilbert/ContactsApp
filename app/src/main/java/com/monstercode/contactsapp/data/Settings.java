package com.monstercode.contactsapp.data;

public class Settings {
    private static boolean clickToCall;

    public static boolean isClickToCall() {
        return clickToCall;
    }

    public static void setClickToCall(boolean clickToCall) {
        Settings.clickToCall = clickToCall;
    }
}
