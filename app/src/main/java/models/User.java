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
    private int followers = 0;
    private int following = 0;
    private int posts = 0;

    public User() {
        setFollowers(0);
        setFollowing(0);
        setPosts(0);
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

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public void save() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference usersRef = firebaseRef.child("users").child(getId());
        usersRef.setValue(this);
    }

    public void update() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();

        HashMap<String, Object> object = new HashMap<>();
        object.put("/users/" + getId() + "/name", getName());
        object.put("/users/" + getId() + "/searchName", getSearchName());
        object.put("/users/" + getId() + "/photoPath", getPhotoPath());

        firebaseRef.updateChildren(object);
    }

    public void updatePostsQtt() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference usersRef = firebaseRef.child("users").child(getId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("posts", getPosts());

        usersRef.updateChildren(data);
    }

    private Map<String, Object> convertToMap() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", getEmail());
        userMap.put("name", getName());
        userMap.put("id", getId());
        userMap.put("photoPath", getPhotoPath());
        userMap.put("followers", getFollowers());
        userMap.put("following", getFollowing());
        userMap.put("posts", getPosts());

        return userMap;
    }
}
