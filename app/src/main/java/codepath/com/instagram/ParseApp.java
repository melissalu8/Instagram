package codepath.com.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import codepath.com.instagram.Model.Post;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        // Set up parse server
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("mlinstagram")
                .clientKey("masterkey")
                .server("http://melissalu8-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}


