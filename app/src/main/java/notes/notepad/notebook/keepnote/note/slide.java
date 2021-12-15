package notes.notepad.notebook.keepnote.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.arnd.airdoc.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class slide extends AppCompatActivity {

    private FirebaseAuth mAuth;

     ViewPager viewPager;
     PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        List<Fragment> list = new ArrayList<>();
        list.add(new page1());
        list.add(new page2());
        list.add(new page3());
        list.add(new page4());


        viewPager = findViewById(R.id.view_pagerr);
        pagerAdapter = new slider_adapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(pagerAdapter);
        mAuth = FirebaseAuth.getInstance();


    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
            //Toast.makeText(this, "" + currentUser.getEmail(), Toast.LENGTH_SHORT).show();

        } else {


        }

    }


    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(slide.this, MainActivity.class);
        startActivity(intent);
    }


}