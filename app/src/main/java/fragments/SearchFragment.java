package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.SearchAdapter;
import helpers.FirebaseConfig;
import models.User;
import pedroadmn.instagramclone.com.R;

public class SearchFragment extends Fragment {

    private SearchView svUsers;
    private RecyclerView rvUsers;
    private List<User> users;
    private DatabaseReference userRef;
    private SearchAdapter searchAdapter;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initializeComponents(view);
        svUsers.setQueryHint("Search Users");
        svUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String typedText = newText.toLowerCase();
                searchUsers(typedText);
                return true;
            }
        });
        return view;
    }

    private void searchUsers(String typedText) {
        users.clear();

        if (typedText.length() >= 2) {
            Query query = userRef.orderByChild("searchName")
                    .startAt(typedText)
                    .endAt(typedText + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        users.add(ds.getValue(User.class));
                    }

                    searchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void initializeComponents(View view) {
        svUsers = view.findViewById(R.id.svUsers);

        rvUsers = view.findViewById(R.id.rvUsers);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        users = new ArrayList<>();
        userRef = FirebaseConfig.getFirebase().child("users");

        searchAdapter = new SearchAdapter(users , getActivity());
        rvUsers.setAdapter(searchAdapter);
    }
}