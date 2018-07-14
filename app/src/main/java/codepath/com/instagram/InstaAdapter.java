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

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import codepath.com.instagram.Model.GlideApp;
import codepath.com.instagram.Model.Post;

// Comments Array?
// Communicating
public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {

    private List<Post> mPosts;
    private HomeActivity homeActivity;
    Context context;

    // pass in the Tweets array in the constructor
    public InstaAdapter(List<Post> posts, HomeActivity homeActivity) {
        mPosts = posts;
        this.homeActivity = homeActivity;
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

        holder.tvLikes.setText(post.getLikes().toString());
        holder.tvComment.setText(post.getCommentCount().toString());
        holder.tvLocation.setText(post.getLocation());

        // TODO: profile image using Glide
        GlideApp.with(context)
                .load(post.getImage().getUrl())
                .centerCrop()
                .into(holder.ivPhoto);

        GlideApp.with(context)
                .load(post.getUser().getParseFile("profilePicture").getUrl())
                .centerCrop()
                .transform(new CircleCrop())
                .into(holder.ivProfileImage);
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

        // Likes and Comments
        boolean likes;
        boolean comments;

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

            ibLikes.setOnClickListener(this);
            ibComment.setOnClickListener(this);
            ivProfileImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // FAVORITE
                if (view.getId() == R.id.ibLikes) {
                    // get the post at the position, this won't work if the class is static
                    Post post = mPosts.get(position);
                    // TODO: Unfavorite
                    //favorited = post.isFavorited();
//                    if (favorited == false) {
                    int likesCount = (int) post.getLikes();
                    likesCount++;
                    tvLikes.setText(likesCount + "");
                    post.setLikes(likesCount);
//                                    favorited = tweet.isFavorited();
                    ibLikes.setImageResource(R.drawable.ic_heart_filled_24dp);
                }

                else if (view.getId() == R.id.ibComment) {
                    // get the post at the position, this won't work if the class is static
                    Post post = mPosts.get(position);
                    //favorited = post.isFavorited();
//                    if (favorited == false) {
                    int commentCount = (int) post.getCommentCount();
                    commentCount++;
                    tvComment.setText(commentCount + "");
                    post.setCommentCount(commentCount);
//                                    favorited = tweet.isFavorited();
                    ibComment.setImageResource(R.drawable.ic_chat_bubble_filled_24dp);

                    // luanch intent to detail activity
                    Intent intent = new Intent(context, DetailActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                    // show the activity
                    context.startActivity(intent);
                }

                else if (view.getId() == R.id.ivProfileImage){
                    // ParseUser user
                    // Fragment transaction
                    // new bundle

                    // get the post at the position, this won't work if the class is static
                    Post post = mPosts.get(position);
                    ParseUser user = post.getUser();
                    homeActivity.transactionProfile(user);
                }
//                    }

                else {
                    // get the post at the position, this won't work if the class is static
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


