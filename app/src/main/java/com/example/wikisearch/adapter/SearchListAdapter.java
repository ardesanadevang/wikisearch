package com.example.wikisearch.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wikisearch.R;
import com.example.wikisearch.activity.WebViewActivity;
import com.example.wikisearch.model.WikiModel;
import com.example.wikisearch.util.Util;
import com.google.gson.Gson;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {

    private List<WikiModel> wikiModelList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView image;
        ConstraintLayout root;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            image = (ImageView) view.findViewById(R.id.image);
            root = (ConstraintLayout) view.findViewById(R.id.root);
        }
    }

    public SearchListAdapter(Activity activity, List<WikiModel> wikiModelList) {
        this.activity = activity;
        this.wikiModelList = wikiModelList;
    }

    public void updateList(List<WikiModel> wikiModelList) {
        this.wikiModelList = wikiModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_search_list, parent, false);

        return new MyViewHolder(itemView);
    }

    Gson gson = new Gson();

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final WikiModel wikiModel = wikiModelList.get(position);
        Util.putInLog("posi := " + position + "  data:- " + gson.toJson(wikiModel));
        if (wikiModel.getTitle() != null)
            holder.title.setText(wikiModel.getTitle());

        if (wikiModel.getTerms() != null && wikiModel.getTerms().getDescription().size() > 0)
            holder.description.setText(wikiModel.getTerms().getDescription().get(0));

        if (wikiModel.getThumbnail() != null) {
            Util.putInLog("url :- " + wikiModel.getThumbnail().getSource());
            Glide.with(activity)
                    .load(wikiModel.getThumbnail().getSource())
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(holder.image);
        } else {
            holder.image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_user));
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, WebViewActivity.class);
                i.putExtra("pageId", wikiModel.getPageid());
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wikiModelList.size();
    }
}