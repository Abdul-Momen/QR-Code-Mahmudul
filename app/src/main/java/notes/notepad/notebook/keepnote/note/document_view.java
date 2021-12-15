package notes.notepad.notebook.keepnote.note;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.arnd.airdoc.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import se.simbio.encryption.Encryption;

public class document_view extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    DatabaseReference DataRef, databaseReference;
    EditText view_title, view_des;
    String uid, ttl_text, des_text, view_key;
    private String document_key;
    Query query;
    Button  menu_btn;
    String  formattedDate;
    Date date;
    TextToSpeech tts;
    String e1 = "sonargaon";
    String e2 = "urnothackedon";
    byte[] ev = new byte[16];
    Encryption encryption;
    String erp;

    ImageView ttsaction,ttspeak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);


        view_des = (EditText) findViewById(R.id.view_description_text);
        view_title = (EditText) findViewById(R.id.view_title_text);
        menu_btn = (Button) findViewById(R.id.menu_button);
        ttspeak =  (ImageView)findViewById(R.id.tts_spak);



        date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(date);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DataRef = FirebaseDatabase.getInstance().getReference();
        view_key = getIntent().getStringExtra("passkey");

        //gets the  documnets of current user
        query = DataRef.child("Documents").child(uid).child(view_key);

        //view document  only single time
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("t").getValue().toString().trim();
                    String description = dataSnapshot.child("d").getValue().toString().trim();
                    //this is node key saved as key
                    document_key = dataSnapshot.child("k").getValue().toString().trim();

                    encryption = Encryption.getDefault(e1, e2, ev);
                    String decrypted = encryption.decryptOrNull(description);

                    view_title.setText(title);
                    view_des.setText(decrypted);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });



        ttsaction = (ImageView) findViewById(R.id.tts_action);

        ttsaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();

            }
        });

        ttspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Toast.makeText(getApplicationContext(), "processing..", Toast.LENGTH_LONG).show();
         /*       if (toSpeak!=null){
                TextToSpeech tts = new TextToSpeech(document_view.this, (TextToSpeech.OnInitListener)document_view.this);
                tts.setLanguage(Locale.ENGLISH);
                tts.speak(toSpeak, TextToSpeech.QUEUE_ADD, null);
                }*/

                tts=new TextToSpeech(document_view.this, new TextToSpeech.OnInitListener() {

                    @Override
                    public void onInit(int status) {
                        // TODO Auto-generated method stub
                        if(status == TextToSpeech.SUCCESS){
                            int result=tts.setLanguage(Locale.US);
                            if(result==TextToSpeech.LANG_MISSING_DATA ||
                                    result==TextToSpeech.LANG_NOT_SUPPORTED){
                                Log.e("error", "This Language is not supported");
                            }
                            else{
                                ConvertTextToSpeech();
                            }
                        }
                        else
                            Log.e("error", "Initilization Failed!");
                    }
                });
            }

            private void ConvertTextToSpeech() {
                // TODO Auto-generated method stub
                String text = view_des.getText().toString();
                if(text==null||"".equals(text))
                {
                    text = "Content not available";
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }else
                    tts.speak(text+"is saved", TextToSpeech.QUEUE_FLUSH, null);
            }
        });






    }
    //methods


    private void update_met() {
        ttl_text = view_title.getText().toString().trim();
        des_text = view_des.getText().toString().trim();
        encryption = Encryption.getDefault(e1, e2, ev);
        erp = encryption.encryptOrNull(des_text);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Documents");

        HashMap hashMap = new HashMap();

        hashMap.put("t", ttl_text);
        hashMap.put("d", erp);
        hashMap.put("dt", formattedDate);


        databaseReference.child(uid).child(document_key).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(document_view.this, "saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(document_view.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void delete_met() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Documents");

        databaseReference.child(uid).child(document_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(document_view.this, "Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), home_screen.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(document_view.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void Alert_dailog_delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(document_view.this);

        builder.setTitle("Delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete_met();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void listen_data() {

        String toSpeak = view_des.getText().toString();
         Toast.makeText(this, "starting", Toast.LENGTH_SHORT).show();
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

       // Toast.makeText(this, "processing..", Toast.LENGTH_LONG).show();
        TextToSpeech tts = new TextToSpeech(this, (TextToSpeech.OnInitListener) getApplicationContext());
        tts.setLanguage(Locale.ENGLISH);
        tts.speak(toSpeak, TextToSpeech.QUEUE_ADD, null);


    }




    private  void savepdf() {
        Toast.makeText(this, "This feature is coming soon", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case  1000:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    savepdf();
                }
                else Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
        }
    }




    public void Popup_menu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_res);
        popup.show();

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.delete_menu:
                Alert_dailog_delete();
                return true;
            case R.id.tts_menu:
                listen_data();
                return true;

            default:
                return false;
        }


    }

    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
           // ttsaction.setVisibility(View.GONE);

        }
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), home_screen.class));
        onPause();
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

    public void rdrc_back(View view) {
        startActivity(new Intent(document_view.this, home_screen.class));
    }

    public void rdrc_back2(View view) {
        startActivity(new Intent(document_view.this, home_screen.class));

    }

    public void update_doc(View view) {
        update_met();
    }
}