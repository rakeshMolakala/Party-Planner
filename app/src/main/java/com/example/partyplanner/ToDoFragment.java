package com.example.partyplanner;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editTextName = new EditText(getContext());
                editTextName.setHint("Enter New Task");
                androidx.appcompat.app.AlertDialog.Builder addAlert = new AlertDialog.Builder(getActivity());
                addAlert.setMessage("Add New Task");
                addAlert.setView(editTextName);

                addAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = editTextName.getText().toString();

                        if (task == null || task.trim().equals("")) {
                            Snackbar.make(view, "Please Enter Some Task", Snackbar.LENGTH_LONG)
                                    .setAction("Dismiss", null).show();
                        } else {
                            TodoModel newtodoModel = new TodoModel(false, task);
                            String userId = firebaseUser.getUid();
                            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                                    .child("Tasks").push().setValue(newtodoModel).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(ToDoFragment.this.getActivity(), "Task added", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ToDoFragment.this.getActivity(), "Could not add task", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                addAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                addAlert.show();
            }
        });

        init(viewGroup);
        return viewGroup;
    }

    private void init(ViewGroup container) {
        getTodoList();
        createRecyclerView(container);
    }

    ItemTouchHelper simpleCallBack = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        TodoModel deletedItem = null;

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            TodoModel selectedTask = todoModels.get(position);
            String userId = firebaseUser.getUid();

            switch (direction) {
                case ItemTouchHelper.RIGHT:
                    final EditText editTextName = new EditText(getContext());
                    editTextName.setHint("Enter Updated Task");
                    editTextName.setText(selectedTask.getText());
                    androidx.appcompat.app.AlertDialog.Builder alert1 = new AlertDialog.Builder(getActivity());
                    alert1.setMessage("Edit task");
                    alert1.setView(editTextName);
                    alert1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String YouEditStringValue = editTextName.getText().toString();
                            if (YouEditStringValue == null || YouEditStringValue.trim().equals("")) {

                            } else {
                                int position = viewHolder.getAdapterPosition();
                                FirebaseDatabase.getInstance().getReference("Users").child(userId)
                                        .child("Tasks/" + selectedTask.getKey() + "/text").setValue(YouEditStringValue).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(ToDoFragment.this.getActivity(), "Task Updated", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(ToDoFragment.this.getActivity(), "Could not update task", Toast.LENGTH_LONG).show();
                                    }
                                });
                                todoAdapter.notifyItemChanged(position);
                            }
                        }
                    });
                    alert1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            todoAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });
                    alert1.show();
                    break;
                case ItemTouchHelper.LEFT:
                    androidx.appcompat.app.AlertDialog.Builder alert2 = new AlertDialog.Builder(getActivity());
                    alert2.setMessage("Are you sure you want to delete?");
                    alert2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int position = viewHolder.getAdapterPosition();
                            deletedItem = todoModels.get(position);
                            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                                    .child("Tasks/" + selectedTask.getKey()).removeValue().addOnCompleteListener(del -> {
                                if (del.isSuccessful()) {
                                    Toast.makeText(ToDoFragment.this.getActivity(), "Task: " + selectedTask.getText() + " deleted", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ToDoFragment.this.getActivity(), "Could not delete task", Toast.LENGTH_LONG).show();
                                }
                            });
                            todoAdapter.notifyItemRemoved(position);
                            dialogInterface.cancel();
                        }
                    });
                    alert2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            todoAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });
                    alert2.show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_light))
                    .addSwipeRightActionIcon(R.drawable.ic_edit)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    });

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
        simpleCallBack.attachToRecyclerView(todoRecyclerView);
    }
}
