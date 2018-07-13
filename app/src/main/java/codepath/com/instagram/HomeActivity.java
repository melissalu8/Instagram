package codepath.com.instagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

// parse-dashboard --appId mlinstagram --masterKey masterkey --serverURL http://melissalu8-instagram.herokuapp.com/parse

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        // TODO: Add Timeline Fragment
        final Capture capture = new Capture();
        final Timeline timeline = new Timeline();
        final Profile profile = new Profile();

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
                            case R.id.aProfile:
                                FragmentTransaction toProfile = fragmentManager.beginTransaction();
                                toProfile.replace(R.id.your_placeholder, profile).commit();
                                return true;
                        }
                    }
                });

    }

}
