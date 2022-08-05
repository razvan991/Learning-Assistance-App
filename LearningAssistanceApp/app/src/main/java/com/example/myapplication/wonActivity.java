package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.HashMap;

public class wonActivity extends AppCompatActivity {
    CircularProgressBar circularProgressBar;
    TextView resultText;
    DatabaseReference reference;
    ImageView back;
    int correct,wrong;
    Double time = 0.0;
    FirebaseUser firebaseUser;
    TextView timpScurs;
    LinearLayout btnShare;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        time=getIntent().getDoubleExtra("time",time);
        correct=getIntent().getIntExtra("correct",0);
        wrong=getIntent().getIntExtra("wrong",0);


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        timpScurs=findViewById(R.id.timpScurs2);
        circularProgressBar=findViewById(R.id.circularProgressBar);
        back=findViewById(R.id.ic_back);
        resultText=findViewById(R.id.resultText);
        btnShare=findViewById(R.id.btnShare);

        back.setOnClickListener(v -> finish());

        circularProgressBar.setProgress(correct);
        resultText.setText(correct + "/10");
        timpScurs.setText(getTimerText());

        btnShare.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "FII Application");
                String shareMessage= "\nI got "+ correct + " Out of 10 ";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        });
    }
    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }
    @SuppressLint("DefaultLocale")
    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
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
    protected void onPause(){
        super.onPause();
        status("offline");
    }

}
