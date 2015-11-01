package com.pankra.gitrepolist;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Serge on 01.11.2015.
 */
public class AuthDialogFragment extends DialogFragment {
    public interface AuthListener {
        public void onAuthSubmit(String login, String pass);
    }

    AuthListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AuthListener) activity;
        } catch (ClassCastException e) {
            Log.e("AuthDialog", activity.getClass().getName() + " must implement AuthListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.auth_dialog_title);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_signin, null);
        final EditText loginEditText = (EditText) v.findViewById(R.id.username);
        final EditText passEditText = (EditText) v.findViewById(R.id.password);

        builder.setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onAuthSubmit(loginEditText.getText().toString(), passEditText.getText().toString());
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthDialogFragment.this.getDialog().cancel();
                    }
                });
    return builder.create();
    }
}
