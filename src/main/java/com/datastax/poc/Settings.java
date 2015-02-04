package com.datastax.poc;

import java.util.Set;


/**
 * Created by patrick on 03/02/15.
 */
public class Settings {

    private static Settings s_settings;


    public static synchronized Settings getInstance() {
        if (s_settings == null) {
            s_settings = new Settings();
        }
        return s_settings;
    }

    public String getKeyspace() {
        return "lotsys";
    }

    public String[] getContactPoints() {
        return new String[]{"127.0.0.1"};
    }
}
