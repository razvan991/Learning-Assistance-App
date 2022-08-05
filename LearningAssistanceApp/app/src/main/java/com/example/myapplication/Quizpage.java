package com.example.myapplication;

import static com.example.myapplication.MainActivity.closeDrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Models.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Quizpage extends AppCompatActivity {
    DrawerLayout drawerLayout;
    public static ArrayList<Question> listBD;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    TextView quizoop,quizbd;
    LinearLayout homeButton,chatButton,courseButton,logoutButton,quizButton;
    ImageView logoButton;
    public static ArrayList<Question> listOOP;
    ImageView burgerMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizpage);

        findItemsByID();

        homeButton.setOnClickListener(v -> MainActivity.redirectActivity(Quizpage.this,MainActivity.class));
        chatButton.setOnClickListener(v -> MainActivity.redirectActivity(Quizpage.this, ChatActivity.class));
        quizButton.setOnClickListener(v -> recreate());
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Quizpage.this,RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
        courseButton.setOnClickListener(v -> MainActivity.redirectActivity(Quizpage.this,Cursuri.class));
        logoButton.setOnClickListener(v -> MainActivity.closeDrawer(drawerLayout));
        burgerMenu.setOnClickListener(v -> MainActivity.openDrawer(drawerLayout));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        listOOP=new ArrayList<>();
        listBD =new ArrayList<>();

        listBD.add(new Question("Un tabel poate avea cel mult :","O singura cheie de index","O singura cheie externa","O singura cheie secundara","O singura cheie primara","O singura cheie primara","Cele mai multe tabele ar trebui să aibă o cheie primară, iar fiecare tabel poate avea doar o singură cheie primară. "));
        listBD.add(new Question("Subinterogarea:","Se referă la o instrucţiune SELECT care conţine o instrucţiune SELECT subordonată","Poate fi realizată cu operatorul INTERSECT","Se aplică în cazul relațiilor de tipul 1 la 1","Folosește clauza SUBQUERY în sintaxa instrucțiunii","Se referă la o instrucţiune SELECT care conţine o instrucţiune SELECT subordonată","O subinterogare este o instrucțiune SELECT imbricată într-o selectare, selectați... INTO, INSERARE... INTO, DELETe sau UPDATE Statement sau în interiorul altei subinterogări."));
        listBD.add(new Question("Operatorii logici din clauza WHERE pot fi:","AND, OR","!=","NOT BETWEEN","IS TRUE","NOT BETWEEN","Not between"));
        listBD.add(new Question("Utilizarea clauzelor DISTINCT și ORDER BY în instrucțiunea SELECT:","Asigură sortarea datelor crescător sau descrescător","Elimină rândurile duplicate și stabilește criteriile de sortare a datelor afișate în rezultatul interogarii","Permite stabilirea pseudonimelor și ordonarea datelor afișate în interogare","Permite specificarea unor condiții de selecție a rezultatelor","Elimină rândurile duplicate și stabilește criteriile de sortare a datelor afișate în rezultatul interogarii","Cuvântul cheie ORDER BY este folosit pentru a sorta setul de rezultate iar clauza DISTINCT este folosită pentru a returna numai valori distincte (diferite)."));
        listBD.add(new Question("Utilizarea clauzei WHERE în instrucțiunea SELECT:","Permite filtrarea rezultatelor","Asigura sortarea datelor crescator sau descrescator","Elimina randurile duplicate din rezultatul interogarii","Permite stabilirea pseudonimelor pentru coloanele afisate in interogare","Permite filtrarea rezultatelor","Clauza SQL WHERE este folosită pentru a specifica o condiție în timpul preluării datelor dintr-un singur tabel sau prin alăturarea cu mai multe tabele."));
        listBD.add(new Question("Instrucțiunea ALTER TABLE permite","Modificarea conținutului unui rând","Adăugarea unui rând","Adăugarea unei restricții","Modificarea interogării datelor din tabelă","Adăugarea unei restricții","Instructiuna ALTER TABLE este folosita pentru a adauga, sterge sau modifica coloane intr-un tabel existent"));
        listBD.add(new Question("Restricţia referenţială (FOREIGN KEY) la nivel de tabel impune o regulă:","Prin care coloana trebuie să fie de tip cheie externă","De unicitate a valorilor din acea coloană","Care nu poate referi nici o altă coloană din tabel","Care poate referi coloane multiple","Care poate referi coloane multiple","O cheie straina dintr-un tabel indica o cheie primara dintr-un alt tabel"));
        listBD.add(new Question("Prima formă normală","Asigură eliminarea datelor multivaloare","Folosește indecși unici în relațiile m la m","Presupune descompunerea bazei de date în minim 3 tabele","Asigură eliminarea dependențelor parțiale","Asigură eliminarea datelor multivaloare","O schema de relatie R este in NF daca si numai daca domeniile atributelor sunt indivizibile si fiecare valoare a fiecarui atribut este atomica"));
        listBD.add(new Question("Care convenție de sintaxă este corectă pentru SQL:","Instrucțiunea SQL se termină prin delimitatorul punct","Instrucțiunea SQL se termină prin delimitatorul /","Articolele într-o listă sunt separate prin virgulă","Articolele într-o listă sunt separate prin punct și virgulă","Articolele într-o listă sunt separate prin virgulă","Lista ,"));
        listBD.add(new Question("A doua formă normală: ","Asigură eliminarea dependențelor tranzitive","Asigură eliminarea restricțiilor referențiale","Presupune descompunerea bazei de date în minim 3 tabele","Asigură eliminarea dependențelor parțiale","Asigură eliminarea dependențelor parțiale","O schema de relatie R este in 2NF daca este in 1NF si daca orice atribut neprim din relatia R este dependent plin de orice cheie a lui R"));

        listOOP.add(new Question("Care dintre următoarele cuvinte cheie realizează moștenirea în Java? ","implements","inherits","extends","super","extends","Numită și derivare, moștenirea este un mecanism de refolosire a codului specific limbajelor orientate obiect și reprezintă posibilitatea de a defini o clasă care extinde o altă clasă deja existentă."));
        listOOP.add(new Question("Care dintre următoarele patternuri ar fi mai util dacă dorim ca anumite clase să fie notificate de schimbări ale altor clase?  ","Visitor","Strategy","Factory","Observer","Observer","Modelul de observator este un model de proiectare software în care un obiect, numit subiect, menține o listă a dependenților săi, numiți observatori, și îi notifică automat despre orice schimbare de stare, de obicei apelând una dintre metodele lor."));
        listOOP.add(new Question("Care dintre urmatoarele variante nu defineste încapsularea? ","expunerea unei interfețe high-level de lucru cu obiectul","accesul la membri private folosind metode de tip getter și setter","posibilitatea suprascrierii (overriding) metodelor","construirea de obiecte complexe și ascunderea modului lor de funcționare","posibilitatea suprascrierii (overriding) metodelor","suprascrierea metodelor"));
        listOOP.add(new Question("Care dintre următoarele concepte reprezintă o relație HAS-A? ","agregarea","mostenirea","polimorfismul","incapsularea","agregarea","Un obiect Student poate avea proprietăți precum student_id, nume, adresă. Acest obiect poate avea și un alt obiect denumit adresă cu informații proprii, cum ar fi orașul, statul, țara. În această situație, studentul are o adresă de referință a entității."));
        listOOP.add(new Question("Care variantă reprezintă o supraîncărcare corectă pentru metoda: protected int getGrade(String course)","protected int getGrade(String course) throws IOException","private int getGrade(String course)","protected long getGrade(String course)","public long getGrade(int studID)","public long getGrade(int studID)","da"));
        listOOP.add(new Question("Care variantă reprezintă supraîncărcarea corectă a metodei: String getMessage()","public String getMessage()","StringBuffer getMessage()","public String getMessage(String from)","public String getMessage() throws Exception","public String getMessage(String from)","da"));
        listOOP.add(new Question("Care combinație reprezintă, într-o clasă pe nume Test, o suprascriere, respectiv o supraîncărcare validă (overriding și overloading) pentru metoda equals din java.lang.Object?","public Boolean equals (Object o) \\ protected Integer equals (Object b)","boolean equals(Object o) \\ public boolean equals(Test t)","public Boolean equals (Object t) \\ public int equals (Object b)","public boolean equals(Object t) \\ public int equals(Test t)","public boolean equals(Object t) \\ public int equals(Test t)","da"));
        listOOP.add(new Question("Care variantă reprezintă suprascrierea corectă a metodei: protected int computeX(int a, float b) {…}?","int computeX(int a, float b) {…}","public int computeX(int a, float b) {…}","protected int computeX(Integer a, Float b) {…}","protected Integer computeX(int a, float b) {…}","public int computeX(int a, float b) {…}","da"));
        listOOP.add(new Question("Observer Pattern si Singleton Pattern, din punct de vedere al clasificării Gang of Four, sunt: ","Observer este un design pattern behavioural, iar Singleton este structural","Observer este un design pattern structural, iar Singleton este behavioural","Observer este un design pattern behavioural, iar Singleton este creational","ambele sunt structural","Observer este un design pattern behavioural, iar Singleton este creational","da"));
        listOOP.add(new Question("Care dintre urmatoarele afirmații este corectă?","Cuvântul final în fața unei metode arată faptul că acea metoda poate fi apelată doar în clasa respectivă","O clasa final nu poate fi instanțiată","Cuvantul final în fața unei variabile arată că acea variabilă poate fi assignata o singura dată","Toate cele de mai sus","Cuvantul final în fața unei variabile arată că acea variabilă poate fi assignata o singura dată","da"));


        quizbd.setOnClickListener(v -> MainActivity.redirectActivity(Quizpage.this,QuizBD.class));
        quizoop.setOnClickListener(v -> MainActivity.redirectActivity(Quizpage.this,QuizOOP.class));
    }
    public void findItemsByID(){
        quizoop=findViewById(R.id.quizoop);
        quizbd=findViewById(R.id.quizbd);
        burgerMenu=findViewById(R.id.clickBurgerMenu);
        drawerLayout=findViewById(R.id.drawer_layout);
        logoButton=findViewById(R.id.logoClick);
        logoutButton=findViewById(R.id.logoutButton);
        homeButton=findViewById(R.id.homeButton);
        chatButton=findViewById(R.id.chatButton);
        quizButton=findViewById(R.id.quizButton);
        courseButton=findViewById(R.id.courseButton);
        logoutButton=findViewById(R.id.logoutButton);
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
        closeDrawer(drawerLayout);
    }
}