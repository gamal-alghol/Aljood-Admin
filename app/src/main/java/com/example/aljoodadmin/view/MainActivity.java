package com.example.aljoodadmin.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.adapter.PagerAdapter;
import com.example.aljoodadmin.fragment.ContactLenses;
import com.example.aljoodadmin.fragment.Devices;
import com.example.aljoodadmin.fragment.Lenses;
import com.example.aljoodadmin.fragment.Users;
import com.example.aljoodadmin.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter adapter;
    Toolbar mCustomToolbar;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mCustomToolbar = findViewById(R.id.toolbar_condition);
        getRegistrationToken();

        viewPager =  findViewById(R.id.pager);
        if (viewPager != null) {
            setupViewPager(viewPager);

        }


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PagerAdapter(getSupportFragmentManager(), this) ;
        adapter.addFragment(new Users(), "المراكز");
        adapter.addFragment(new Devices(Constans.FIREBASE_DB_Devices), "الأجهزة");
        adapter.addFragment(new Devices(Constans.FIREBASE_DB_Accessories), "الاكسسوارات");
        adapter.addFragment(new Devices(Constans.FIREBASE_DB_FRAMES), "الاطارات");
        adapter.addFragment(new Lenses(), "العدسات");
        adapter.addFragment(new ContactLenses(), "العدسات الاصقة");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void getRegistrationToken () {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token = task.getResult().getToken();
                            FirebaseDatabase.getInstance()
                                    .getReference(Constans.ADMIN_TOKEN).child(token).setValue(token);
                        }else{
                            Log.d("ttt",task.getException().getMessage());
                        }
                    }
                });


    }
}