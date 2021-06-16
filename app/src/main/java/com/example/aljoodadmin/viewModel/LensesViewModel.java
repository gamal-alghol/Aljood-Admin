package com.example.aljoodadmin.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.aljoodadmin.model.Lense;
import com.example.aljoodadmin.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LensesViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> headerMutibleLiveData;
    private MutableLiveData<HashMap<String, List<Lense>>> listDataChild2;
    List<String> listDataHeader;

    HashMap<String, List<Lense>> listHashMap2;

    public LensesViewModel(@NonNull Application application) {
        super(application);
        headerMutibleLiveData = new MutableLiveData<>();
        listDataChild2 = new MutableLiveData<>();
    }
    public MutableLiveData<List<String>> getHeder() {

        FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_Lenses)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                               if (task.isSuccessful()) {
                                                   listDataHeader  = new ArrayList<>();

                                                   for (QueryDocumentSnapshot queryDocumentSnapshots1 : task.getResult()) {
                                                       String id = queryDocumentSnapshots1.getId();
                                                       listDataHeader.add(id);
                                                   }
                                                   headerMutibleLiveData.setValue(listDataHeader);

                                               }
                                           }
                                       });
        return headerMutibleLiveData;
    }

    public MutableLiveData<HashMap<String, List<Lense>>> getAllSub() {

        listHashMap2 = new HashMap<>();

        FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_Lenses).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (int i = 0; i < task.getResult().size(); i++) {
                                final String id = task.getResult().getDocuments().get(i).getId();

                                FirebaseFirestore.getInstance()
                                        .collection(Constans.FIREBASE_DB_Lenses)
                                        .document(id)
                                        .collection("type")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    ArrayList<Lense> lenses = new ArrayList<>();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Lense lense = document.toObject(Lense.class);
                                                        lense.setId(document.getId());
                                                        lenses.add(lense);
                                                    }
                                                    listHashMap2.put(id, lenses);
                             }
                             }
                                        });
                            }
                            listDataChild2.setValue(listHashMap2);
                        }

                    }
                });
        return listDataChild2;
                        }

}
