package com.monstercode.contactsapp.data;

public class Settings {
    private static boolean clickToCall = true;
    private static boolean addPhoneContacts = false;

    public static boolean isClickToCall() {
        return clickToCall;
    }

    public static void setClickToCall(boolean clickToCall) {
        Settings.clickToCall = clickToCall;
    }

    public static boolean isAddPhoneContacts() {
        return addPhoneContacts;
    }

    public static void setAddPhoneContacts(boolean addPhoneContacts) {
        Settings.addPhoneContacts = addPhoneContacts;
    }

    private static String token = "";
    public static void setToken(String newToken) {token = newToken;}
    public static String getToken() {return token;}
}
