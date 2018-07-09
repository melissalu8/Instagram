package codepath.com.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Set up parse server
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("mlinstagram")
                .clientKey("masterkey")
                .server("http://melissalu8-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}


