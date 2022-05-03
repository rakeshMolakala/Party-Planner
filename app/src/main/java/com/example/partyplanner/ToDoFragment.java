package com.example.partyplanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToDoFragment extends Fragment {

    ArrayList<TodoModel> todoModels = new ArrayList<>();
    private RecyclerView todoRecyclerView;
    private TodoAdapter todoAdapter;
    FirebaseAuth authentication = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = authentication.getCurrentUser();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_todo, container, false);
        todoRecyclerView = viewGroup.findViewById(R.id.todo_view);

        FloatingActionButton fab = viewGroup.findViewById(R.id.fab);

        init(viewGroup);
        return viewGroup;    }

    private void init(ViewGroup container) {
        getTodoList();
        createRecyclerView(container);
    }

    void getTodoList() {
        String userId = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(userId).child("Tasks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todoModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TodoModel text = dataSnapshot.getValue(TodoModel.class);
                    text.setKey(dataSnapshot.getKey());
                    todoModels.add(text);
                    todoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void createRecyclerView(ViewGroup container) {
        RecyclerView.LayoutManager rLayoutManger = new LinearLayoutManager(container.getContext());
        todoRecyclerView.setHasFixedSize(true);
        todoAdapter = new TodoAdapter(todoModels);
        TodoListener todoListener = new TodoListener() {


            @Override
            public void onCheckBoxClick(int position, boolean isChecked) {
                TodoModel selectedTask = todoModels.get(position);
                String userId = firebaseUser.getUid();
                FirebaseDatabase.getInstance().getReference("Users").child(userId)
                        .child("Tasks/" + selectedTask.getKey() + "/checked").setValue(isChecked);
                todoAdapter.notifyItemChanged(position);
            }
        };
        todoAdapter.setOnItemClickListener(todoListener);
        todoRecyclerView.setAdapter(todoAdapter);
        todoRecyclerView.setLayoutManager(rLayoutManger);
    }
}

