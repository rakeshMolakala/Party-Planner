package com.example.partyplanner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class ChatTabFragment extends Fragment {
    private final ArrayList<ChatTabItem> linkItemCardArrayList = new ArrayList<>();
    private RecyclerView linkCollectorRecyclerView;
    private ChatViewAdapter itemviewAdapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_chattab, container, false);
        linkCollectorRecyclerView =  viewGroup.findViewById(R.id.chats_recycler_view);
        progressBar = viewGroup.findViewById(R.id.chatsprogressBar);
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
                usersCount[0] = (int) snapshot.child("friendsList").getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dataSnapshot.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User details = snapshot.getValue(User.class);
                Map<String, String> friendsList = details.friendsList;
                if (friendsList.size() > 0) {
                    for (Map.Entry<String, String> entry : friendsList.entrySet()) {
                        String email = entry.getKey();
                        if(email.equals("dummy")) {
                            continue;
                        }
                        String username = entry.getValue();
                        email = cleanEmail(email);
                        ChatTabItem itemCard = new ChatTabItem(username, email);
                        linkItemCardArrayList.add(itemCard);
                        itemviewAdapter.notifyItemInserted(0);
                    }
                    if (linkItemCardArrayList.size() >= usersCount[0]-1) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




    }

    private String cleanEmail(String email) {
        return email.replaceAll("\\,", ".");
    }

    private void createRecyclerView(ViewGroup container) {
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(container.getContext());
        linkCollectorRecyclerView.setHasFixedSize(true);
        itemviewAdapter = new ChatViewAdapter(linkItemCardArrayList);
        ChatItemListener itemClickListener = new ChatItemListener() {
            @Override
            public void onChatItemClick(int position, String userName, String userEmail,
                                            Context context) {
                linkItemCardArrayList.get(position).onChatItemClick(position, userName, userEmail,
                        context);
                itemviewAdapter.notifyItemChanged(position);
            }
        };
        itemviewAdapter.setOnItemClickListener(itemClickListener);
        linkCollectorRecyclerView.setAdapter(itemviewAdapter);
        linkCollectorRecyclerView.setLayoutManager(rLayoutManger);
    }
}
