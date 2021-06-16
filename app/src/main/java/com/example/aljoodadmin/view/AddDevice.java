package com.example.aljoodadmin.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.aljoodadmin.R;
import com.example.aljoodadmin.model.Device;
import com.example.aljoodadmin.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class AddDevice extends AppCompatActivity {
EditText name , discroption;
    SwitchMaterial isAvailable,isPromotion;
LinearLayout btnImage;
TextView txtImages,title;
    Button addDevice;
Device device,beforDevice;
    ArrayList<Uri> uriArrayList = new ArrayList<>();
    ArrayList<String> photoStringList = new ArrayList<>();
    ProgressBar progressBar;
    private static final int REQUEST_PICK_PHOTO = 100;
    private Uri imageUri;
    ViewPager vp_images;
    String photoUrl;
    String category;
    boolean available,promotion,isImageUPdate=false;
    PageIndicatorView mPageIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        title=findViewById(R.id.textView5);
        name=findViewById(R.id.name);
        discroption=findViewById(R.id.discroption);
        isAvailable=findViewById(R.id.isAvailable);
        isPromotion=findViewById(R.id.isPromotion);
        btnImage =findViewById(R.id.ln_images);
        vp_images=findViewById(R.id.vp_images);
         addDevice=findViewById(R.id.add_device);
         txtImages=findViewById(R.id.txt_images);
progressBar=findViewById(R.id.progressBar2);
category=getIntent().getStringExtra("category");
if (category.equals(Constans.FIREBASE_DB_Accessories)){
    title.setText("الاكسسوارات");
}
        mPageIndicatorView=findViewById(R.id.mPageIndicatorView);

        btnImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectPhoto();
            }
        });
        if(getIntent().hasExtra("id")){
            UpdateDevice();
        }else {
            addDevice();
        }


isAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        available=isChecked;
    }
});
        isPromotion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                promotion=isChecked;
            }
        });
    }

    private void UpdateDevice() {
        addDevice.setText("تعديل");
        txtImages.setText("تغيير الصور");

        getDeviceFromFireBase();
        upDataDeviceInFireBase();
    }

    private void upDataDeviceInFireBase() {
addDevice.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(isImageUPdate){
            uploadImage();
        }else{
            UpdateObjectDevice(beforDevice.images,beforDevice.devicesId);
        }
    }
});
    }

    private void getDeviceFromFireBase() {
        FirebaseFirestore.getInstance().collection(category)
                .document(getIntent().getStringExtra("id")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    beforDevice=document.toObject(Device.class);
                    name.setText(beforDevice.getName());
                    discroption.setText(beforDevice.getDescribe());
                    isAvailable.setChecked(beforDevice.isAvailable());
                    isPromotion.setChecked(beforDevice.isPromotion());
                    for(int i=0;i<beforDevice.getImages().size();i++){
                        uriArrayList.add(Uri.parse(beforDevice.getImages().get(i)));
                    }
                    DeviceImagesAdapter playgroundImagesAdapter = new DeviceImagesAdapter(getApplicationContext(),uriArrayList);
                    vp_images.setAdapter(playgroundImagesAdapter);
                    mPageIndicatorView.setViewPager(vp_images);
                }
            }
        });
    }

    private void addDevice() {

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValid(name)&&uriArrayList.size()>=1){
                    if (imageUri != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadImage();
                            }
                        });
                    }
                }
            }
        });
    }


    void uploadImage() {
        progressBar.setVisibility(View.VISIBLE);
        photoStringList.clear();
        addDevice.setEnabled(false);
        for (int uploadCount = 0;uploadCount < uriArrayList.size(); uploadCount++) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("deviceImages")
                    .child(System.currentTimeMillis() + "." + getFileExtension(uriArrayList.get(uploadCount)));
            final int finalUploadCount = uploadCount;
            storageReference.putFile(uriArrayList.get(uploadCount)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("ttt", "Image uploader Done ");

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoUrl = uri.toString();
                            photoStringList.add(photoUrl);
                            device = new Device();
                            if (finalUploadCount ==uriArrayList.size()-1){

     if (isImageUPdate){
         UpdateObjectDevice(photoStringList,beforDevice.devicesId);
}else if(!isImageUPdate){
    addObjectDevice(photoStringList);
}
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Constans.bDiloge(AddDevice.this, "اضافة جهاز", "فشل رفع الصورة", R.drawable.ic_cross);
                            progressBar.setVisibility(View.GONE);
                            addDevice.setEnabled(true);

                            Log.d("ttt", "Error in get image ...=> " + e.getMessage());
                        }
                    })

                    ;
                }
            });

        }
    }
    void UpdateObjectDevice(List<String> images,String id) {

        DocumentReference firebaseFirestore=FirebaseFirestore.getInstance().collection(category).document(id);

        device=new Device(id
                ,name.getText().toString()
                ,discroption.getText().toString()
                ,images
                , available
                ,promotion);

        firebaseFirestore.set(device)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            imageUri=null;
                            Constans.bDiloge(AddDevice.this, "تعديل جهاز", "تم تعديل جهاز بنجاح", R.drawable.ic_baseline_check_circle_24);
                            addDevice.setEnabled(true);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            addDevice.setEnabled(true);

                            Constans.bDiloge(AddDevice.this, "تعديل جهاز", "فشل تعديل جهاز ", R.drawable.ic_cross);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                addDevice.setEnabled(true);

                Constans.bDiloge(AddDevice.this, "تعديل جهاز", "فشل تعديل جهاز ", R.drawable.ic_cross);

                Log.d("ttt", "Error in add news ...=> " + e.getMessage());

            }
        });
    }
    void addObjectDevice(List<String> images) {

DocumentReference firebaseFirestore=FirebaseFirestore.getInstance().collection(category).document();

device=new Device(firebaseFirestore.getId()
        ,name.getText().toString()
        ,discroption.getText().toString()
        ,images
        , available
        ,promotion);

        firebaseFirestore.set(device)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            imageUri=null;
                            Constans.bDiloge(AddDevice.this, "اضافة جهاز", "تم اضافة جهاز بنجاح", R.drawable.ic_baseline_check_circle_24);
                            addDevice.setEnabled(true);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            addDevice.setEnabled(true);

                            Constans.bDiloge(AddDevice.this, "اضافة جهاز", "فشل اضافة جهاز ", R.drawable.ic_cross);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                addDevice.setEnabled(true);

                Constans.bDiloge(AddDevice.this, "اضافة جهاز", "فشل اضافة جهاز ", R.drawable.ic_cross);

                Log.d("ttt", "Error in add news ...=> " + e.getMessage());

            }
        });
    }
    private String getFileExtension(Uri itemUri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getApplicationContext().getContentResolver().getType(itemUri));
    }
    public void selectPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK) {
          Log.d("ttt","REQUEST_PICK_PHOTO");
            if (data.getClipData() != null) {
                uriArrayList.clear();

                int countClipData = data.getClipData().getItemCount();
                int currentImageSelect = 0;
                while (currentImageSelect < countClipData) {
                    imageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                    uriArrayList.add(imageUri);
                    currentImageSelect += 1;

                }
                DeviceImagesAdapter playgroundImagesAdapter = new DeviceImagesAdapter(getApplicationContext(), uriArrayList);
                vp_images.setAdapter(playgroundImagesAdapter);
                if (!getIntent().hasExtra("add")){
isImageUPdate=true;
                }
                mPageIndicatorView.setViewPager(vp_images);
                Toast.makeText(getApplicationContext(),"لقد قمت بتحديد "+uriArrayList.size()+"صورة ",Toast.LENGTH_SHORT).show();


            }


            if (data.getData() != null) {

                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri.toString().contains("image")) {
                    //handle image
                    imageUri = data.getData();
                    if (imageUri != null) {
                        uriArrayList.clear();
                        uriArrayList.add(imageUri);
                     //   userImage.setVisibility(View.VISIBLE);
                        DeviceImagesAdapter playgroundImagesAdapter = new DeviceImagesAdapter(getApplicationContext(), uriArrayList);
                        vp_images.setAdapter(playgroundImagesAdapter);
                        mPageIndicatorView.setViewPager(vp_images);
                        Toast.makeText(getApplicationContext(),"لقد قمت بتحديد "+uriArrayList.size()+"صورة ",Toast.LENGTH_SHORT).show();


                        if (!getIntent().hasExtra("add")){
                            isImageUPdate=true;
                        }
                    }}}}}

    private class DeviceImagesAdapter extends PagerAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<Uri> mImagesUrls;


        DeviceImagesAdapter(Context context, List<Uri> urls) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mImagesUrls = urls;
        }



        public void clear() {
            mImagesUrls.clear();
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return mImagesUrls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.devices_image, container, false);

            final ImageView img_View = itemView.findViewById(R.id.img_image);

            Glide.with(mContext).load(mImagesUrls.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(R.drawable.logo_jood)
                    .into(img_View);



            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
    private boolean inputValid(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(),"يرجى اضافة"+editText.getHint(),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}