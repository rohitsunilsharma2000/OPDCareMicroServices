package com.mycompany.useraccess.context;


import com.mycompany.useraccess.enums.AppType;

public class AppContextHolder {
    private static final ThreadLocal<AppType> appTypeHolder = new ThreadLocal<>();

    public static void setAppType(AppType appType) {
        appTypeHolder.set(appType);
    }

    public static AppType getAppType() {
        return appTypeHolder.get();
    }

    public static void clear() {
        appTypeHolder.remove();
    }
}
