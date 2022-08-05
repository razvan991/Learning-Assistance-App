package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.Models.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CursBD extends AppCompatActivity {
    ListView pdfListView;
    DrawerLayout drawerLayout;
    String materie;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    ArrayList<Course> searchedCourse;
    EditText searchCourse;
    ImageView burgerMenu;
    ArrayList<Course> courseListBD = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursbd);
        materie="BD";
        burgerMenu=findViewById(R.id.clickBurgerMenu);
        searchCourse=findViewById(R.id.search_course);
        pdfListView= findViewById(R.id.listview);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        burgerMenu.setOnClickListener(v -> MainActivity.openDrawer(drawerLayout));
        getAllCourses();

        searchCourse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable searchCurs) {
                if(searchCurs.toString().isEmpty()){
                    searchedCourse.clear();
                    courseListBD.clear();
                    getAllCourses();
                }else{
                    searchCourse(searchCurs.toString().toLowerCase());}
            }
        });
        pdfListView.setOnItemClickListener((adapterView, view, i, id) -> {
            Course item= (Course) pdfListView.getItemAtPosition(i);
            //in item am cursul selectat
            Intent start=new Intent(getApplicationContext(),pdfActivity.class);
            start.putExtra("pdfFileName",item.getTitlu());
            start.putExtra("pdfURL",item.getPdfURL());
            start.putExtra("materie",materie);
            startActivity(start);
        });
        drawerLayout=findViewById(R.id.drawer_layout);
    }
    // Cautarea cursului dupa keywords salvate in database
    private void searchCourse(String toString) {
        searchedCourse=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CursuriBD");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Course curs = snapshot1.getValue(Course.class);
                    if (curs != null && curs.getKeywords().contains(toString)) {
                        searchedCourse.add(curs);
                    }
                }
                CursAdapter cursAdapter = new CursAdapter(searchedCourse,getApplicationContext(),"BD");
                pdfListView.setAdapter(cursAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getAllCourses() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CursuriBD");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //courseList.clear();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    Course course = dataSnapshot1.getValue(Course.class);
                    if (course != null && course.getTitlu() != null && course.getPdfURL() != null) {
                        courseListBD.add(course);
                        Collections.sort(courseListBD, (o1, o2) -> {
                            Integer pdfID = Integer.parseInt(o1.getPdfID());
                            Integer pdfID2 = Integer.parseInt(o2.getPdfID());
                            return pdfID - pdfID2;
                        });
                    }
                }

                pdfListView.setAdapter(new CursAdapter(courseListBD,CursBD.this,"BD"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage()); //Don't ignore errors!
            }
        });
    }
    @Override
    public void onPause(){
        super.onPause();
        status("offline");
        MainActivity.closeDrawer(drawerLayout);
    }
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }
}
