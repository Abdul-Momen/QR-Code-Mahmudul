package notes.notepad.notebook.keepnote.note;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.arnd.airdoc.R;
import com.arnd.airdoc.Scan;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class home_screen extends AppCompatActivity {
    TextView txt_name, tap;
    private FirebaseAuth mAuth;

    String username;
    Uri userimage;
    CircleImageView crcimg;
    TextView itm_count;
    Query query;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Modelclass> options;
    FirebaseRecyclerAdapter<Modelclass, ViewHolder> adapter;
    Button shw_lay, shw_lay2, cancel_drw;
    RelativeLayout drw, new_usr;
    int count = 0;
    String uid;
    FloatingActionButton floatingActionButton;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

//offline mode should be on top
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        } catch (Exception e) {

            e.printStackTrace();
        }


        txt_name = (TextView) findViewById(R.id.txt_name);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.add_document);

        shw_lay = (Button) findViewById(R.id.shw_drw);
        shw_lay2 = (Button) findViewById(R.id.shw_drw2);

        cancel_drw = (Button) findViewById(R.id.canceldrw);
        new_usr = (RelativeLayout) findViewById(R.id.new_user);

        drw = (RelativeLayout) findViewById(R.id.drw_lay);

        shw_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drw.setVisibility(View.VISIBLE);
            }
        });

        shw_lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drw.setVisibility(View.VISIBLE);
            }
        });

        cancel_drw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drw.setVisibility(View.GONE);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.rclrv);


        username = user.getDisplayName();
        txt_name.setText("Hi, " + username);

        crcimg = (CircleImageView) findViewById(R.id.profile_image);
        userimage = user.getPhotoUrl();
        Picasso.get().load(userimage).into(crcimg);

        itm_count = findViewById(R.id.itm_count);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //gets the  documnets of current user
        query = databaseReference.child("Documents").child(uid);

        //show the documents


        options = new FirebaseRecyclerOptions.Builder<Modelclass>().setQuery(query, Modelclass.class).build();


        adapter = new FirebaseRecyclerAdapter<Modelclass, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull Modelclass model) {

                int a=position+1;
                String no= String.valueOf(a);


                holder.titleview.setText(String.format(no+"."));
                holder.dateview.setText("Date: " + model.getDt());
             //   holder.color_shw.setCardBackgroundColor(model.getColor());
                //dsb.setVisibility(View.INVISIBLE);


                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String visit_user_id = getRef(position).getKey();
                        Intent intent = new Intent(getApplicationContext(), document_view.class);
                        intent.putExtra("passkey", visit_user_id);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_look, parent, false);


                return new ViewHolder(view);
            }


        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        // important line to show new dat at top
        linearLayoutManager.setStackFromEnd(true);


        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = (int) snapshot.getChildrenCount();
                if (count > 0) {
                    itm_count.setText("Notes (" + Integer.toString(count) + ")");

                } else {
                    new_usr.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter.startListening();

        recyclerView.setAdapter(adapter);


        if (Check_Network.isInternetAvailable(home_screen.this)) {


        } else {

            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show();


        }
/*
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home_screen.this, recheck.class));

            }
        });*/


    }
//methods


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        int darkflag = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (darkflag == Configuration.UI_MODE_NIGHT_YES) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    public void drw_logout(View view) {

        startActivity(new Intent(home_screen.this, logout.class));

    }

    public void drw_security_key(View view) {
        startActivity(new Intent(home_screen.this, Security_key.class));

    }

    public void drw_scan(View view) {
        startActivity(new Intent(home_screen.this, Scan.class));

    }

    public void hm_rdrc_scan(View view) {

        startActivity(new Intent(home_screen.this, Scan.class));

    }

    public void hm_profile(View view) {
        startActivity(new Intent(home_screen.this, logout.class));


    }


    public void hm_key(View view) {
        startActivity(new Intent(home_screen.this, Security_key.class));

    }


    public void flt_action(View view) {

    }
}