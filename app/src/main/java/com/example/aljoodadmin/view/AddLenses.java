package com.example.aljoodadmin.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.model.Lense;
import com.example.aljoodadmin.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class AddLenses extends AppCompatActivity {
    ChipGroup chipGroupCategory;
    Button chipGroupCategoryBtn,add;
    AutoCompleteTextView spnSph,spnCyl;
    Chip chip;
    ArrayAdapter adapter;
    ArrayList<String> arrayListSph,arrayListCyl,arrayCatygory;
    DocumentReference documentReference;
    float selectedCyl,selectedSph;
    TextView isAvilableTxtView;
    String groupCategoryString=null;
    SwitchMaterial isAvilable;
    ProgressBar progressBarCategory,progressBarSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lenses);
        chipGroupCategoryBtn =findViewById(R.id.btn_category);
        chipGroupCategory = findViewById(R.id.chip_group_category);
        spnSph =findViewById(R.id.spn_sph);
        spnCyl =findViewById(R.id.spn_cyl);
        progressBarSearch=findViewById(R.id.progressBar_search);
        progressBarCategory=findViewById(R.id.progressBar_Catygory);
        add=findViewById(R.id.add_lense);
isAvilable=findViewById(R.id.isAvailable);
        getCategory();
        creatSpinnerSph();
        creatSpinnerCyl();
        Listeners();

    }

    private void Listeners() {
        chipGroupCategory.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip=group.findViewById(checkedId);
                if (chip!=null) {
                    chipGroupCategoryBtn.setText(chip.getText());
                    groupCategoryString= (String) chip.getText();
                }else{
                    chipGroupCategoryBtn.setText(getResources().getString(R.string.catygory));
                    groupCategoryString=null;
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


        spnSph.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSph= Float.parseFloat((String) parent.getItemAtPosition(position)) ;
                // here is your selected item
            }
        });
        spnCyl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCyl= Float.parseFloat((String) parent.getItemAtPosition(position)) ;


            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSearch.setVisibility(View.VISIBLE);
                if (groupCategoryString != null&&(selectedCyl>=-20||selectedCyl<=20)&&(selectedSph>=-20||selectedSph<=20)){
                   setLense();

                }else {
                    progressBarSearch.setVisibility(View.INVISIBLE);

                    Toast.makeText(getApplicationContext(),"يرجى ملئ جميع الحقول المطلوبة",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setLense() {
        progressBarSearch.setVisibility(View.VISIBLE);

       documentReference= FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_Lenses)
                .document(groupCategoryString)
                .collection("type")
                .document();
        Lense lense=new Lense(documentReference.getId(),selectedSph,selectedCyl,isAvilable.isChecked());
        documentReference.set(lense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if(task.isSuccessful()){
               Constans.bDiloge(AddLenses.this, "اضافة عدسة", "تم اضاغة عدسة بنجاج", R.drawable.ic_baseline_check_circle_24);
               progressBarSearch.setVisibility(View.INVISIBLE);
                }else {
               progressBarSearch.setVisibility(View.INVISIBLE);
               Constans.bDiloge(AddLenses.this,"اضافة عدسة","فشل اضافة",R.drawable.ic_cross);
           }
            }
        });
    }
    private void creatSpinnerCyl() {
        arrayListCyl=new ArrayList<>();
        for(float i =6;i>=-6;i= (float) (i-0.25)){
            String result;
            if(i==0) {
                 result = String.format(Locale.US, "%.2f", i);
            }else {
                result = String.format(Locale.US, "%+.2f", i);

            }

            arrayListCyl.add(result);
        }
        adapter = new ArrayAdapter(getApplicationContext(), R.layout.txt_spinner, arrayListCyl);
        spnCyl.setAdapter(adapter);
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
        adapter = new ArrayAdapter(getApplicationContext(), R.layout.txt_spinner, arrayListSph);
        spnSph.setAdapter(adapter);
    }

    private void getCategory() {
arrayCatygory=new ArrayList<>();
if(getIntent().hasExtra("array")){
    arrayCatygory=getIntent().getStringArrayListExtra("array");
    if(arrayCatygory!=null){
        for(int i=0;i<arrayCatygory.size();i++){
            creatChip(arrayCatygory.get(i));

        }
        progressBarCategory.setVisibility(View.GONE);
    }
}

    }
    private void creatChip(String id) {
        chip = new Chip(AddLenses.this);
        chip.setText(id);
        chip.setId(ViewCompat.generateViewId());
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setTextSize(20);
        chip.setCheckable(true);
        chip.setClickable(true);
        chip.setChipStrokeWidth(4);
        chip.setChipCornerRadius((float) 10);
        chip.setCheckedIconVisible(false);
        chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected);
        chip.setChipStrokeColorResource(R.color.bg_chip_state_list);
        chipGroupCategory.addView(chip);
    }
}