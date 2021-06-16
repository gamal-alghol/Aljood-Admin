package com.example.aljoodadmin.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.aljoodadmin.model.Device;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DevicesViewModel extends AndroidViewModel {
    private MutableLiveData<FirestoreRecyclerOptions<Device>> DevicesLiveData;

    public DevicesViewModel(@NonNull Application application) {
        super(application);
        DevicesLiveData =new MutableLiveData<>();

    }
    public MutableLiveData<FirestoreRecyclerOptions<Device>> getNewsLiveData(String category) {

        Query query = FirebaseFirestore.getInstance()
                .collection(category).orderBy("name", Query.Direction.DESCENDING);
        DevicesLiveData.setValue(new FirestoreRecyclerOptions.Builder<Device>()
                .setQuery(query, Device.class)
                .build());
        return DevicesLiveData;
    }
}
