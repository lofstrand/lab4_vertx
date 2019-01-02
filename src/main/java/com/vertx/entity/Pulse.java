package com.vertx.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Pulse implements Serializable {
    // Constants ----------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    // Properties ---------------------------------------------------------------------------------
    private Date date;
    private int value;

    // Getters/setters ----------------------------------------------------------------------------
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    // Constructors -------------------------------------------------------------------------------
    public Pulse() { }

    public Pulse(Date date, int value) {
        this.date = date;
        this.value = value;
    }
}
