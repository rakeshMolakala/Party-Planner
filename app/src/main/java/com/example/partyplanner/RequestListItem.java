package com.example.partyplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class RequestListItem extends AppCompatActivity implements RequestItemListener {

    private final String userName;
    private final String userEmail;
    private DatabaseReference mDatabase;
    private FirebaseAuth authentication;
    private FirebaseUser firebaseUser;
    private final String requestStatus;
    private String currentLoggedInUserName;
    private String userId;
    private DatabaseReference userRef;
    private String photoUrl;


    public RequestListItem(String userName, String requestStatus, String userEmail, String photoUrl) {
        this.userName = userName;
        this.requestStatus = requestStatus;
        this.userEmail = userEmail;
        this.photoUrl = photoUrl;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("Users");
        authentication = FirebaseAuth.getInstance();
        firebaseUser = authentication.getCurrentUser();
        userId = firebaseUser.getUid();
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                currentLoggedInUserName = user.username;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list_item);
    }

    @Override
    public boolean equals(Object o) {
        RequestListItem obj = (RequestListItem) o;
        return this.userName.equals(obj.userName) && this.userEmail.equals(obj.userEmail) &&
                this.requestStatus.equals(obj.requestStatus);
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getRequestStatus() {
        return this.requestStatus;
    }

    @Override
    public void onRequestItemClick(int position, String receivingUserName, String receivingUserEmail,
                                   String requestStatus,Context context) {
        if (requestStatus.equals("Add Friend")) {
            makeRequest(receivingUserName, receivingUserEmail, context);
        }
        else if (requestStatus.equals("Accept")) {
            acceptRequest(receivingUserName, receivingUserEmail, context);
        }

    }

    private void acceptRequest(String requestedUserName, String receivingUserEmail,
                               Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        Log.d("tag106", requestedUserName);
        Log.d("tag107", receivingUserEmail);
        alertDialogBuilder.setMessage("Accept " + userName + "'s request?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        userRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                User user = dataSnapshot.getValue(User.class);
                                Log.d("tag115", user.toString());
                                Log.d("tag116", requestedUserName);
                                String name = user.username;
                                String profileImage = user.profileImage;
                                if (name == requestedUserName) {
                                    String firebaseUserEmail = authentication.getCurrentUser().getEmail();
                                    List<String> requestedUserSents = user.requestsSent;
                                    Map<String, List<String>> friendsList = user.friendsList;
                                    String cleanEmail = cleanEmail(firebaseUserEmail);
                                    List<String> nameProfile = new ArrayList<>();
                                    nameProfile.add(currentLoggedInUserName);
                                    //Log.d("tag115", firebaseUser.getPhotoUrl().toString());
                                    if(firebaseUser.getPhotoUrl() == null) {
                                        nameProfile.add("");
                                    }
                                    else {
                                        nameProfile.add(firebaseUser.getPhotoUrl().toString());
                                    }
                                    //nameProfile.add(firebaseUser.getPhotoUrl().toString());
                                    friendsList.put(cleanEmail, nameProfile);
                                    requestedUserSents.remove(firebaseUserEmail);
                                    dataSnapshot.getRef().child("requestsSent").setValue(requestedUserSents);
                                    dataSnapshot.getRef().child("friendsList").setValue(friendsList);
                                    Toast.makeText(context, "Request Accepted!", Toast.LENGTH_SHORT).show();
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

                        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User details = snapshot.getValue(User.class);
                                //Log.d("tag158", details.toString());
                                if (details != null) {
                                    List<String> requestsReceived = details.requestsReceived;
                                    Map<String, List<String>> friendsList = details.friendsList;
                                    String cleanEmail = cleanEmail(receivingUserEmail);


                                    List<String> userPhoto = new ArrayList<>();
                                    userPhoto.add(requestedUserName);
                                    userPhoto.add(photoUrl);
                                    friendsList.put(cleanEmail, userPhoto);

                                    //friendsList.put(cleanEmail,  requestedUserName);
                                    requestsReceived.remove(receivingUserEmail);
                                    snapshot.getRef().child("requestsReceived").setValue(requestsReceived);
                                    snapshot.getRef().child("friendsList").setValue(friendsList);
                                } else {
                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private String cleanEmail(String email) {
        return email.replaceAll("\\.", ",");
    }

    private void makeRequest(String receivingUserName, String receivingUserEmail,
                           Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Send " + userName + " friend request?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference userRef = mDatabase.child("Users");
                        authentication = FirebaseAuth.getInstance();
                        firebaseUser = authentication.getCurrentUser();
                        String userId = firebaseUser.getUid();
                        userRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                User user = dataSnapshot.getValue(User.class);
                                String name = user.username;
                                if (name.equals(receivingUserName)) {
                                    Object currReqReceived;
                                    currReqReceived = dataSnapshot.child("requestsReceived").getValue();
                                    List<String> currReqReceivedList = user.requestsReceived;
                                    firebaseUser = authentication.getCurrentUser();
                                    String firebaseUserEmail = authentication.getCurrentUser().getEmail();
                                    currReqReceivedList.add(firebaseUserEmail);
                                    dataSnapshot.getRef().child("requestsReceived").setValue(currReqReceivedList);
                                    Toast.makeText(context, "Request Sent!", Toast.LENGTH_SHORT).show();
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

                        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User details = snapshot.getValue(User.class);
                                if (details != null) {
                                    List<String> requestsSent = details.requestsSent;
                                    requestsSent.add(receivingUserEmail);
                                    snapshot.getRef().child("requestsSent").setValue(requestsSent);
                                } else {
                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}