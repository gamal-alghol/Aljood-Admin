package com.example.aljoodadmin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.adapter.DeviceAdapter;
import com.example.aljoodadmin.adapter.UserAdabter;
import com.example.aljoodadmin.model.User;
import com.example.aljoodadmin.utils.Constans;
import com.example.aljoodadmin.view.AddUser;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Users extends Fragment {
    RecyclerView  usersRecycler;
    UserAdabter userAdabter;
    ShimmerFrameLayout shimmerFrameLayout;
    DatabaseReference databaseReference;
    ArrayList<User> userArrayList;
    ValueEventListener  valueEventListener;
    FloatingActionButton addUser;

    public Users() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user, container, false);

return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        addUser=view.findViewById(R.id.add_user);
        usersRecycler =view.findViewById(R.id.users);

        userArrayList=new ArrayList<>();
        goToAddUser();
    }

    private void goToAddUser() {
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddUser.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        createRecyclerView();

    }

    private void createRecyclerView() {
     shimmerFrameLayout.setVisibility(View.VISIBLE);
userArrayList.clear();
        databaseReference  = FirebaseDatabase.getInstance().getReference(Constans.FIREBASE_DB_USERS_TABLE);
        valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    User user=dataSnapshot1.getValue(User.class);

                    userArrayList.add(user);
                }
                shimmerFrameLayout.setVisibility(View.GONE);
                usersRecycler.setLayoutManager(getLinearManger(LinearLayoutManager.VERTICAL));
                userAdabter = new UserAdabter(getContext(),userArrayList);
                usersRecycler.setAdapter(userAdabter);
                userAdabter.setOnItemClickListener(new UserAdabter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(String firebaseId) {
                        startActivity(new Intent(getContext(),AddUser.class).putExtra("id",firebaseId));
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private LinearLayoutManager getLinearManger(int orientation) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(orientation);
        return llm;
    }
}