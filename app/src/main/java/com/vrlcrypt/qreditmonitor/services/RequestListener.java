package com.vrlcrypt.qreditmonitor.services;

public interface RequestListener<T> {
    void onFailure(Exception e);
    void onResponse(T object);
}
