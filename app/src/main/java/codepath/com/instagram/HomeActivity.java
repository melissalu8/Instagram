package codepath.com.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;

// parse-dashboard --appId mlinstagram --masterKey masterkey --serverURL http://melissalu8-instagram.herokuapp.com/parse

public class HomeActivity extends AppCompatActivity {

    private Button logoutBtn;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        if (savedInstanceState == null) {
//            fragmentA = FragmentA.newInstance("foo");
//            fragmentB = FragmentB.newInstance("bar");
//            fragmentC = FragmentC.newInstance("baz");
//
//            // Let's first dynamically add a fragment into a frame container
//            getSupportFragmentManager().beginTransaction().
//                    replace(R.id.your_placeholder, new Capture(), "AddCapture").
//                    commit();
//            // Now later we can lookup the fragment by tag
//            Capture capture = (Capture)
//                    getSupportFragmentManager().findFragmentByTag("CaptureLookUp");
//
//            capture.onLaunchCamera();
//        }
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tbTop);
//        setSupportActionBar(toolbar);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        // TODO: Add Timeline Fragment
        final Capture capture = new Capture();
        final Timeline timeline = new Timeline();
//        final Fragment fragment3 = new ThirdFragment();

        FragmentTransaction toTimeline = fragmentManager.beginTransaction();
        toTimeline.replace(R.id.your_placeholder, timeline).commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            default:
                            case R.id.aCreate:
                                FragmentTransaction toCapture = fragmentManager.beginTransaction();
                                toCapture.replace(R.id.your_placeholder, capture).commit();
                                return true;
                            case R.id.aTimeline:
                                FragmentTransaction toTimeline = fragmentManager.beginTransaction();
                                toTimeline.replace(R.id.your_placeholder, timeline).commit();
                                return true;
//                            case R.id.action_music:
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.flContainer, fragment3).commit();
//                                return true;
                        }
                    }
                });

        logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                final Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "You have successfully logged out", Toast.LENGTH_LONG).show();
            }
        });

    }
}
