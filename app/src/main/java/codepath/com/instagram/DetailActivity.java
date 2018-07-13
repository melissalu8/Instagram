package codepath.com.instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.instagram.Model.GlideApp;
import codepath.com.instagram.Model.Post;

public class DetailActivity extends AppCompatActivity {
    Post post;

    // the view objects
    // Automatically finds each field by the specified ID.
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvCaption) TextView tvCaption;
    @BindView(R.id.tvUserCaption) TextView tvUserCaption;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvTimeStamp) TextView tvTimeStamp;
    @BindView(R.id.tvLikes) TextView tvLikes;
    @BindView(R.id.tvComment) TextView tvComment;
    @BindView(R.id.ivPhoto) ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // unwrap the movie passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("DetailActivity", String.format("Showing details for '%s'", post.getUser()));

        tvUserName.setText(post.getUser().getUsername().toString());
        tvCaption.setText(post.getDescription());
        tvUserCaption.setText(post.getUser().getUsername().toString());
        tvTimeStamp.setText(post.getCreatedAt().toString());

        GlideApp.with(this)
                .load(post.getImage().getUrl())
                .into(ivPhoto);
    }
}
