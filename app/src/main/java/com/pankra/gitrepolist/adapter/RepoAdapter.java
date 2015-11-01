package com.pankra.gitrepolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pankra.gitrepolist.R;
import com.pankra.gitrepolist.model.Repo;

import java.util.List;

/**
 * Created by SPankratov on 26.10.2015.
 */
public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder>{
    private static List<Repo> mDataSet;
    private static Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;


        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }


        @Override
        public void onClick(View v) {
            Log.d("DDDD", "Element " + getLayoutPosition() + " clicked.");
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mDataSet.get(getLayoutPosition()).getHtml_url()));
            mContext.startActivity(i);
        }
    }

    public RepoAdapter(Context context, List<Repo> mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Repo repo = mDataSet.get(position);
        String rowText = repo.getName() +
                (repo.getDescription() == null ? "" : " (" + repo.getDescription() + ") \n") +
                (repo.getHtml_url() == null ? "" : "html_url: " + repo.getHtml_url() + "\n") +
                (repo.getOwner_url() == null ? "" : "owner: " + repo.getOwner_url());

        holder.getTextView().setText(rowText);
        if (repo.isFork()) {
            holder.getTextView().setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
