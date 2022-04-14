package com.example.partyplanner;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

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

import java.util.ArrayList;


public class FirstFragment extends Fragment {
    private final ArrayList<RequestListItem> linkItemCardArrayList = new ArrayList<>();
    private RecyclerView linkCollectorRecyclerView;
    private RequestViewAdapter itemviewAdapter;

    private static final String BACKUP_KEY_INSTANCE = "BACKUP_KEY_INSTANCE";
    private static final String SIZE_OF_LINK_COLLECTOR = "SIZE_OF_LINK_COLLECTOR";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);
        linkCollectorRecyclerView = (RecyclerView) viewGroup.findViewById(R.id.links_view);
        init(savedInstanceState, viewGroup);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Deletes items on swiping.
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                return;

            }
        });
        itemTouchHelper.attachToRecyclerView(linkCollectorRecyclerView);
        return viewGroup;
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        init(savedInstanceState);
//
//        //Specify what action a specific gesture performs, in this case swiping right or left deletes the entry
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            // Deletes items on swiping.
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getLayoutPosition();
//                linkItemCardArrayList.remove(position);
//                itemviewAdapter.notifyItemRemoved(position);
//                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myConstraintLayout), "Link Removed", Snackbar.LENGTH_SHORT);
//                mySnackbar.show();
//
//            }
//        });
//        itemTouchHelper.attachToRecyclerView(linkCollectorRecyclerView);
//    }


    // Handling Orientation Changes on Android
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        int size = linkItemCardArrayList == null ? 0 : linkItemCardArrayList.size();
//        outState.putInt(SIZE_OF_LINK_COLLECTOR, size);
//
//        for (int i = 0; i < size; i++) {
//            // put itemName information into instance
//            outState.putString(BACKUP_KEY_INSTANCE + i + "1" , linkItemCardArrayList.get(i).getItemName());
//            // put itemUrl information into instance
//            outState.putString(BACKUP_KEY_INSTANCE + i + "2" , linkItemCardArrayList.get(i).getItemUrl());
//        }
//        super.onSaveInstanceState(outState);
//
//    }

    private void init(Bundle savedInstanceState, ViewGroup container) {
        initialItemData(savedInstanceState);
        createRecyclerView(container);
    }

    private void initialItemData(Bundle savedInstanceState) {
        // If Opening activity is not for the first time.
//        if (savedInstanceState != null && savedInstanceState.containsKey(SIZE_OF_LINK_COLLECTOR)) {
//            if (linkItemCardArrayList == null || linkItemCardArrayList.size() == 0) {
//                int size = savedInstanceState.getInt(SIZE_OF_LINK_COLLECTOR);
//                // Retrieve keys we stored in the instance
//                for (int i = 0; i < size; i++) {
//                    String itemName = savedInstanceState.getString(BACKUP_KEY_INSTANCE + i + "1");
//                    String itemUrl = savedInstanceState.getString(BACKUP_KEY_INSTANCE + i + "2" );
//                    LinkItemCard itemCard = new LinkItemCard(itemName, itemUrl);
//                    linkItemCardArrayList.add(itemCard);
//                }
//            }
//        }
        FirebaseAuth authentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authentication.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name = snapshot.child("username").getValue().toString();
                //ArrayList<String> friendsList = snapshot.child("friends").getValue();

                if (!name.equals(firebaseUser.toString())) {
                    RequestListItem itemCard = new RequestListItem(name);
                    linkItemCardArrayList.add(itemCard);
                    itemviewAdapter.notifyItemInserted(0);
                }

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

    }

    private void createRecyclerView(ViewGroup container) {
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(container.getContext());

        linkCollectorRecyclerView.setHasFixedSize(true);
        itemviewAdapter = new RequestViewAdapter(linkItemCardArrayList);
        RequestItemListener itemClickListener = new RequestItemListener() {
            @Override
            public void onRequestItemClick(int position, String userName, Context context) {
                linkItemCardArrayList.get(position).onRequestItemClick(position, userName, context);
                itemviewAdapter.notifyItemChanged(position);
            }
        };
        itemviewAdapter.setOnItemClickListener(itemClickListener);
        linkCollectorRecyclerView.setAdapter(itemviewAdapter);
        linkCollectorRecyclerView.setLayoutManager(rLayoutManger);
    }

//    private void addItemToCollector(int position, String item_name, String item_url) {
//
//        if (!Patterns.WEB_URL.matcher(item_url).matches()) {
//            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myConstraintLayout), "Invalid URL Format", Snackbar.LENGTH_SHORT);
//            mySnackbar.show();
//        }
//        else {
//            linkItemCardArrayList.add(position, new LinkItemCard(item_name, item_url));
//            itemviewAdapter.notifyItemInserted(position);
//            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myConstraintLayout), "Link Added Successfully", Snackbar.LENGTH_SHORT);
//            mySnackbar.show();
//        }
//    }
}
