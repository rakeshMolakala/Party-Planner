package com.example.partyplanner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {
    private final ArrayList<RequestListItem> linkItemCardArrayList = new ArrayList<>();
    private RecyclerView linkCollectorRecyclerView;
    private RequestViewAdapter itemviewAdapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);
        linkCollectorRecyclerView =  viewGroup.findViewById(R.id.links_view);
        progressBar = viewGroup.findViewById(R.id.progressBar);
        init(viewGroup);
        return viewGroup;
    }

    private void init(ViewGroup container) {
        initialItemData();
        createRecyclerView(container);
    }

    private void initialItemData() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth authentication = FirebaseAuth.getInstance();
        String firebaseUserEmail = authentication.getCurrentUser().getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataSnapshot = reference.child("Users");
        String userId = authentication.getCurrentUser().getUid();
        final int[] usersCount = new int[1];

        dataSnapshot.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersCount[0] = (int) snapshot.child("requestsReceived").getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        dataSnapshot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                String name = user.username;

                List<String> requestSentList = user.requestsSent;
                if(requestSentList.size() > 0) {
                    if (requestSentList.contains(firebaseUserEmail)) {
                        RequestListItem itemCard = new RequestListItem(name, "Accept",
                                user.email, user.profileImage);
                        linkItemCardArrayList.add(itemCard);
                        itemviewAdapter.notifyItemInserted(0);
                    }
                    if (linkItemCardArrayList.size() >= usersCount[0]-1) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                String name = user.username;

                RequestListItem currObj = new RequestListItem(name, "Accept", user.email,
                        user.profileImage);
                int indexOf = 0;
                if (linkItemCardArrayList.contains(currObj)) {
                    indexOf = linkItemCardArrayList.indexOf(currObj);
                    linkItemCardArrayList.remove(currObj);
                }
                itemviewAdapter.notifyItemRemoved(indexOf);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void createRecyclerView(ViewGroup container) {
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(container.getContext());
        linkCollectorRecyclerView.setHasFixedSize(true);
        itemviewAdapter = new RequestViewAdapter(linkItemCardArrayList);
        RequestItemListener itemClickListener = new RequestItemListener() {
            @Override
            public void onRequestItemClick(int position, String userName, String userEmail,
                                           String requestStatus, Context context) {
                linkItemCardArrayList.get(position).onRequestItemClick(position, userName, userEmail,
                        requestStatus,
                        context);
                itemviewAdapter.notifyItemChanged(position);
            }
        };
        itemviewAdapter.setOnItemClickListener(itemClickListener);
        linkCollectorRecyclerView.setAdapter(itemviewAdapter);
        linkCollectorRecyclerView.setLayoutManager(rLayoutManger);
    }
}
