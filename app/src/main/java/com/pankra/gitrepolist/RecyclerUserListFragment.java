package com.pankra.gitrepolist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pankra.gitrepolist.adapter.UserAdapter;
import com.pankra.gitrepolist.model.User;
import com.pankra.gitrepolist.service.GitHubService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by SPankratov on 26.10.2015.
 */
public class RecyclerUserListFragment extends Fragment {
    protected List<User> mDataSet = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected UserAdapter mAdapter;


    private UserListCallback mCallback = sCallback;

    public interface UserListCallback {
        void onItemSelected(String id);
    }


    /**
     * A dummy implementation of the {@link UserListCallback} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static UserListCallback sCallback = new UserListCallback() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataSet();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_user_list_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    private void initDataSet() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubService gitHubService = retrofit.create(GitHubService.class);

        Call<List<User>> call = gitHubService.getUsers(0);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Response<List<User>> response, Retrofit retrofit) {
                mDataSet = response.body();
                mAdapter = new UserAdapter(getContext(), mDataSet);
                mAdapter.setUserListCallback(mCallback);

                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("getUser", t.getMessage());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof UserListCallback)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallback = (UserListCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallback = sCallback;
    }
}
