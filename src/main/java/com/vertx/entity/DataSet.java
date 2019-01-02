package com.vertx.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Sebastian Löfstrand (selo@kth.se), Jonas Lundvall (jonlundv@kth.se)
 *
 * DataSet class
 */
public class DataSet implements Serializable {
    // Properties ---------------------------------------------------------------------------------
    private String name;
    private ArrayList<Pulse> data = new ArrayList<>();

    // Getters/setters ----------------------------------------------------------------------------
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Pulse> getData() {
        return data;
    }
    public void setData(ArrayList<Pulse> data) {
        this.data = data;
    }

    // Constructors -------------------------------------------------------------------------------
    public DataSet(String name, ArrayList<Pulse> data) {
        this.name = name;
        this.data = data;
    }

    public DataSet() { }

}

