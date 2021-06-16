package com.example.aljoodadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aljoodadmin.R;
import com.example.aljoodadmin.adapter.DeviceAdapter;
import com.example.aljoodadmin.model.Device;
import com.example.aljoodadmin.view.AddDevice;
import com.example.aljoodadmin.view.AddUser;
import com.example.aljoodadmin.viewModel.DevicesViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Devices extends Fragment {

    RecyclerView devicesRecycler;
    DeviceAdapter deviceAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    DevicesViewModel devicesViewModel;
    FloatingActionButton addDevice;

    String category;
public Devices(String category){
this.category=category;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_devices, container, false);

        devicesRecycler =view.findViewById(R.id.devices);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        addDevice=view.findViewById(R.id.add_user);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goToAddDevices();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (deviceAdapter !=null)
            deviceAdapter.startListening();
    }
    @Override
    public void onResume() {
        super.onResume();
      devicesViewModel = ViewModelProviders.of(this).get(DevicesViewModel.class);

        createRecyclerViewMajors();
    }
    @Override
    public void onPause() {
        super.onPause();
        devicesViewModel.getNewsLiveData(category).removeObservers(this);
    }
    private void goToAddDevices() {
        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddDevice.class).putExtra("category",category)
                        .putExtra("add",1));
            }
        });
    }
    private void createRecyclerViewMajors() {

       shimmerFrameLayout.setVisibility(View.VISIBLE);
        devicesRecycler.setLayoutManager(getLinearManger(LinearLayoutManager.VERTICAL));
     devicesViewModel.getNewsLiveData(category).observe(getActivity(), new Observer<FirestoreRecyclerOptions<Device>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Device> deviceFirestoreRecyclerOptions) {
                deviceAdapter = new DeviceAdapter(deviceFirestoreRecyclerOptions,getContext(),getActivity().getSupportFragmentManager(),category);
                devicesRecycler.setAdapter(deviceAdapter);
                deviceAdapter.startListening();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                devicesRecycler.setVisibility(View.VISIBLE);
            }


        });

    }
    private LinearLayoutManager getLinearManger(int orientation) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(orientation);
        return llm;
    }
}