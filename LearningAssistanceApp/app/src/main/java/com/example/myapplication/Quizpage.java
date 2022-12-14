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

        listBD.add(new Question("Un tabel poate avea cel mult :","O singura cheie de index","O singura cheie externa","O singura cheie secundara","O singura cheie primara","O singura cheie primara","Cele mai multe tabele ar trebui s?? aib?? o cheie primar??, iar fiecare tabel poate avea doar o singur?? cheie primar??. "));
        listBD.add(new Question("Subinterogarea:","Se refer?? la o instruc??iune SELECT care con??ine o instruc??iune SELECT subordonat??","Poate fi realizat?? cu operatorul INTERSECT","Se aplic?? ??n cazul rela??iilor de tipul 1 la 1","Folose??te clauza SUBQUERY ??n sintaxa instruc??iunii","Se refer?? la o instruc??iune SELECT care con??ine o instruc??iune SELECT subordonat??","O subinterogare este o instruc??iune SELECT imbricat?? ??ntr-o selectare, selecta??i... INTO, INSERARE... INTO, DELETe sau UPDATE Statement sau ??n interiorul altei subinterog??ri."));
        listBD.add(new Question("Operatorii logici din clauza WHERE pot fi:","AND, OR","!=","NOT BETWEEN","IS TRUE","NOT BETWEEN","Not between"));
        listBD.add(new Question("Utilizarea clauzelor DISTINCT ??i ORDER BY ??n instruc??iunea SELECT:","Asigur?? sortarea datelor cresc??tor sau descresc??tor","Elimin?? r??ndurile duplicate ??i stabile??te criteriile de sortare a datelor afi??ate ??n rezultatul interogarii","Permite stabilirea pseudonimelor ??i ordonarea datelor afi??ate ??n interogare","Permite specificarea unor condi??ii de selec??ie a rezultatelor","Elimin?? r??ndurile duplicate ??i stabile??te criteriile de sortare a datelor afi??ate ??n rezultatul interogarii","Cuv??ntul cheie ORDER BY este folosit pentru a sorta setul de rezultate iar clauza DISTINCT este folosit?? pentru a returna numai valori distincte (diferite)."));
        listBD.add(new Question("Utilizarea clauzei WHERE ??n instruc??iunea SELECT:","Permite filtrarea rezultatelor","Asigura sortarea datelor crescator sau descrescator","Elimina randurile duplicate din rezultatul interogarii","Permite stabilirea pseudonimelor pentru coloanele afisate in interogare","Permite filtrarea rezultatelor","Clauza SQL WHERE este folosit?? pentru a specifica o condi??ie ??n timpul prelu??rii datelor dintr-un singur tabel sau prin al??turarea cu mai multe tabele."));
        listBD.add(new Question("Instruc??iunea ALTER TABLE permite","Modificarea con??inutului unui r??nd","Ad??ugarea unui r??nd","Ad??ugarea unei restric??ii","Modificarea interog??rii datelor din tabel??","Ad??ugarea unei restric??ii","Instructiuna ALTER TABLE este folosita pentru a adauga, sterge sau modifica coloane intr-un tabel existent"));
        listBD.add(new Question("Restric??ia referen??ial?? (FOREIGN KEY) la nivel de tabel impune o regul??:","Prin care coloana trebuie s?? fie de tip cheie extern??","De unicitate a valorilor din acea coloan??","Care nu poate referi nici o alt?? coloan?? din tabel","Care poate referi coloane multiple","Care poate referi coloane multiple","O cheie straina dintr-un tabel indica o cheie primara dintr-un alt tabel"));
        listBD.add(new Question("Prima form?? normal??","Asigur?? eliminarea datelor multivaloare","Folose??te indec??i unici ??n rela??iile m la m","Presupune descompunerea bazei de date ??n minim 3 tabele","Asigur?? eliminarea dependen??elor par??iale","Asigur?? eliminarea datelor multivaloare","O schema de relatie R este in NF daca si numai daca domeniile atributelor sunt indivizibile si fiecare valoare a fiecarui atribut este atomica"));
        listBD.add(new Question("Care conven??ie de sintax?? este corect?? pentru SQL:","Instruc??iunea SQL se termin?? prin delimitatorul punct","Instruc??iunea SQL se termin?? prin delimitatorul /","Articolele ??ntr-o list?? sunt separate prin virgul??","Articolele ??ntr-o list?? sunt separate prin punct ??i virgul??","Articolele ??ntr-o list?? sunt separate prin virgul??","Lista ,"));
        listBD.add(new Question("A doua form?? normal??: ","Asigur?? eliminarea dependen??elor tranzitive","Asigur?? eliminarea restric??iilor referen??iale","Presupune descompunerea bazei de date ??n minim 3 tabele","Asigur?? eliminarea dependen??elor par??iale","Asigur?? eliminarea dependen??elor par??iale","O schema de relatie R este in 2NF daca este in 1NF si daca orice atribut neprim din relatia R este dependent plin de orice cheie a lui R"));

        listOOP.add(new Question("Care dintre urm??toarele cuvinte cheie realizeaz?? mo??tenirea ??n Java? ","implements","inherits","extends","super","extends","Numit?? ??i derivare, mo??tenirea este un mecanism de refolosire a codului specific limbajelor orientate obiect ??i reprezint?? posibilitatea de a defini o clas?? care extinde o alt?? clas?? deja existent??."));
        listOOP.add(new Question("Care dintre urm??toarele patternuri ar fi mai util dac?? dorim ca anumite clase s?? fie notificate de schimb??ri ale altor clase?  ","Visitor","Strategy","Factory","Observer","Observer","Modelul de observator este un model de proiectare software ??n care un obiect, numit subiect, men??ine o list?? a dependen??ilor s??i, numi??i observatori, ??i ??i notific?? automat despre orice schimbare de stare, de obicei apel??nd una dintre metodele lor."));
        listOOP.add(new Question("Care dintre urmatoarele variante nu defineste ??ncapsularea? ","expunerea unei interfe??e high-level de lucru cu obiectul","accesul la membri private folosind metode de tip getter ??i setter","posibilitatea suprascrierii (overriding) metodelor","construirea de obiecte complexe ??i ascunderea modului lor de func??ionare","posibilitatea suprascrierii (overriding) metodelor","suprascrierea metodelor"));
        listOOP.add(new Question("Care dintre urm??toarele concepte reprezint?? o rela??ie HAS-A? ","agregarea","mostenirea","polimorfismul","incapsularea","agregarea","Un obiect Student poate avea propriet????i precum student_id, nume, adres??. Acest obiect poate avea ??i un alt obiect denumit adres?? cu informa??ii proprii, cum ar fi ora??ul, statul, ??ara. ??n aceast?? situa??ie, studentul are o adres?? de referin???? a entit????ii."));
        listOOP.add(new Question("Care variant?? reprezint?? o supra??nc??rcare corect?? pentru metoda: protected int getGrade(String course)","protected int getGrade(String course) throws IOException","private int getGrade(String course)","protected long getGrade(String course)","public long getGrade(int studID)","public long getGrade(int studID)","da"));
        listOOP.add(new Question("Care variant?? reprezint?? supra??nc??rcarea corect?? a metodei: String getMessage()","public String getMessage()","StringBuffer getMessage()","public String getMessage(String from)","public String getMessage() throws Exception","public String getMessage(String from)","da"));
        listOOP.add(new Question("Care combina??ie reprezint??, ??ntr-o clas?? pe nume Test, o suprascriere, respectiv o supra??nc??rcare valid?? (overriding ??i overloading) pentru metoda equals din java.lang.Object?","public Boolean equals (Object o) \\ protected Integer equals (Object b)","boolean equals(Object o) \\ public boolean equals(Test t)","public Boolean equals (Object t) \\ public int equals (Object b)","public boolean equals(Object t) \\ public int equals(Test t)","public boolean equals(Object t) \\ public int equals(Test t)","da"));
        listOOP.add(new Question("Care variant?? reprezint?? suprascrierea corect?? a metodei: protected int computeX(int a, float b) {???}?","int computeX(int a, float b) {???}","public int computeX(int a, float b) {???}","protected int computeX(Integer a, Float b) {???}","protected Integer computeX(int a, float b) {???}","public int computeX(int a, float b) {???}","da"));
        listOOP.add(new Question("Observer Pattern si Singleton Pattern, din punct de vedere al clasific??rii Gang of Four, sunt: ","Observer este un design pattern behavioural, iar Singleton este structural","Observer este un design pattern structural, iar Singleton este behavioural","Observer este un design pattern behavioural, iar Singleton este creational","ambele sunt structural","Observer este un design pattern behavioural, iar Singleton este creational","da"));
        listOOP.add(new Question("Care dintre urmatoarele afirma??ii este corect???","Cuv??ntul final ??n fa??a unei metode arat?? faptul c?? acea metoda poate fi apelat?? doar ??n clasa respectiv??","O clasa final nu poate fi instan??iat??","Cuvantul final ??n fa??a unei variabile arat?? c?? acea variabil?? poate fi assignata o singura dat??","Toate cele de mai sus","Cuvantul final ??n fa??a unei variabile arat?? c?? acea variabil?? poate fi assignata o singura dat??","da"));


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