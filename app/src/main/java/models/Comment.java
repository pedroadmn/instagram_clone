package models;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import helpers.FirebaseConfig;

public class Comment implements Serializable {

    private String id;
    private String postId;
    private String userId;
    private String photoPath;
    private String userName;
    private String comment;

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean save() {
        DatabaseReference commentsRef = FirebaseConfig.getFirebase()
                .child("comments")
                .child(getPostId());

        String commentKey = commentsRef.push().getKey();
        setId(commentKey);
        commentsRef.child(getId()).setValue(this);

        return true;
    }
}
