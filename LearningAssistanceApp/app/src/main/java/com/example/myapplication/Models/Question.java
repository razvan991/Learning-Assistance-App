package com.example.myapplication.Models;

public class Question {
    String Question;
    String oA;
    String oB;
    String oC;
    String oD;
    String Answer;
    String Hint;

    public Question(String question, String oA, String oB, String oC, String oD, String answer, String hint) {
        Question = question;
        this.oA = oA;
        this.oB = oB;
        this.oC = oC;
        this.oD = oD;
        this.Answer = answer;
        this.Hint=hint;
    }
    public Question(){

    }

    public String getQuestion() {
        return Question;
    }
    public String getoA() {
        return oA;
    }
    public String getoB() {
        return oB;
    }
    public String getoC() {
        return oC;
    }
    public String getoD() {
        return oD;
    }
    public String getAnswer() {
        return Answer;
    }
    public String getHint(){return Hint;}

    public void setQuestion(String question) {
        Question = question;
    }
    public void setoA(String oA) {
        this.oA = oA;
    }
    public void setoB(String oB) {
        this.oB = oB;
    }
    public void setoC(String oC) {
        this.oC = oC;
    }
    public void setoD(String oD) {
        this.oD = oD;
    }
    public void setAnswer(String answer) {
        Answer = answer;
    }
    public void setHint(String hint){this.Hint=hint;}

}
