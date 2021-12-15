package notes.notepad.notebook.keepnote.note;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.arnd.airdoc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Security_key extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    FirebaseFirestore db;
    EditText sk_text,hint_text;
    String get_key, userid, get_hint_key;
    DocumentReference documentReference;
    FirebaseAuth firebaseAuth;
    Button savekey, menu_btn;
    TextView textView, name_sc;
    CircleImageView full_profile_sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_key);


        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        userid = firebaseAuth.getCurrentUser().getUid();

        documentReference = db.collection("security_key").document(userid);

        sk_text = findViewById(R.id.add_key);

        hint_text = findViewById(R.id.hint_key);

        textView = findViewById(R.id.btm_sk);

        menu_btn= findViewById(R.id.menu_button);


        full_profile_sc =  findViewById(R.id.profile_sc);
         Uri full_image = user.getPhotoUrl();
        Picasso.get().load(full_image).into(full_profile_sc);

        String username= user.getDisplayName();
        name_sc =  findViewById(R.id.name_sc);
        name_sc.setText(username);




        savekey = (Button) findViewById(R.id.save_skey);

        savekey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_key = sk_text.getText().toString().trim();
                get_hint_key=hint_text.getText().toString().trim();

                if (get_key.matches("")) {

                    Toast.makeText(Security_key.this, "Enter Security key", Toast.LENGTH_SHORT).show();

                }
                else {
                    add_key();
                }
            }
        });

        status();



    }
///methods
    private void add_key() {

        Map<String, String> save_data = new HashMap<>();
        save_data.put("security_key", get_key);
        save_data.put("hint", get_hint_key);


        documentReference.set(save_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Security_key.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Security_key.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        })

        ;
    }


    private void status() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot data = task.getResult();
                if (data.exists()) {
                    savekey.setText("Update");
                    Toast.makeText(Security_key.this, "You already have a security key", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    private void delete() {


        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Security_key.this, "Security Key Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), home_screen.class));




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Security_key.this, "Failed to delete", Toast.LENGTH_SHORT).show();
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

    public void sk_back2(View view) {
        startActivity(new Intent(getApplicationContext(), home_screen.class));

    }

    public void sk_back(View view) {
        startActivity(new Intent(getApplicationContext(), home_screen.class));

    }

    public void Pop_menu_sc(View view) {

        PopupMenu popup2 = new PopupMenu(this, view);
        popup2.setOnMenuItemClickListener(this);
        popup2.inflate(R.menu.menu_sc);
        popup2.show();
    }


    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.delete_sc:
                delete();
                return true;
            default:
                return false;
        }


    }


}