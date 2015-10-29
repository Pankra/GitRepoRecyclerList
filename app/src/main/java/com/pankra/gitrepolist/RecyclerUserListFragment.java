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
import com.pankra.gitrepolist.interfaces.LoadUserListener;
import com.pankra.gitrepolist.interfaces.UserListTapListener;
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
    private LinearLayoutManager mLayoutManager;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_user_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UserAdapter(getActivity(), mTapListener, mLoadListener);
        mRecyclerView.setAdapter(mAdapter);

        loadUserData(0);
    }

    private final UserListTapListener mTapListener = new UserListTapListener() {
        @Override
        public void userTap(String login) {
            mCallback.onItemSelected(login);
        }
    };

    private final LoadUserListener mLoadListener = new LoadUserListener() {
        @Override
        public void loadNext(long lastUserId) {
            loadUserData(lastUserId);
        }
    };

    private void addDataToAdapter(List<User> users) {
        if (users != null) {
            mAdapter.addItems(users);
        }
    }

    public void loadUserData(long lastUserId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubService gitHubService = retrofit.create(GitHubService.class);

        Call<List<User>> call = gitHubService.getUsers(lastUserId);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Response<List<User>> response, Retrofit retrofit) {
                List<User> users = response.body();
                addDataToAdapter(users);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("loadUserData", t.getMessage());
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
}
