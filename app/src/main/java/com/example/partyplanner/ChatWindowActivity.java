package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChatWindowActivity extends AppCompatActivity {

    private String receivingUserName;
    private String receivingUserEmail;
    private EditText message;
    private String senderEmail;
    private Map<String, String> emailToUser;
    private String currentLoggedInUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chat_window);
        FloatingActionButton sendButton = findViewById(R.id.sendButton);
        TextView receiverName = findViewById(R.id.receiverName);
        message = findViewById(R.id.input);
        Intent i = getIntent();
        receivingUserName = i.getStringExtra("receivingUserName");
        receivingUserEmail = i.getStringExtra("receivingUserEmail");
        senderEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        receiverName.setText(receivingUserName);
        LinearLayout profileLinear = findViewById(R.id.profileLinear);

        emailToUser = new HashMap<>();
        emailToUser.put(senderEmail, "You");
        emailToUser.put(receivingUserEmail, receivingUserName);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();

        FirebaseDatabase.getInstance().getReference("Users").child(userId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        currentLoggedInUserName = user.username;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        profileLinear.setOnClickListener(view -> {
            Intent intent = new Intent(ChatWindowActivity.this, ViewProfile.class);
            intent.putExtra("UserName", receivingUserName);
            startActivity(intent);
        });

        sendButton.setOnClickListener(view -> {
            ChatMessageModel messageModel = new ChatMessageModel(senderEmail,
                    receivingUserEmail, message.getText().toString().trim());
            if (message.getText().toString().replace(" ", "").length() == 0) {
                Toast.makeText(ChatWindowActivity.this, "Message is empty", Toast.LENGTH_LONG).show();
                return;
            }
            FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
            String userId1 = firebaseUser1.getUid();
            FirebaseDatabase.getInstance().getReference("Users").child(userId1)
                    .child("Messages").child(receivingUserName).push().setValue(messageModel).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    message.setText("");
                } else {
                    Toast.makeText(ChatWindowActivity.this, "Sending Message Failed", Toast.LENGTH_LONG).show();
                }
            });

            FirebaseDatabase.getInstance().getReference("Users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessageModel messageModel = new ChatMessageModel(senderEmail,
                            receivingUserEmail, message.getText().toString());
                    User user = dataSnapshot.getValue(User.class);
                    String name = user.username;
                    if (name.equals(receivingUserName)) {
                        dataSnapshot.getRef().child("Messages").child(currentLoggedInUserName).push().setValue(messageModel);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });

        ListView listOfMessages = findViewById(R.id.list_of_messages);
        FirebaseListAdapter<ChatMessageModel> adapter = new FirebaseListAdapter<ChatMessageModel>(this, ChatMessageModel.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference("Users")
                .child(userId).child("Messages").child(receivingUserName)) {
            @Override
            protected void populateView(View v, ChatMessageModel model, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessage());
                messageUser.setText(emailToUser.get(model.getSender()));
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }
}