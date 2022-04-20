package com.example.partyplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FirstFragment extends Fragment {
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
        //new FriendSuggestionWorker(viewGroup).execute();
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
//        DatabaseReference userRef = mDatabase.child("Users");
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference userRef = mDatabase.child("Users");
//        authentication = FirebaseAuth.getInstance();
//        firebaseUser = authentication.getCurrentUser();
        //String userId = authentication.getCurrentUser().getUid();
        final int[] usersCount = new int[1];

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
             usersCount[0] = (int) snapshot.getChildrenCount();
             //Log.d("mytag, num of users", String.valueOf(usersCount[0]));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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



        dataSnapshot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                //Log.d("95", user.toString());
                String name = user.username;
                String email = user.email;

                List<String> requestSentList = user.requestsSent;

                List<String> requestReceivedList = user.requestsReceived;
                String requestStatus = "Add Friend";
                if (requestSentList.contains(firebaseUserEmail.toString())) {
                    requestStatus = "Request Received";
                }
                if (requestReceivedList.contains(firebaseUserEmail.toString())) {
                    requestStatus = "Request Sent";
                }

                if (!email.equals(firebaseUserEmail)) {
                    //Log.d("ff107", firebaseUserEmail);
                    RequestListItem itemCard = new RequestListItem(name, requestStatus, user.email);
                    linkItemCardArrayList.add(itemCard);
                    itemviewAdapter.notifyItemInserted(0);
                }
//                Log.d("mytag, size of list", String.valueOf(linkItemCardArrayList.size()));
//                Log.d("mytag, user count", String.valueOf(usersCount[0]));
                if (linkItemCardArrayList.size() >= usersCount[0]-1) {
                    //Log.d("mytag", "In 103 if loop");
                   progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Log.d("133", snapshot.getValue(User.class).toString());
                User user = snapshot.getValue(User.class);
                String name = user.username;
                String email = user.email;
                List<String> requestReceivedList = user.requestsReceived;
                String requestStatus = "Add Friend";
                if (requestReceivedList.contains(firebaseUserEmail)) {
                    //Log.d("1332", "In if condition");
                    requestStatus = "Request Sent";
                    //Log.d("1332", requestStatus);
                }

                if (!email.equals(firebaseUserEmail)) {
//                    Log.d("ff107", firebaseUserEmail);
//                    Log.d("1333", requestStatus);

                    //linkItemCardArrayList.remove();
                    //RequestListItem itemCard = new RequestListItem(name, requestStatus, user.email);
                    RequestListItem currObj = new RequestListItem(name, "Add Friend", user.email);
                    int indexOf = 0;
                    if (linkItemCardArrayList.contains(currObj)) {
                        indexOf = linkItemCardArrayList.indexOf(currObj);
                        //Log.d("1334", String.valueOf(indexOf));
                        linkItemCardArrayList.set(indexOf, new RequestListItem(name, requestStatus, user.email));
                    }
                    //linkItemCardArrayList.add(new RequestListItem(name, requestStatus, user.email));

                    //itemviewAdapter.notifyItemInserted(0);
                    itemviewAdapter.notifyItemChanged(indexOf);
                }
//                Log.d("mytag, size of list", String.valueOf(linkItemCardArrayList.size()));
//                Log.d("mytag, user count", String.valueOf(usersCount[0]));
//                if (linkItemCardArrayList.size() >= usersCount[0]-1) {
//                    Log.d("mytag", "In 103 if loop");
//                    //progressBar.setVisibility(View.GONE);
//                }

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
