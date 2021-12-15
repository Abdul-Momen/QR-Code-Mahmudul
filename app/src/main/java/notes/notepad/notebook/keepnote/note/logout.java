package notes.notepad.notebook.keepnote.note;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.arnd.airdoc.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class logout extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Button logout_btn;
    CircleImageView full_profile;
    Uri full_image;
    String useremail;
    TextView textemail;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        //get mail
        useremail = user.getEmail();
        textemail = (TextView) findViewById(R.id.profile_email);
        textemail.setText(useremail);

        //get image
        full_profile = (CircleImageView) findViewById(R.id.full_profile);
        full_image = user.getPhotoUrl();
        Picasso.get().load(full_image).into(full_profile);


        logout_btn = (Button) findViewById(R.id.logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                mAuth.getInstance().signOut();
                finish();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                             //   Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(logout.this, MainActivity.class);
                                startActivity(i);
                            }
                        });


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


    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }


    public void log_back(View view) {
        startActivity(new Intent(logout.this,home_screen.class));
    }

    public void log_back2(View view) {
        startActivity(new Intent(logout.this,home_screen.class));

    }
}
