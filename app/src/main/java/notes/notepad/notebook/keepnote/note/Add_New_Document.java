package notes.notepad.notebook.keepnote.note;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.res.Configuration;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Toast;

import com.arnd.airdoc.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


import se.simbio.encryption.Encryption;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Add_New_Document extends AppCompatActivity {


    EditText title, description;

    Button color_btn;
    ImageButton save_document;
    String title_text, des_text;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    DatabaseReference database_ref;
    String now_date, user_id, formattedDate;
    Date date;
    String e1 = "sonargaon";
    String e2 = "urnothackedon";
    byte[] ev = new byte[16];
    Encryption encryption;
    String erp;
    private int RecordAudioRequestCode = 1;
    int selected_color;

    ImageView mic_button;
    String color_code;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__document);


        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(date);


        title = findViewById(R.id.title_text);
        description = findViewById(R.id.description_text);

        color_btn = findViewById(R.id.color_btn);
        color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Color_Picker();
            }
        });


        save_document = findViewById(R.id.save_document);

        save_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_text = title.getText().toString().trim();
                des_text = description.getText().toString().trim();
                //for encryption
                encryption = Encryption.getDefault(e1, e2, ev);
                erp = encryption.encryptOrNull(des_text);
                now_date = formattedDate;

                if (title_text.matches("") && des_text.matches("")) {
                    Toast.makeText(Add_New_Document.this, "Write Something ", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(Add_New_Document.this, "Encrypting", Toast.LENGTH_SHORT).show();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            // Intent mainIntent = new Intent(Add_New_Document.this, MainActivity.class);
                            upload_document();
                        }
                    }, 1200);


                }
            }
        });


        user_id = firebaseUser.getUid();
        database_ref = FirebaseDatabase.getInstance().getReference().child("Documents");


        Intent i = getIntent();
        String text = i.getStringExtra("TextBox");
       // Now set this value to EditText
        description.setText(text);


    }

    private void Color_Picker() {

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, selected_color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                selected_color = color;
                color_btn.setBackgroundColor(selected_color);


            }
        });
        colorPicker.show();
    }

    //methods


    private void upload_document() {


        HashMap hashMap = new HashMap();
        String key = database_ref.push().getKey();
        hashMap.put("t", title_text);
        hashMap.put("d", erp);
        hashMap.put("dt", now_date);
        hashMap.put("k", key);
        hashMap.put("color", selected_color);


        //this  lines  saves it in firebase until .add on success
        database_ref.child(user_id).child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(Add_New_Document.this, "Added", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), home_screen.class));


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Add_New_Document.this, "Failed ", Toast.LENGTH_SHORT).show();
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


    public void rdrc_back2(View view) {
        startActivity(new Intent(getApplicationContext(), home_screen.class));

    }

    public void rdrc_back(View view) {
        startActivity(new Intent(getApplicationContext(), home_screen.class));

    }
}