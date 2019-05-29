package com.tianyi.eventbus;

import java.io.Serializable;

/**
 * Uesr：92863 on 2019/5/29 16:35
 * Email：928636443@qq.com
 * Project: HermesEventbus
 */
public class Friend implements Serializable {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Friend(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
