package com.pankra.gitrepolist.model;

/**
 * Created by Serge on 24.10.2015.
 */
public class User {

    //имя
    String login;
    long id;
    //картинка
    String avatar_url;
    String html_url;
    //репозитории
    String repos_url;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getRepos_url() {
        return repos_url;
    }

    public void setRepos_url(String repos_url) {
        this.repos_url = repos_url;
    }

    @Override
    public String toString() {
        return login;
    }
}
