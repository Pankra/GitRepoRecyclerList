package com.pankra.gitrepolist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pankra.gitrepolist.adapter.RepoAdapter;
import com.pankra.gitrepolist.model.Rate;
import com.pankra.gitrepolist.model.Repo;
import com.pankra.gitrepolist.service.GitHubService;
import com.pankra.gitrepolist.service.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A fragment representing a single User detail screen.
 * This fragment is either contained in a {@link UserListActivity}
 * in two-pane mode (on tablets) or a {@link UserDetailActivity}
 * on handsets.
 */
public class UserDetailFragment extends Fragment {
    protected List<Repo> mDataSet = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected RepoAdapter mAdapter;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String USER_LOGIN = "user_login";
    public static final String AUTH_LOGIN = "auth_login";
    public static final String AUTH_PASS = "auth_pass";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(USER_LOGIN)) {
            String login = getArguments().getString(AUTH_LOGIN);
            String pass = getArguments().getString(AUTH_PASS);
            getRepoList(getArguments().getString(USER_LOGIN), login, pass);
        }
    }

    private void getRepoList(final String user, String login, String pass) {
        Log.d("UserDetailFragment", "LOGIN: " + login);
        Log.d("UserDetailFragment", "PASS: " + pass);

        final GitHubService gitHubService = ServiceGenerator.createService(GitHubService.class, login, pass);

        Call<Rate> limits = gitHubService.getRateLimit();
        limits.enqueue(new Callback<Rate>() {
            @Override
            public void onResponse(Response<Rate> response, Retrofit retrofit) {
                Rate rate = response.body();
                Log.d("RateLimit", "Remaining: " + rate.getCoreRemaining());
                Log.d("RateLimit", "Limit: " + rate.getCoreLimit());
                if (rate.getCoreRemaining() == 0) {
                    if (rate.getCoreLimit() == 60) {
                        //пользователь еще не авторизовался, предложим ему авторизоваться
                        new AuthDialogFragment().show(getFragmentManager(), "AuthDialog");
                    } else {
                        //пользователь авторизован и все равно достиг предела, просто сообщим ему это
                        new AlertDialog.Builder(getContext())
                                .setMessage("Request Limit of " + rate.getCoreLimit() + " exceeded.")
                                .show();
                    }
                } else {

                    Call<List<Repo>> call = gitHubService.getRepos(user);

                    call.enqueue(new Callback<List<Repo>>() {
                        @Override
                        public void onResponse(Response<List<Repo>> response, Retrofit retrofit) {
                            mDataSet = response.body();
                            if (mDataSet == null) {
                                mDataSet = new ArrayList<>();
                                new AlertDialog.Builder(getContext())
                                        .setMessage("User '" + user + "' not found.")
                                        .show();
                            } else if (mDataSet.size() == 0) {
                                new AlertDialog.Builder(getContext())
                                        .setMessage("User '" + user + "' has no repos.")
                                        .show();
                            }
                            mAdapter = new RepoAdapter(getContext(), mDataSet);

                            mRecyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e("getUser", t.getMessage());
                        }
                    });

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("checkLimit", t.getMessage());

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_user_list_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
