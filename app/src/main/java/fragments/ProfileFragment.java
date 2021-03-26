package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import activities.EditProfileActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import pedroadmn.instagramclone.com.R;

public class ProfileFragment extends Fragment {

    private ProgressBar progressBar;
    private CircleImageView cvProfileImage;
    private GridView gvProfile;
    private TextView tvPostNumber;
    private TextView tvFollowersNumber;
    private TextView tvFollowingNumber;
    private Button btEditProfile;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeComponents(view);

        return view;
    }

    private void initializeComponents(View view) {
        progressBar = view.findViewById(R.id.progressBarProfile);
        cvProfileImage = view.findViewById(R.id.cvProfileImage);
        gvProfile = view.findViewById(R.id.gvProfile);
        tvPostNumber = view.findViewById(R.id.tvPostNumber);
        tvFollowersNumber = view.findViewById(R.id.tvFollowersNumber);
        tvFollowingNumber = view.findViewById(R.id.tvFollowingNumber);
        btEditProfile = view.findViewById(R.id.btEditProfile);

        btEditProfile.setOnClickListener(v -> openEditProfileScreen());
    }

    private void openEditProfileScreen() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }
}