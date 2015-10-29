package com.pankra.gitrepolist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pankra.gitrepolist.R;
import com.pankra.gitrepolist.interfaces.LoadUserListener;
import com.pankra.gitrepolist.interfaces.UserListTapListener;
import com.pankra.gitrepolist.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SPankratov on 26.10.2015.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private List<User> mDataSet = new ArrayList<>();
    private Context mContext;

    private UserListTapListener mTapListener;
    private LoadUserListener mLoadUserListener;

    public UserAdapter(Context context, UserListTapListener tapListener, LoadUserListener loadListener) {
        this.mContext = context;
        mTapListener = tapListener;
        mLoadUserListener = loadListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imgView;
        private View view;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.txt);
            imgView = (ImageView) v.findViewById(R.id.img);
            view = v;
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImgView() {
            return imgView;
        }

        public View getView() {
            return view;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User item = mDataSet.get(position);
        holder.getTextView().setText(item.getLogin());
        Glide.with(mContext).load(item.getAvatar_url()).override(50, 50).into(holder.getImgView());

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item != null) {
                    mTapListener.userTap(item.getLogin());
                }
            }
        });

        if (position == mDataSet.size() - 1) {
            long lastLoadedUser = mDataSet.get(mDataSet.size() - 1).getId();
            mLoadUserListener.loadNext(lastLoadedUser);
        }


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addItems(List<User> users) {
        mDataSet.addAll(users);
        notifyDataSetChanged();
    }

}
