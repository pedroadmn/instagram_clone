package models;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import helpers.FirebaseConfig;

public class Post implements Serializable {
    private String id;
    private String userId;
    private String description;
    private String photoPath;

    public Post() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference postRef = firebaseRef.child("posts");
        String postId = postRef.push().getKey();
        setId(postId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean save() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference usersRef = firebaseRef.child("posts").child(getUserId()).child(getId());
        usersRef.setValue(this);
        return true;
    }
}
