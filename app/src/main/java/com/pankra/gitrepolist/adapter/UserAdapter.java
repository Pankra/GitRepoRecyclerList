package com.pankra.gitrepolist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pankra.gitrepolist.R;
import com.pankra.gitrepolist.model.User;

import java.util.List;

import static com.pankra.gitrepolist.RecyclerUserListFragment.*;

/**
 * Created by SPankratov on 26.10.2015.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private static List<User> mDataSet;
    private Context mContext;
    private UserListCallback mUserListCallback;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final ImageView imgView;
        private UserListCallback listCallback;

        public void setListCallback(UserListCallback listCallback) {
            this.listCallback = listCallback;
        }

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.txt);
            imgView = (ImageView) v.findViewById(R.id.img);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImgView() {
            return imgView;
        }

        @Override
        public void onClick(View v) {
            Log.d("DDDD", "Element " + getLayoutPosition() + " clicked.");
            listCallback.onItemSelected(mDataSet.get(getLayoutPosition()).getLogin());
        }
    }

    public UserAdapter(Context context, List<User> mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = context;
    }

    public void setUserListCallback(UserListCallback userListCallback) {
        this.mUserListCallback = userListCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.setListCallback(mUserListCallback);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTextView().setText(mDataSet.get(position).getLogin());

        Glide.with(mContext).load(mDataSet.get(position).getAvatar_url()).override(50, 50).into(holder.getImgView());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
