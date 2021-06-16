package com.example.aljoodadmin.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.annotations.Color;
import com.example.aljoodadmin.annotations.ColorString;
import com.example.aljoodadmin.annotations.ContactLensesString;
import com.example.aljoodadmin.model.ContactLense;
import com.example.aljoodadmin.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class AddContactLenses extends AppCompatActivity {
    AutoCompleteTextView spnSph,spnColor;
    TextInputLayout spnSphTextInputLayout;
    ChipGroup chipGroupCategory,chipGroupDuration;
    Button chipGroupCategoryBtn,chipGroupDurationBtn,search;
    String groupCategoryString,groupDurationString=null;
    ArrayAdapter adapter;
    ArrayList<String> arrayListSph;
    ArrayList<String> arrayListColorCosmetic;
    ArrayList<String> arrayListColorMedcal;
    ProgressBar progressBarSearch;
    boolean isAvilable;
Switch aSwitch;
    float selectedSph;
    int  selectedColor;
    String selectedColorString;

    Chip chipChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_lenses);
        spnSph =findViewById(R.id.spn_sph);
        spnColor =findViewById(R.id.spn_color);
        chipGroupCategoryBtn =findViewById(R.id.btn_category);
        chipGroupCategory = findViewById(R.id.chip_group_category);
        chipGroupDurationBtn =findViewById(R.id.btn_duration);
        chipGroupDuration = findViewById(R.id.chip_group_duration);
        progressBarSearch=findViewById(R.id.progressBar_search);
        spnSphTextInputLayout=findViewById(R.id.textInputLayout_sph);
        search=findViewById(R.id.search);
aSwitch=findViewById(R.id.avilable);
        Listeners();
        creatSpinnerSph();
        creatSpinnerColor();
    }

    private void Listeners() {
        spnSph.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSph= Float.parseFloat((String) parent.getItemAtPosition(position)) ;
                // here is your selected item
            }
        });
        spnColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedColorString  = (String) parent.getItemAtPosition(position);
                switch (selectedColorString) {
                    case ColorString.transparent_lences :
                        selectedColor = Color.transparent_lences;
                        break;
                    case ColorString.grey :
                        selectedColor = Color.grey;
                        break;
                    case ColorString.sterling_grey :
                        selectedColor = Color.sterling_grey;
                        break;
                    case ColorString.blue :
                        selectedColor = Color.blue;
                        break;
                    case ColorString.green :
                        selectedColor = Color.green;
                        break;

                    case ColorString.brouwn :
                        selectedColor = Color.brouwn;
                        break;
                    case ColorString.amethyst :
                        selectedColor = Color.amethyst;
                        break;
                    case ColorString.gemstone_green :
                        selectedColor = Color.gemstone_green;
                        break;
                    case ColorString.turquoise :
                        selectedColor = Color.turquoise;
                        break;
                    case ColorString.honey :
                        selectedColor = Color.honey;
                        break;
                    case ColorString.pure_hazel :
                        selectedColor = Color.pure_hazel;
                        break;

                }
                spnColor.setTextColor(getResources().getColor(selectedColor));


            }
        });


        chipGroupCategory.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                chipChecked = findViewById(checkedId);

                if(chipChecked != null){
                    chipGroupCategoryBtn.setText(chipChecked.getText());
                    switch ((String) chipChecked.getText()) {
                        case ContactLensesString.medicalAr :
                            groupCategoryString = ContactLensesString.medical;
                            spnSphTextInputLayout.setVisibility(View.VISIBLE);
                            // adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListColorMedcal);
                            //spnColor.setAdapter(adapter);
                            break;
                        case ContactLensesString.cosmeticAr :
                            groupCategoryString = ContactLensesString.cosmetic;
                            spnSphTextInputLayout.setVisibility(View.INVISIBLE);
                            selectedSph=0;
                            //     adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListColorCosmetic);
                            //       spnColor.setAdapter(adapter);
                            break;
                    }
                }
                else {
                    chipGroupCategoryBtn.setText(AddContactLenses.this.getResources().getString(R.string.catygory));
                    groupCategoryString = null;
                }
            }
        });
        chipGroupDuration.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                chipChecked =findViewById(checkedId);

                if (chipChecked != null) {
                    chipGroupDurationBtn.setText(chipChecked.getText());
                    switch ((String) chipChecked.getText()) {
                        case ContactLensesString.monthlyAr :
                            groupDurationString = ContactLensesString.monthly;
                            break;
                        case ContactLensesString
                                .yearlyAr :
                            groupDurationString = ContactLensesString.yearly;
                            break;
                    }
                } else {
                    chipGroupDurationBtn.setText(getResources().getString(R.string.duration));
                    groupDurationString = null;
                }
            }
        });
        chipGroupCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chipGroupCategory.getVisibility()==View.GONE){
                    chipGroupCategory.setVisibility(View.VISIBLE);
                    chipGroupCategoryBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_bottom, 0, 0, 0);
                }else if(chipGroupCategory.getVisibility()==View.VISIBLE){
                    chipGroupCategoryBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_top, 0,  0, 0);
                    chipGroupCategory.setVisibility(View.GONE);

                }
            }
        });

        chipGroupDurationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chipGroupDuration.getVisibility()==View.GONE){
                    chipGroupDuration.setVisibility(View.VISIBLE);
                    chipGroupDurationBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_bottom, 0, 0, 0);
                }else if(chipGroupDuration.getVisibility()==View.VISIBLE){
                    chipGroupDurationBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_top, 0,  0, 0);
                    chipGroupDuration.setVisibility(View.GONE);

                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSearch.setVisibility(View.VISIBLE);
                if (groupCategoryString != null&& groupDurationString!=null){
                    getLense();

                }else {
                    progressBarSearch.setVisibility(View.INVISIBLE);

                    Toast.makeText(AddContactLenses.this,"يرجى اختيار فئة و مدة العدسة",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void    getLense() {
        DocumentReference documentReference =  FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_contactLenses)
                .document(groupCategoryString+"-"+groupDurationString)
                .collection("products").document();

        ContactLense contactLense=new ContactLense(documentReference.getId(),selectedSph,aSwitch.isChecked(),selectedColorString);
        documentReference.set(contactLense).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                                progressBarSearch.setVisibility(View.INVISIBLE);
                                Constans.bDiloge(AddContactLenses.this, "اضافة عدسة", "تم اضاغة عدسة بنجاج", R.drawable.ic_baseline_check_circle_24);
                            }else {
                            progressBarSearch.setVisibility(View.INVISIBLE);
                            Constans.bDiloge(AddContactLenses.this,"اضافة عدسة","فشل اضافة",R.drawable.ic_cross);

                        }

                    }
                });



    }

    private void creatSpinnerSph() {
        arrayListSph=new ArrayList<>();

        for(float i =20;i>=-20;i= (float) (i-0.25)){
            String result;
            if(i==0) {
                result = String.format(Locale.US, "%.2f", i);
            }else {
                result = String.format(Locale.US, "%+.2f", i);

            }

            arrayListSph.add(result);
        }
        adapter = new ArrayAdapter(this, R.layout.txt_spinner, arrayListSph);
        spnSph.setAdapter(adapter);
    }
    private void creatSpinnerColor() {
        arrayListColorCosmetic = new ArrayList<>();
        arrayListColorCosmetic.add(ColorString.grey);
        arrayListColorCosmetic.add(ColorString.blue);
        arrayListColorCosmetic.add(ColorString.brouwn);
        arrayListColorCosmetic.add(ColorString.green);
        arrayListColorCosmetic.add(ColorString.gemstone_green);
        arrayListColorCosmetic.add(ColorString.honey);
        arrayListColorCosmetic.add(ColorString.sterling_grey);
        arrayListColorCosmetic.add(ColorString.pure_hazel);
        arrayListColorCosmetic.add(ColorString.turquoise);
        arrayListColorCosmetic.add(ColorString.amethyst);
        adapter = new ArrayAdapter(this, R.layout.txt_spinner, arrayListColorCosmetic);
        spnColor.setAdapter(adapter);
    }
    }