package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.Models.Course;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class pdfActivity extends AppCompatActivity {
    PDFView pdfView;
    String extractedText="";
    CircleImageView nextWord;
    ImageButton btn_search;
    ArrayList<Course> courseList;
    String materie;
    FirebaseDatabase firebaseDatabase;
    Integer j;
    List<Integer>aparitii= new ArrayList<>();
    DatabaseReference databaseReference;
    EditText searchPDF;
    List<Integer> listapagini= new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfactivity);
        pdfView= findViewById(R.id.pdfview);
        searchPDF=findViewById(R.id.searchInPDF);
        nextWord=findViewById(R.id.nextword);
        btn_search=findViewById(R.id.btn_searchinpdf);
        nextWord.setVisibility(View.GONE);

        String getItem=getIntent().getStringExtra("pdfFileName");
        String pdfURL=getIntent().getStringExtra("pdfURL");
        materie=getIntent().getStringExtra("materie");

        searchPDF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btn_search.setOnClickListener(v -> {
                    if (s.toString()==null || s.toString().trim().isEmpty()) {
                        Toast.makeText(pdfActivity.this,"Trebuie sa introduci un cuvant !",Toast.LENGTH_LONG).show();
                    }else {
                    if(listapagini.size()==0){
                        matchWord(s.toString().toLowerCase(),extractedText);
                    }else {
                        Toast.makeText(pdfActivity.this,"Trebuie sa parcurgi toate aparitiile cuvantului deja cautat pentru a cauta alt cuvant !",Toast.LENGTH_LONG).show();
                    }
                }});
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_search.setOnClickListener(v -> {
                    if (s.toString()==null || s.toString().trim().isEmpty()) {
                        Toast.makeText(pdfActivity.this, "Trebuie sa introduci un cuvant !", Toast.LENGTH_LONG).show();
                    } else {
                        if (listapagini.size() == 0) {
                            matchWord(s.toString().toLowerCase(), extractedText);
                        } else {
                            Toast.makeText(pdfActivity.this, "Trebuie sa parcurgi toate aparitiile cuvantului deja cautat pentru a cauta alt cuvant !", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {
                btn_search.setOnClickListener(v -> {
                    if (s.toString()==null || s.toString().trim().isEmpty()){
                            Toast.makeText(pdfActivity.this,"Trebuie sa introduci un cuvant !",Toast.LENGTH_LONG).show();
                    }else {
                    if(listapagini.size()==0){
                        matchWord(s.toString().toLowerCase(),extractedText);
                    }else {
                        Toast.makeText(pdfActivity.this,"Trebuie sa parcurgi toate aparitiile cuvantului deja cautat pentru a cauta alt cuvant !",Toast.LENGTH_LONG).show();
                    }
                }});
            }
        });

        courseList=new ArrayList<>();
        readAllCourses();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (Course curs : courseList){
                    String titluPDF=curs.getTitlu();
                    String pdfURL = curs.getPdfURL();
                    if (getItem.equals(titluPDF)){
                        extractTextPDF(pdfURL);
                        new RetrivedPdffromFirebase().execute(pdfURL);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    HashMap<Integer,String> map= new HashMap<>();
    private void extractTextPDF(String url){
        new Thread(() -> {
    try {
        PdfReader reader = new PdfReader(url);
        int n = reader.getNumberOfPages();
        for (int i = 0; i < n; i++) {
            String extractText=PdfTextExtractor.getTextFromPage(reader, i + 1).trim().toLowerCase();
            extractedText = extractedText + PdfTextExtractor.getTextFromPage(reader, i + 1).trim().toLowerCase() + "\n";
                map.put(i + 1, extractText);
    }
        reader.close();
    } catch (Exception e) {
        Log.d("error", String.valueOf(e));
    }
        }).start();
    }
    private void matchWord(String cuvant,String extractedText){
        nextWord.setVisibility(View.VISIBLE);
        if(extractedText.contains(cuvant)){
            for (Map.Entry<Integer, String> integerStringEntry : map.entrySet()) {
                String text = ((Map.Entry) integerStringEntry).getValue().toString();
                Integer pagina = (Integer) ((Map.Entry) integerStringEntry).getKey();
                if (text.contains(cuvant)) {
                    listapagini.add(pagina);
                    aparitii.add(pagina);
                }
            }
        }
        if (listapagini.size()==1){
            Toast.makeText(this, "Cuvantul cautat are o singura aparitie.",
                    Toast.LENGTH_LONG).show();
        }else {
        Toast.makeText(this, "Cuvantul cautat are "+ listapagini.size() + " aparitii",
                Toast.LENGTH_LONG).show();}
        nextWord.setOnClickListener(v -> {
            if(aparitii.size()!=0 && listapagini.size()!=0){
                j=0;
                pdfView.jumpTo(listapagini.get(j)-1);
                if(aparitii.contains(listapagini.get(j))){
                    aparitii.remove(listapagini.get(j));
                    listapagini.remove(listapagini.get(j));
                }
        }
        else {
            Toast.makeText(getApplicationContext(),"Cauta alt cuvant ! " , Toast.LENGTH_LONG).show();
            nextWord.setVisibility(View.GONE);
            }
        });
    }
    DatabaseReference reference;
    private void readAllCourses(){
        if(materie.equals("OOP")){
            reference= FirebaseDatabase.getInstance().getReference("Cursuri");}
        else{
            reference= FirebaseDatabase.getInstance().getReference("CursuriBD");
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    Course course = dataSnapshot1.getValue(Course.class);
                    if (course!=null){
                        courseList.add(course);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    class RetrivedPdffromFirebase extends AsyncTask<String,Void,InputStream>{
        @Override
        protected InputStream doInBackground(String... strings){
            InputStream pdfStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == 200) {
                    pdfStream = new BufferedInputStream(httpURLConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return pdfStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }

}
