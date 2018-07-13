package codepath.com.instagram.Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final  String KEY_DESCRIPTION = "description";
    private static final  String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_LIKES = "likes";
    private static final String KEY_COMMENT_COUNT = "commentCount";
    public boolean favorited = false;
    public boolean retweeted = false;

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    // parseuser rather than parsepointer
    // set up in such a way that user is returned with every post
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public Number getLikes () {return getInt(KEY_LIKES);}

    public void setLikes (Number likes) { put(KEY_LIKES, likes); }

    public Number getCommentCount () {return getInt(KEY_COMMENT_COUNT);}

    public void setCommentCount (Number commentCount) { put(KEY_COMMENT_COUNT, commentCount); }

    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }
}
