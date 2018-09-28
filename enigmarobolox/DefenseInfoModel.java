package com.enigmarobolox.arm_avi.enigmarobolox;

import java.io.Serializable;

/**
 * Created by Arm_AVI on 3/27/2018.
 */

public class DefenseInfoModel implements Serializable {

    private String title, phone;
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
