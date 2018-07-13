package codepath.com.instagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.instagram.Model.Post;

// TODO : Add implements fragment Call backs ex. ProfileCallBacks
public class Timeline extends Fragment {

    private SwipeRefreshLayout swipeContainer;

    Button refreshButton;
    InstaAdapter instaAdapter;
    ArrayList<Post> posts;
    RecyclerView rvInstaTimeline;
    ProgressBar pb;

    @BindView(R.id.ibLikes) ImageButton ibLikes;
    @BindView(R.id.ibComment) ImageButton ibComment;
    @BindView(R.id.tvLikes) TextView tvLikes;
    @BindView(R.id.tvComment) TextView tvComment;


    public Timeline() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());

        pb = (ProgressBar) getActivity().findViewById(R.id.pbLoading);

        swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendPostQuery();
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // find the RecylcerView
        rvInstaTimeline = (RecyclerView) getActivity().findViewById(R.id.rvInstaTimeline);

        posts = new ArrayList<Post>();
        instaAdapter = new InstaAdapter(posts);
        // RecyclerView setup (layout manager, use adapter)
        rvInstaTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));
        // set the adapter
        rvInstaTimeline.setAdapter(instaAdapter);

        // init the arraylist (data source)
        sendPostQuery();
    }

    private void sendPostQuery() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                posts.clear();
                instaAdapter.notifyDataSetChanged();
                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        pb.setVisibility(ProgressBar.VISIBLE);
                        Post post = objects.get(i);
                        posts.add(0, post);
                        // construct the adapter from this datasource
                        instaAdapter.notifyItemInserted(0);
                        rvInstaTimeline.scrollToPosition(0);
                        Log.d("HomeActivity", "Post[" + i + "' = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );
                    }
                    pb.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                posts.clear();
                posts.addAll(objects);
                swipeContainer.setRefreshing(false);
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
