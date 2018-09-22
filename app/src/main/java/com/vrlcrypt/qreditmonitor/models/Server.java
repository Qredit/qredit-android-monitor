package com.vrlcrypt.qreditmonitor.models;

/**
 * Created by vrlcypt  on 21/03/17.
 */

import java.util.Arrays;
import java.util.List;

public enum Server {
    qredit1(0, "api.qredit.cloud", "http://api.qredit.cloud/api/"),
    custom(1, "Custom", "");

    private final int id;
    private final String name;
    private final String apiAddress;

    Server(int id, String name, String apiAddress) {
        this.id = id;
        this.name = name;
        this.apiAddress = apiAddress;
    }

    public static Server fromId(int id){
        switch (id){
            case 0:
                return qredit1;
            case 1:
                return custom;
            default:
                return null;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getApiAddress() {
        return apiAddress;
    }

    public boolean isCustomServer() {
        return id == Server.custom.getId();
    }

    public static List<String> getServers() {
        return Arrays.asList(
                Server.qredit1.name,
                Server.custom.name);
    }
}
