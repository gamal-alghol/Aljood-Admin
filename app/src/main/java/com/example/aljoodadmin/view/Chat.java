package com.example.aljoodadmin.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.adapter.ChatAdapter;
import com.example.aljoodadmin.model.Msg;
import com.example.aljoodadmin.notifcation.Notification;
import com.example.aljoodadmin.notifcation.Client;
import com.example.aljoodadmin.notifcation.MyResponse;
import com.example.aljoodadmin.notifcation.NotificationAPI;
import com.example.aljoodadmin.notifcation.NotificationSender;
import com.example.aljoodadmin.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {
    ImageButton imgSend,btnBack,points;
    RecyclerView listChat;
    EditText editMsg;
    int limitMsgs = 8;
    ArrayList<Msg> msgArrayList;
    String id,token;
    TextView title;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    private NotificationAPI apiService;
    int numOfPoints;
    AlertDialog dialog;
    ChatAdapter messagesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imgSend=findViewById(R.id.send);
        editMsg=findViewById(R.id.edit_text_msg);
        listChat=findViewById(R.id.list_chat);
        progressBar=findViewById(R.id.progressBar2);
        btnBack=findViewById(R.id.btn_back);
        points=findViewById(R.id.points_btn);
title=findViewById(R.id.title);
        msgArrayList=new ArrayList<>();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(NotificationAPI.class);
if (getIntent().hasExtra("id")&&getIntent().hasExtra("name")&&getIntent().hasExtra("points")){
    id=getIntent().getStringExtra("id");
    token=getIntent().getStringExtra("token");
    numOfPoints=getIntent().getIntExtra("points",0);
    title.setText(getIntent().getStringExtra("name"));
}
        getMessages();
        layoutManager = new LinearLayoutManager(this);
        listChat.setLayoutManager(layoutManager);
        listChat.setItemAnimator(new DefaultItemAnimator());
        messagesAdapter = new ChatAdapter(msgArrayList, getApplicationContext());
        listChat.setAdapter(messagesAdapter);
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMsg.getText().toString()!=null&&!editMsg.getText().toString().equals("")){
                    sendMsg(id);

                }
            }
        });
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
                  dialog = builder.create();
                 View dialogLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.points_edit, null,false);
                  final EditText points=dialogLayout.findViewById(R.id.points_edit);
                Button update=dialogLayout.findViewById(R.id.update_points);
                points.setText(numOfPoints+"");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!points.getText().toString().isEmpty()){
                        onUpdatePoints(Integer.parseInt(points.getText().toString()));}
                    }
                });
                dialog.setView(dialogLayout);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                if (!dialog.isShowing())
                    dialog.show();

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void onUpdatePoints(int numPoints) {
        FirebaseDatabase.getInstance().getReference(Constans.FIREBASE_DB_USERS_TABLE)
                .child(id).child("points").setValue(numPoints).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sendNotifications(token, "aljood","تم تحديث بياناتك");
                Constans.bDiloge(Chat.this,"تحديث مستخدم","تم التحديث بنجاح", R.drawable.ic_baseline_check_circle_24);
dialog.dismiss();
            }
        });
    }

    private void getMessages() {
        FirebaseDatabase.getInstance().getReference(Constans.FIREBASE_DB_CHAT).child(id).orderByChild("time")
          .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        limitMsgs = limitMsgs + 1;
                        Msg message = dataSnapshot.getValue(Msg.class);
                        msgArrayList.add(message);
                        messagesAdapter.notifyDataSetChanged();
//                        refreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        listChat.scrollToPosition(msgArrayList.size() - 1);
                        listChat.setEnabled(true);
                    }
                }).run();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

            }


        });
        progressBar.setVisibility(View.GONE);
    }

    private void sendMsg(String id) {
        imgSend.setEnabled(false);
        final Msg msg=new Msg(id,2,editMsg.getText().toString(),getCurrentDate());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("chat").child(id).push().setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    editMsg.setText("");
                    imgSend.setEnabled(true);

                    sendNotifications(token, "aljood",msg.getMessage().toString());

                }else{
                    imgSend.setEnabled(true);
                }
            }
        });
    }
    public void sendNotifications(String usertoken, String title, String message) {
        Notification data = new Notification(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(Chat.this, "Failed", Toast.LENGTH_LONG).show();
                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    private Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }
}