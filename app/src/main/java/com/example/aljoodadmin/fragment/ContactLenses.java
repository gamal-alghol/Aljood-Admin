package com.example.aljoodadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.aljoodadmin.R;
import com.example.aljoodadmin.adapter.ExpandableListAdapterContactLense;
import com.example.aljoodadmin.model.ContactLense;
import com.example.aljoodadmin.utils.Constans;
import com.example.aljoodadmin.view.AddContactLenses;
import com.example.aljoodadmin.viewModel.ContactLensesViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ContactLenses extends Fragment {
    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String, List<ContactLenses>> listDataChild;
    ContactLensesViewModel lensesViewModel;
    ProgressBar progressBar;
    FloatingActionButton addLensesBtn;
    View view;
    ExpandableListAdapterContactLense listAdapter;
    public ContactLenses() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreateView(inflater, container, savedInstanceState);
        view= inflater.inflate(R.layout.fragment_contact_lenses, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addLensesBtn=view.findViewById(R.id.add_lenses);
        expandableListView =view.findViewById(R.id.list_subjects);
        progressBar=view.findViewById(R.id.progressBar5);
        lensesViewModel= ViewModelProviders.of(this).get(ContactLensesViewModel.class);
        listDataHeader = new ArrayList<String>();
        listDataChild=new HashMap<>();
        progressBar.setVisibility(View.VISIBLE);
        creatRecycleView();
        addLensesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), AddContactLenses.class));
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        lensesViewModel = ViewModelProviders.of(this).get(ContactLensesViewModel.class);
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
        lensesViewModel.getAllSub().observe(getActivity(), new Observer<HashMap<String, List<ContactLense>>>() {
            @Override
            public void onChanged(HashMap<String, List<ContactLense>> stringListHashMap) {
                progressBar.setVisibility(View.INVISIBLE);
                creatAdapter(stringListHashMap);
            }
        });
    }
    private void updateRecycleView() {
        lensesViewModel.getAllSub().observe(getActivity(), new Observer<HashMap<String, List<ContactLense>>>() {
            @Override
            public void onChanged(HashMap<String, List<ContactLense>> stringListHashMap) {
                creatAdapter(stringListHashMap);
            }
        });
    }
    void creatAdapter(HashMap<String, List<ContactLense>> stringListHashMap) {
        listAdapter = new ExpandableListAdapterContactLense(getContext(), listDataHeader, stringListHashMap);
        expandableListView.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new ExpandableListAdapterContactLense.OnItemClickListener() {
            @Override
            public void onItemClicked(String document, String lenseId,int lenseSph, boolean isCheck) {
                if (lenseId.equals("brine")){
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseFirestore.getInstance()
                     .collection(Constans.FIREBASE_DB_contactLenses)
                     .document(lenseId).update(lenseSph+"",isCheck).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.INVISIBLE);

                                Toast.makeText(getContext(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                                updateRecycleView();
                            }
                            }
                            });
                    }else   {
                    progressBar.setVisibility(View.VISIBLE);

                    FirebaseFirestore.getInstance()
                        .collection(Constans.FIREBASE_DB_contactLenses)
                        .document(document)
                        .collection("products")
                        .document(lenseId)
                        .update("available", isCheck)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.INVISIBLE);

                                    Toast.makeText(getContext(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                                    updateRecycleView();
                                }
                            }
                        });
                }
            }
        });
    }




}