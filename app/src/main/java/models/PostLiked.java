package models;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import helpers.FirebaseConfig;

public class PostLiked {

    public int likeQtt = 0;
    public Feed feed;
    public User user;

    public PostLiked() {

    }

    public int getLikeQtt() {
        return likeQtt;
    }

    public void setLikeQtt(int likeQtt) {
        this.likeQtt = likeQtt;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateLikes(int qtt) {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();

        DatabaseReference postLikesRef = firebaseRef.child("post-likes")
                .child(feed.getId())
                .child("likeQtt");

        setLikeQtt(getLikeQtt() + qtt);

        postLikesRef.setValue(getLikeQtt());
    }

    public void save() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("userName", user.getName());
        userData.put("photoPath", user.getPhotoPath());

        DatabaseReference postLikedRef = firebaseRef.child("post-likes")
                .child(feed.getId())
                .child(user.getId());

        postLikedRef.setValue(userData);

        updateLikes(1);
    }

    public void removeLike() {
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();

        DatabaseReference postLikedRef = firebaseRef.child("post-likes")
                .child(feed.getId())
                .child(user.getId());

        postLikedRef.removeValue();

        updateLikes(-1);
    }
}
