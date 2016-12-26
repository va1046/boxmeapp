package com.example.vamshi.boxmeapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vamshi.boxmeapp.Util.NetworkUtil;
import com.example.vamshi.boxmeapp.R;
import com.example.vamshi.boxmeapp.Model.Repodetails;
import com.example.vamshi.boxmeapp.activity.ViewRepoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.vamshi.boxmeapp.activity.HomeActivty.INT_KEY;
import static com.example.vamshi.boxmeapp.activity.HomeActivty.PAR_KEY;

/**
 * Created by vamshi on 16-12-2016.
 */

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.CustomViewHolder> {

    NoNetwork noNetwork;
    Context context;
    List<Repodetails> repodetailses;
    public RepoListAdapter(Context context, List<Repodetails> repodetailses) {
        this.context = context;
        this.repodetailses = repodetailses;
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repoitem, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        Repodetails repodetails = repodetailses.get(position);
        Pair<View, String> pair1 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mProfilePic.setTransitionName("profile" + position);
            pair1 = Pair.create((View) holder.mProfilePic, holder.mProfilePic.getTransitionName());
        }
        holder.mRepoTitle.setText(repodetails.getTitle());
        holder.mProfilePic.getDrawable().setColorFilter(repodetails.getColor(), PorterDuff.Mode.MULTIPLY);
        holder.mRepodescription.setText(repodetails.getDescription());
        final Pair<View, String> finalPair = pair1;
        holder.mLLrepoitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noNetwork = (NoNetwork) context;
                if (!NetworkUtil.getConnectivityStatusString(context)) {
                    noNetwork.nonetwork();
                } else {

                    Intent intent = new Intent(context, ViewRepoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(PAR_KEY, repodetailses.get(position));
                    bundle.putInt(INT_KEY, position);
                    intent.putExtras(bundle);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, finalPair);
                    context.startActivity(intent, options.toBundle());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (repodetailses != null)
            return repodetailses.size();
        else
            return 0;
    }

    public interface NoNetwork {
        void nonetwork();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_repoitem)
        LinearLayout mLLrepoitem;
        @BindView(R.id.repoimageview)
        ImageView mProfilePic;
        @BindView(R.id.repotitle)
        TextView mRepoTitle;
        @BindView(R.id.repodescription)
        TextView mRepodescription;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
