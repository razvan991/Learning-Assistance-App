package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.myapplication.Quizpage.listBD;

import com.example.myapplication.Models.Question;

public class QuizBD extends AppCompatActivity {
    int correctCount=0;
    int wrongCount=0;
    int index=0;
    Double time = 0.0;
    Timer timer;
    TimerTask timerTask;
    LinearLayout nextBTN,hintBTN;
    Question question;
    DrawerLayout drawerLayout;
    List<Question> listOfQuestions;
    TextView card_question,optiona,optionb,optionc,optiond;
    CardView cardOA,cardOB,cardOC,cardOD;
    TextView textViewCountDown;
    ImageView backToQuiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizbd);
        textViewCountDown=findViewById(R.id.text_view_countdown);
        findItemsByID();
        timer = new Timer();
        listOfQuestions=listBD;
        Collections.shuffle(listOfQuestions);
        question =listBD.get(index);
        nextBTN.setClickable(false);

        setAllData();
        hintBTN=findViewById(R.id.hintBTN);
        hintBTN.setOnClickListener(v -> {
            Context context=getApplicationContext();
            //verificat ce intrebare e si afisat hint-ul pt intrebarea respectiva
            String text= question.getHint();
            int duration=Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context,text,duration);
            toast.show();
        });
        cardOA.setOnClickListener(v -> {
            disableButton();
            nextBTN.setClickable(true);
            if(question.getoA().equals(question.getAnswer())){
                cardOA.setBackgroundColor(getResources().getColor(R.color.green));
                if(index< listBD.size()-1){
                    Correct(cardOA);
                }else{
                    gameWon();
                }
            }else {
                Wrong(cardOA);
            }
        });
        cardOB.setOnClickListener(v -> {
            disableButton();
            nextBTN.setClickable(true);
            if(question.getoB().equals(question.getAnswer())){
                cardOB.setBackgroundColor(getResources().getColor(R.color.green));
                if(index< listBD.size()-1){
                    Correct(cardOB);
                }else{
                    gameWon();
                }
            }else {
                Wrong(cardOB);
            }
        });
        cardOC.setOnClickListener(v -> {
            disableButton();
            nextBTN.setClickable(true);
            if(question.getoC().equals(question.getAnswer())){
                cardOC.setBackgroundColor(getResources().getColor(R.color.green));
                if(index< listBD.size()-1){
                    Correct(cardOC);
                }else{
                    gameWon();
                }
            }else {
                Wrong(cardOC);
            }
        });
        cardOD.setOnClickListener(v -> {
            disableButton();
            nextBTN.setClickable(true);
            if(question.getoD().equals(question.getAnswer())){
                cardOD.setBackgroundColor(getResources().getColor(R.color.green));
                if(index< listBD.size()-1){
                    Correct(cardOD);
                }else{
                    gameWon();
                }
            }else {
                Wrong(cardOD);
            }
        });
        backToQuiz.setOnClickListener(v -> {
            pauseCountDown();
            finish();
            Intent intent=new Intent(QuizBD.this,Quizpage.class);
            startActivity(intent);
        });
        startCountDown();
        drawerLayout=findViewById(R.id.drawer_layout);
    }
    private void startCountDown() {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(() -> {
                    time++;
                    textViewCountDown.setText(getTimerText());
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
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
    public void pauseCountDown(){
        timerTask.cancel();
    }

    //varianta raspuns corect
    public void Correct(CardView card){
        card.setBackgroundColor(getResources().getColor(R.color.green));
        nextBTN.setOnClickListener(v -> {
            nextBTN.setClickable(false);
            correctCount++;
            if(index < listBD.size()-1){
                index++;
                question = listBD.get(index);
                resetColor();
                setAllData();
                enableButton();
            }else{
                gameWon();
            }
        });
    }
    public void Wrong(CardView cardOA){
        cardOA.setBackgroundColor(getResources().getColor(R.color.red));
        nextBTN.setOnClickListener(v -> {
            nextBTN.setClickable(false);
            wrongCount++;
            if(index < listBD.size() - 1){
                index++;
                question = listBD.get(index);
                setAllData();
                resetColor();
                enableButton();
            }else {
                gameWon();
            }
        });
    }
    public void enableButton(){
        cardOA.setClickable(true);
        cardOB.setClickable(true);
        cardOC.setClickable(true);
        cardOD.setClickable(true);
    }
    public void disableButton(){
        cardOA.setClickable(false);
        cardOB.setClickable(false);
        cardOC.setClickable(false);
        cardOD.setClickable(false);
    }
    public void resetColor(){
        cardOA.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        cardOB.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        cardOC.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        cardOD.setBackgroundColor(getResources().getColor(R.color.colorWhite));
    }
    public void gameWon(){
        finish();
        Intent intent=new Intent(QuizBD.this,wonActivity.class);
        intent.putExtra("time",time);
        intent.putExtra("correct",correctCount);
        intent.putExtra("wrong",wrongCount);
        startActivity(intent);
    }
    public void findItemsByID(){
        card_question=findViewById(R.id.card_question);
        backToQuiz=findViewById(R.id.ic_back);
        optiona=findViewById(R.id.card_optiona);
        optionb=findViewById(R.id.card_optionb);
        optionc=findViewById(R.id.card_optionc);
        optiond=findViewById(R.id.card_optiond);

        cardOA=findViewById(R.id.CardA);
        cardOB=findViewById(R.id.CardB);
        cardOC=findViewById(R.id.CardC);
        cardOD=findViewById(R.id.CardD);

        nextBTN=findViewById(R.id.nextBTN);
        //hintBTN=(LinearLayout) findViewById(R.id.hintBTN);
    }
    private void setAllData(){
        card_question.setText(question.getQuestion());
        optiona.setText(question.getoA());
        optionb.setText(question.getoB());
        optionc.setText(question.getoC());
        optiond.setText(question.getoD());
    }
    @Override
    public void onPause(){
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}
