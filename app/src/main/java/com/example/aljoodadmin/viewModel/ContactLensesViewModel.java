package com.example.aljoodadmin.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.annotations.ColorString;
import com.example.aljoodadmin.model.ContactLense;
import com.example.aljoodadmin.model.Lense;
import com.example.aljoodadmin.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactLensesViewModel  extends AndroidViewModel {
    private MutableLiveData<List<String>> headerMutibleLiveData;
    private MutableLiveData<HashMap<String, List<ContactLense>>> listDataChild2;
    List<String> listDataHeader;



    HashMap<String, List<ContactLense>> listHashMap2;
    public ContactLensesViewModel(@NonNull Application application) {
        super(application);
        headerMutibleLiveData = new MutableLiveData<>();
        listDataChild2 = new MutableLiveData<>();
    }
    public MutableLiveData<List<String>> getHeder() {

        FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_contactLenses)
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
    public MutableLiveData<HashMap<String, List<ContactLense>>> getAllSub() {

        listHashMap2 = new HashMap<>();

        FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_contactLenses).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (int i = 0; i < task.getResult().size(); i++) {
                                final String id = task.getResult().getDocuments().get(i).getId();
if(id.equals("brine")){
    final ArrayList<ContactLense>  lensesb = new ArrayList<>();

    FirebaseFirestore.getInstance()
            .collection(Constans.FIREBASE_DB_contactLenses)
            .document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            DocumentSnapshot is=  task.getResult();
            ContactLense lense=new ContactLense();
            lense.setId("brine");
            lense.setAvailable((Boolean) is.get("120"));
            lense.setSph((float) 120);
            lense.setColor("");
            lensesb.add(lense);
            ContactLense lense1=new ContactLense();
            lense1.setId("brine");
            lense1.setAvailable((Boolean) is.get("60"));
            lense1.setSph((float) 60);
            lense1.setColor("");
            lensesb.add(lense1);
    listHashMap2.put(id, lensesb);
        }
    });
}else{

    FirebaseFirestore.getInstance()
            .collection(Constans.FIREBASE_DB_contactLenses)
            .document(id)
            .collection("products")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<ContactLense>  lenses = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ContactLense lense = document.toObject(ContactLense.class);

                            lense.setId(document.getId());
                            lenses.add(lense);
                        }
                        listHashMap2.put(id, lenses);
                    }
                }
            });
}



                            }
                            listDataChild2.setValue(listHashMap2);
                        }

                    }
                });
        return listDataChild2;
    }

}
