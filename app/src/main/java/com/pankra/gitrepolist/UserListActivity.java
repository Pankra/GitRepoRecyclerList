package com.pankra.gitrepolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


/**
 * An activity representing a list of Users. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link UserDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class UserListActivity extends AppCompatActivity
        implements RecyclerUserListFragment.UserListCallback, AuthDialogFragment.AuthListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private String mLogin;
    private String mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle arguments = new Bundle();
        arguments.putString(UserDetailFragment.AUTH_LOGIN, mLogin);
        arguments.putString(UserDetailFragment.AUTH_PASS, mPass);
        RecyclerUserListFragment fragment = new RecyclerUserListFragment();
        fragment.setArguments(arguments);
        transaction.replace(R.id.user_list_container, fragment);
        transaction.commit();


        if (findViewById(R.id.user_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            ActionBar bar = getSupportActionBar();
            bar.setCustomView(R.layout.seach_layout);
            EditText search = (EditText) bar.getCustomView().findViewById(R.id.txt_search);

            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    replaceDetailFragment(v.getText().toString());
                    v.setText(null);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return false;
                }
            });

            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);

        }
    }


    /**
     * Callback method from {@link RecyclerUserListFragment.UserListCallback}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            replaceDetailFragment(id);

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, UserDetailActivity.class);
            detailIntent.putExtra(UserDetailFragment.USER_LOGIN, id);
            detailIntent.putExtra(UserDetailFragment.AUTH_LOGIN, mLogin);
            detailIntent.putExtra(UserDetailFragment.AUTH_PASS, mPass);
            startActivity(detailIntent);
        }
    }

    private void replaceDetailFragment(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(UserDetailFragment.USER_LOGIN, id);
        arguments.putString(UserDetailFragment.AUTH_LOGIN, mLogin);
        arguments.putString(UserDetailFragment.AUTH_PASS, mPass);
        UserDetailFragment fragment = new UserDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.user_detail_container, fragment)
                .commit();
    }

    @Override
    public void onAuthSubmit(String login, String pass) {
        mLogin = login;
        mPass = pass;
        Log.d("UserListActivivty: ", "LOGIN: " + login);
        Log.d("UserListActivivty: ", "PASS: " + pass);
    }
}
