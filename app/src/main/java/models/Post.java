package models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;

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

    public boolean save(DataSnapshot followersSnapshot) {
        Map object = new HashMap();

        User loggedUser = FirebaseUserHelper.getLoggedUserInfo();

        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();

        String idCombination = "/" + getUserId() + "/" + getId();
        object.put("/posts" + idCombination, this);

        for (DataSnapshot follower : followersSnapshot.getChildren()) {
            String follewerId = follower.getKey();

            HashMap<String, Object> followerData = new HashMap<>();
            followerData.put("postPhoto", getPhotoPath());
            followerData.put("description", getDescription());
            followerData.put("id", getId());
            followerData.put("userName", loggedUser.getName());
            followerData.put("userPhoto", loggedUser.getPhotoPath());

            String idUpdate = "/" + follewerId + "/" + getId();

            object.put("/feed" + idUpdate, followerData);
        }

        firebaseRef.updateChildren(object);
        
        return true;
    }
}
