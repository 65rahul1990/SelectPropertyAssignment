package com.example.selectpropertyassignment.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;

public class DBManager {
    private static final DBManager ourInstance = new DBManager();
    private Realm realm;

    public static DBManager getInstance() {
        return ourInstance;
    }

    private DBManager() {
        realm = Realm.getDefaultInstance();
    }

    public void setJsonDataToRealmDatabase(ResponseBody body){
        try {
            String jsonStr = body.string();
            JSONObject object = new JSONObject(jsonStr);
            JSONArray jsonArray = object.optJSONArray("facilities");
            realm.beginTransaction();
            if (jsonArray != null){
                realm.createOrUpdateAllFromJson(FacilityEntity.class, jsonArray);
            }
            realm.commitTransaction();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
