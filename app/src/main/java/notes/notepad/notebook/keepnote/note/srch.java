package notes.notepad.notebook.keepnote.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

import com.arnd.airdoc.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class srch extends AppCompatActivity {
    RecyclerView recview;
    myadapter adapter;
    String uid;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srch);


        editText = (EditText) findViewById(R.id.srch_text);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Modelclass> options =
                new FirebaseRecyclerOptions.Builder<Modelclass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Documents").child(uid), Modelclass.class)
                        .build();


        adapter = new myadapter(options);
        recview.setAdapter(adapter);



    }

    //methods
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.searchmenu,menu);

        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search_met(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search_met(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private void search_met(String s) {
        FirebaseRecyclerOptions<Modelclass> options =
                new FirebaseRecyclerOptions.Builder<Modelclass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Documents").child(uid).startAt(s).endAt(s+"\uf8ff"), Modelclass.class)
                        .build();

        adapter = new myadapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);

    }


}