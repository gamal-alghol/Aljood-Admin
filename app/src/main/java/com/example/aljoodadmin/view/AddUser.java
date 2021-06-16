package com.example.aljoodadmin.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aljoodadmin.model.User;
import com.example.aljoodadmin.notifcation.Client;
import com.example.aljoodadmin.notifcation.MyResponse;
import com.example.aljoodadmin.notifcation.Notification;
import com.example.aljoodadmin.notifcation.NotificationAPI;
import com.example.aljoodadmin.notifcation.NotificationSender;
import com.example.aljoodadmin.utils.Constans;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.aljoodadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUser extends AppCompatActivity {
    private EditText name,email,password,account,points;
    private Button add;
    private FirebaseAuth firebaseAuth;
    private User BeforUser;
    private  Long time;
    private NotificationAPI apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
name=findViewById(R.id.name);
        email=findViewById(R.id.editTextEmailAddress);
        password=findViewById(R.id.password_edit);
        account=findViewById(R.id.account_edit);
        add=findViewById(R.id.add_user);
        points=findViewById(R.id.points_edit);
firebaseAuth=FirebaseAuth.getInstance();

if(getIntent().hasExtra("id")){
    UpdateUser();

}else {
    addUser();

        }
    }

    private void addUser() {

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValid(name) && inputValid(email) && inputValid(account) && inputValid(password)&& inputValid(points)) {

                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        time = getCurrentTimeMilliSecond();
                                        User user = new User(name.getText().toString()
                                                , email.getText().toString()
                                                , password.getText().toString(), ""
                                                , firebaseAuth.getUid()
                                                , Double.parseDouble(account.getText().toString()),Integer.parseInt(points.getText().toString()), time);

                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(
                                                Constans.FIREBASE_DB_USERS_TABLE);
                                        mDatabase.child(user.getFirebaseId())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Constans.bDiloge(AddUser.this, "اضافة مستخدم", "تم اضافة مستخدم بنجاح", R.drawable.ic_baseline_check_circle_24);
                                                } else {
                                                    Constans.bDiloge(AddUser.this, "اضافة مستخدم", "فشل اضافة مستخدم ", R.drawable.ic_cross);
                                                }
                                            }
                                        });

                                    } else {
                                        Constans.bDiloge(AddUser.this, "اضافة مستخدم", "فشل اضافة مستخدم ", R.drawable.ic_cross);

                                    }

                                }
                            });

                }


            }
        });
    }

    private void UpdateUser() {
        apiService = Client.getClient("https://fcm.googleapis.com/").create(NotificationAPI.class);

        email.setEnabled(false);
        password.setEnabled(false);
        add.setText("تحديث");
        FirebaseDatabase.getInstance().getReference(Constans.FIREBASE_DB_USERS_TABLE)
                .child(getIntent().getStringExtra("id")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BeforUser=snapshot.getValue(User.class);
                name.setText(BeforUser.getName());
                email.setText(BeforUser.getEmail());
                password.setText(BeforUser.getPassword());
                account.setText(BeforUser.getAccount()+"");
                points.setText(BeforUser.getPoints()+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( inputValid(name) && inputValid(email) &&inputValid(account)&& inputValid(password)&& inputValid(points)) {

                    time=getCurrentTimeMilliSecond();
                    final User user =new User(name.getText().toString()
                            ,email.getText().toString()
                            ,password.getText().toString(),BeforUser.getTokenId()
                            ,BeforUser.getFirebaseId()
                            ,Double.parseDouble(account.getText().toString())
                            ,Integer.parseInt(points.getText().toString())
                            ,time);

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(
                            Constans.FIREBASE_DB_USERS_TABLE);
                    mDatabase.child(user.getFirebaseId())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                sendNotifications(user.getTokenId(), "aljood","تم تعديل البيانات");
                                Constans.bDiloge(AddUser.this,"تعديل مستخدم","تم التعديل بنجاح", R.drawable.ic_baseline_check_circle_24);
                            }else {
                                Constans.bDiloge(AddUser.this,"تعديل مستخدم","فشل التعديل", R.drawable.ic_cross);
                            }
                        }
                    });
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
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
    private boolean inputValid(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(),"يرجى اضافة"+editText.getHint(),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static long getCurrentTimeMilliSecond() {
        Calendar aGMTCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return aGMTCalendar.getTimeInMillis();
    }

}