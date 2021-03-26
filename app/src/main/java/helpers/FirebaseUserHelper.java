package helpers;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import models.User;

import static helpers.FirebaseConfig.getAuthFirebase;

public class FirebaseUserHelper {

    public static String getUserId() {
        FirebaseAuth auth = getAuthFirebase();
        return auth.getCurrentUser().getUid();
    }

    public static FirebaseUser getCurrentUser() {
        FirebaseAuth auth = getAuthFirebase();
        return auth.getCurrentUser();
    }

    public static User getLoggedUserInfo() {
        FirebaseUser firebaseUser = getCurrentUser();
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(firebaseUser.getDisplayName());

        if (firebaseUser.getPhotoUrl() == null) {
            user.setPhotoPath("");
        } else {
            user.setPhotoPath(firebaseUser.getPhotoUrl().toString());
        }

        return user;
    }

    public static boolean updateUsername(String name) {
        try {
            FirebaseUser user = getCurrentUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.d("Profile", "Error on update user name: ");
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getLoggedUserId() {
        return getCurrentUser().getUid();
    }
}
