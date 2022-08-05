package com.example.myapplication.Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.ChangePasswordActivity;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    CircleImageView profileImage;
    TextView username,emailProfile;
    TextView updateProfilePic,deleteUser,changePass;
    DatabaseReference reference;
    TextView numarMatricol;
    FirebaseUser fuser;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private Uri imageUri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        changePass=view.findViewById(R.id.changePass);
        updateProfilePic=view.findViewById(R.id.updateProfilePic);
        deleteUser=view.findViewById(R.id.deleteUser);
        numarMatricol=view.findViewById(R.id.showNrMatricol);
        emailProfile=view.findViewById(R.id.emailProfile);
        profileImage=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.username);

        storageReference = FirebaseStorage.getInstance().getReference("users_photos");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        changePass.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        deleteUser.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Esti sigur ca vrei sa stergi utilizatorul ?");
            dialog.setMessage("Stergerea acestui utilizator implica eliminarea tuturor datelor " +
                    "salvate in baza de date si imposibilitatea de recuperare a lor.");
            dialog.setPositiveButton("Sterge", (dialog1, which) -> {
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "User deleted !", Toast.LENGTH_SHORT).show();
                                    Log.d("deleteUser", "User account deleted.");
                                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
            dialog.setNegativeButton("Anuleaza", (dialog12, which) -> dialog12.dismiss());
            AlertDialog alertDialog=dialog.create();
            alertDialog.show();
        });

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    emailProfile.setText(user.getEmail());
                }
                if (user != null) {
                    username.setText(user.getUsername());
                }
                if (user != null) {
                    numarMatricol.setText(user.getNumarMatricol());
                }

                if (user != null) {
                    if (user.getImageURL().equals("default")){
                        profileImage.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        if (getActivity() == null) {
                            return;
                        }
                        Glide.with(Objects.requireNonNull(getContext())).load(user.getImageURL()).into(profileImage);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        updateProfilePic.setOnClickListener(v -> openImage());
        return view;
    }

    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        @SuppressLint("UseRequireInsteadOfGet") ContentResolver contentResolver= Objects.requireNonNull(getContext()).getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();
        if(imageUri!=null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(task -> {
                if(!task.isSuccessful()){
                    throw Objects.requireNonNull(task.getException());
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Uri downloadUri=task.getResult();
                    String mUri=downloadUri.toString();

                    reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("imageURL", ""+mUri);
                    reference.updateChildren(map);
                    pd.dismiss();
                }else{
                    Toast.makeText(getContext(),"Failed !",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                pd.dismiss();
            });
        }else {
            Toast.makeText(getContext(),"No image selected !",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null
            && data.getData()!=null){
            imageUri=data.getData();
            if(uploadTask!=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }
}