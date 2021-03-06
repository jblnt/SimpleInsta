package com.example.simpleinsta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simpleinsta.Post;
import com.example.simpleinsta.R;
import com.example.simpleinsta.adapters.PostAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";

    private RecyclerView rvTimeline;
    private SwipeRefreshLayout swipeContainer;
    List<Post> feed;
    private PostAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize adapter
        feed = new ArrayList<>();
        adapter = new PostAdapter(getContext(), feed);

        //grab initial data
        grabData();

        // --
        //User defined logic for timeline using RecyclerView;
        rvTimeline = view.findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTimeline.setAdapter(adapter);

        //swipe refresh methods
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(requireContext(), "Updating", Toast.LENGTH_SHORT).show();
                updateTimeline();
            }
        });
    }

    private void grabData() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setLimit(20);
        query.orderByDescending("createdAt");
        query.include("user.email");
        query.include("userimgs.profilepic");

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null){
                    //something went wrong
                    Log.e(TAG, "Query Went Wrong", e);
                } else {
                    adapter.addFresh(objects);
                }
            }
        });
    }

    private void updateTimeline() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setLimit(20);
        query.orderByDescending("createdAt");
        query.include("user.email");
        query.include("userimgs.profilepic");

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null){
                    //something went wrong
                    Log.e(TAG, "Query Went Wrong", e);
                } else {
                    //Log.i(TAG, String.valueOf(objects.size()));
                    adapter.clear();
                    adapter.addFresh(objects);

                    swipeContainer.setRefreshing(false);
                    //adapter.notifyDataSetChanged();
                }
            }
        });
    }
}