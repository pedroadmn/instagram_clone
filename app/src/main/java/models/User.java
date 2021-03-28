package models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import helpers.FirebaseConfig;

public class User implements Serializable {

    private String id;
    private String name;
    private String searchName;
    private String email;
    private String password;
    private String photoPath;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public void save() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference usersRef = firebaseRef.child("users").child(getId());
        usersRef.setValue(this);
    }

    public void update() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference usersRef = firebaseRef.child("users").child(getId());

        usersRef.updateChildren(convertToMap());
    }

    private Map<String, Object> convertToMap() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", getEmail());
        userMap.put("name", getName());
        userMap.put("id", getId());
        userMap.put("photoPath", getPhotoPath());

        return userMap;
    }
}
