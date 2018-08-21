package com.example.selectpropertyassignment.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FacilityDetailEntity extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


}
