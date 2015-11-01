package com.pankra.gitrepolist.service;

import com.pankra.gitrepolist.model.Rate;
import com.pankra.gitrepolist.model.Repo;
import com.pankra.gitrepolist.model.User;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Serge on 24.10.2015.
 */
public interface GitHubService {

    @GET("/users")
    Call<List<User>> getUsers(@Query("since") long lastUserId);

    @GET("/rate_limit")
    Call<Rate> getRateLimit();

    @GET("/users/{login}/repos")
    Call<List<Repo>> getRepos(@Path("login") String login);
}
