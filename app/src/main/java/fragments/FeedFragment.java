package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.FeedAdapter;
import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import models.Feed;
import pedroadmn.instagramclone.com.R;

public class FeedFragment extends Fragment {

    private RecyclerView rvFeed;
    private FeedAdapter feedAdapter;
    private List<Feed> feedList = new ArrayList<>();

    private ValueEventListener feedEventListener;
    private DatabaseReference feedRef;
    private String loggedUserId;

    public FeedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        initializeComponents(view);

        loggedUserId = FirebaseUserHelper.getLoggedUserId();
        feedRef = FirebaseConfig.getFirebase().child("feed").child(loggedUserId);

        rvFeed.setHasFixedSize(true);
        rvFeed.setLayoutManager(new LinearLayoutManager(getActivity()));

        feedAdapter = new FeedAdapter(feedList, getActivity());
        rvFeed.setAdapter(feedAdapter);

        return view;
    }

    private void initializeComponents(View view) {
        rvFeed = view.findViewById(R.id.rvFeed);
    }

    private void getFeedPosts() {
        feedEventListener = feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    feedList.add(ds.getValue(Feed.class));
                }

                Collections.reverse(feedList);
                feedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getFeedPosts();
    }

    @Override
    public void onStop() {
        super.onStop();
        feedRef.removeEventListener(feedEventListener);
    }
}