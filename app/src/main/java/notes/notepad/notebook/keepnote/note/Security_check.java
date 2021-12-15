package notes.notepad.notebook.keepnote.note;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.arnd.airdoc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Security_check extends AppCompatActivity {

    FirebaseFirestore db;
    EditText sc_text;
    TextView hint_text;
    String get_key, userid;
    DocumentReference documentReference;
    FirebaseAuth firebaseAuth;
    Button checkkey;
    RelativeLayout sc_one, sc_two;
    LottieAnimationView secure_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_check);




        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userid = firebaseAuth.getCurrentUser().getUid();

        documentReference = db.collection("security_key").document(userid);

        sc_text = findViewById(R.id.check_sc);

        sc_one = findViewById(R.id.sc_one);
        sc_two = findViewById(R.id.sc_two);

        secure_view=(LottieAnimationView) findViewById(R.id.secure_anim);

        hint_text = (TextView) findViewById(R.id.hint_txt);
        hint_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint();
            }
        });


        checkkey = findViewById(R.id.check_btn);

        checkkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                check_status();
            }
        }, 1000);




    }

    //methods
    private void check() {

        get_key = sc_text.getText().toString();


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot data = task.getResult();
                if (data.exists()) {
                    String sc_key = data.getString("security_key");
                    // Toast.makeText(Security_check.this, "Approved"+sc_key, Toast.LENGTH_SHORT).show();

                    if (get_key.equalsIgnoreCase(sc_key)) {

                        startActivity(new Intent(Security_check.this, home_screen.class));

                    } else {
                        Toast.makeText(Security_check.this, "Try Again", Toast.LENGTH_SHORT).show();

                    }


                }
            }
        });

    }

    private void check_status() {

        documentReference.get().addOnCompleteListener(task -> {
            try {
                DocumentSnapshot data = task.getResult();

                if (data.exists()) {
                    sc_one.setVisibility(View.INVISIBLE);
                    sc_two.setVisibility(View.VISIBLE);
                    secure_view.setVisibility(View.VISIBLE);


                } else {

                    startActivity(new Intent(Security_check.this, home_screen.class));
                }
            }catch (Exception e){
                Toast.makeText(Security_check.this, "Check Network", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Security_check.this, home_screen.class));

            }
        });

    }

    private void hint() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot data = task.getResult();
                if (data.exists()) {
                    String sc_key = data.getString("hint");
                    hint_text.setText("hint: "+sc_key);


                }
            }
        });


    }





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        int darkflag = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (darkflag == Configuration.UI_MODE_NIGHT_YES) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

}


