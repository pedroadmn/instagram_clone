package models;

import java.io.Serializable;

public class Feed implements Serializable {
    private String id;
    private String postPhoto;
    private String description;
    private String userName;
    private String userPhoto;

    public Feed() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(String photo) {
        this.postPhoto = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
