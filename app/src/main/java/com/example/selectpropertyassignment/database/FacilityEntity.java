package com.example.selectpropertyassignment.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FacilityEntity extends RealmObject {
    @PrimaryKey
    private String facility_id;
    private String name;
    private RealmList<FacilityDetailEntity> options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public RealmList<FacilityDetailEntity> getOptions() {
        return options;
    }


}
