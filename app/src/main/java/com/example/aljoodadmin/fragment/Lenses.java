package com.example.aljoodadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.adapter.ExpandableListAdapterLense;
import com.example.aljoodadmin.model.Lense;
import com.example.aljoodadmin.utils.Constans;
import com.example.aljoodadmin.view.AddLenses;
import com.example.aljoodadmin.viewModel.LensesViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Lenses extends Fragment {
    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String, List<Lense>> listDataChild;
LensesViewModel lensesViewModel;
    ProgressBar progressBar;
    FloatingActionButton addLensesBtn;
View view;
    ExpandableListAdapterLense listAdapter;
    public Lenses() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

super.onCreateView(inflater, container, savedInstanceState);
view= inflater.inflate(R.layout.fragment_lenses, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addLensesBtn=view.findViewById(R.id.add_lenses);
        expandableListView =view.findViewById(R.id.list_subjects);
        progressBar=view.findViewById(R.id.progressBar5);
        lensesViewModel= ViewModelProviders.of(this).get(LensesViewModel.class);

        listDataHeader = new ArrayList<String>();
        listDataChild=new HashMap<>();
        progressBar.setVisibility(View.VISIBLE);
        creatRecycleView();
        addLensesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), AddLenses.class)
                        .putStringArrayListExtra("array", (ArrayList<String>) listDataHeader));
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        lensesViewModel = ViewModelProviders.of(this).get(LensesViewModel.class);

    }

    @Override
    public void onPause() {
        super.onPause();
        lensesViewModel.getHeder().removeObservers(this);
        lensesViewModel.getAllSub().removeObservers(this);


    }

    private void creatRecycleView() {

        lensesViewModel.getHeder().observe(getActivity(), new Observer<List<String>>() {
    @Override
    public void onChanged(List<String> strings) {
        progressBar.setVisibility(View.INVISIBLE);
        listDataHeader.clear();
        listDataHeader.addAll(strings);
    }

});
        lensesViewModel.getAllSub().observe(getActivity(), new Observer<HashMap<String, List<Lense>>>() {
            @Override
            public void onChanged(HashMap<String, List<Lense>> stringListHashMap) {
                progressBar.setVisibility(View.INVISIBLE);
creatAdapter(stringListHashMap);

            }
        });
    }
    private void updateRecycleView() {


        lensesViewModel.getAllSub().observe(getActivity(), new Observer<HashMap<String, List<Lense>>>() {
            @Override
            public void onChanged(HashMap<String, List<Lense>> stringListHashMap) {
                progressBar.setVisibility(View.INVISIBLE);
                creatAdapter(stringListHashMap);
            }
        });
    }
void creatAdapter(HashMap<String, List<Lense>> stringListHashMap){
    listAdapter = new ExpandableListAdapterLense(getContext(), listDataHeader, stringListHashMap);
    expandableListView.setAdapter(listAdapter);
    expandableListView.setVisibility(View.VISIBLE);
    listAdapter.setOnItemClickListener(new ExpandableListAdapterLense.OnItemClickListener() {
        @Override
        public void onItemClicked(String document, String lenseId, boolean isCheck) {
            FirebaseFirestore.getInstance()
                    .collection(Constans.FIREBASE_DB_Lenses)
                    .document(document)
                    .collection("type")
                    .document(lenseId).update("available",isCheck)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                        updateRecycleView();
                    }
                }
            });
        }
    });
}

}
/*
        FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_Lenses)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for( QueryDocumentSnapshot queryDocumentSnapshots1 :task.getResult()){
                                String id = queryDocumentSnapshots1.getId();
                                listDataHeader.add(id);
                            }
                            FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_Lenses).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
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
                                                                    if(task.isSuccessful()){
                                                                        ArrayList<Lense> lenses = new ArrayList<>();
                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                            Lense lense = document.toObject(Lense.class);
                                                                            lense.setId(document.getId());
                                                                            lenses.add(lense);

                                                                        }
                                                                        listDataChild.put(id,lenses);

                                                                    }
                                                                }
                                                            });

                                                }
                                                ExpandableListAdapter2 listAdapter = new ExpandableListAdapter2(getContext(), listDataHeader, listDataChild);
                                                // setting list adapter
                                                progressBar.setVisibility(View.INVISIBLE);
                                                expandableListView.setAdapter(listAdapter);
                                                expandableListView.setVisibility(View.VISIBLE);
                                            }

                                        }

                                    });
                        }

                    }

                });
*/