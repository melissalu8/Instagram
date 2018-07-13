package codepath.com.instagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import codepath.com.instagram.Model.GlideApp;
import codepath.com.instagram.Model.Post;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {

    private List<Post> mPosts;
    Context context;

    // pass in the Tweets array in the constructor
    public InstaAdapter(List<Post> posts) {
        mPosts = posts;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout you created
        // need to first get context
        context = parent.getContext();
        // get the layout using the context object
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate item_tweet
        View tweetView = inflater.inflate(R.layout.post_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(tweetView);
        return (ViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InstaAdapter.ViewHolder holder, int position) {
        // get the data according to position
        Post post = mPosts.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(post.getUser().getUsername().toString());
        holder.tvCaption.setText(post.getDescription());
        holder.tvUserCaption.setText(post.getUser().getUsername().toString());
        holder.tvTimeStamp.setText(post.getCreatedAt().toString());
//        holder.tvBody.setText(tweet.body);
//        holder.tvID.setText(tweet.user.screenName);
//        holder.tvTimeStamp.setText(tweet.createdAt);
//        holder.tvFavCount.setText(tweet.favorite_count+"");
//        holder.tvRetweetCount.setText(tweet.retweet_count+"");
//        holder.tvComCount.setText(tweet.reply_count+"");

        // TODO: profile image using Glide
        GlideApp.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvLocation;
        public TextView tvTimeStamp;
        public ImageView ivPhoto;

        public ImageButton ibLikes;
        public TextView tvLikes;
        public ImageButton ibComment;
        public TextView tvComment;
        public TextView tvUserCaption;
        public TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);

            // TODO: Description setOnClickListener
           itemView.setOnClickListener(this);
            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);

            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);

            ibLikes = (ImageButton) itemView.findViewById(R.id.ibLikes);
            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
            ibComment = (ImageButton) itemView.findViewById(R.id.ibComment);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvUserCaption = (TextView) itemView.findViewById(R.id.tvUserCaption);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);

            // TODO: setOnClickListener for ibLikes, ibComment

//            ibComment.setOnClickListener(this);
//            ibRetweet.setOnClickListener(this);
//            ibHeart.setOnClickListener(this);


            // TODO: grab onClick idea from Twitter for likes and comments
//        @Override
//        public void onClick(View view) {
//            // gets item position
//            int position = getAdapterPosition();
//            // make sure the position is valid, i.e. actually exists in the view
//            if (position != RecyclerView.NO_POSITION) {

        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Post post = mPosts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, DetailActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                // show the activity
                context.startActivity(intent);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}


