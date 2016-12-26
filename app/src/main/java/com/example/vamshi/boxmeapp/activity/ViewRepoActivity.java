package com.example.vamshi.boxmeapp.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vamshi.boxmeapp.R;
import com.example.vamshi.boxmeapp.Model.Repodetails;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewRepoActivity extends AppCompatActivity {

    @BindView(R.id.repoimageview)
    ImageView mProfilePic;
    @BindView(R.id.repotitle)
    TextView mRepoTitle;
    @BindView(R.id.repodescription)
    TextView mRepodescription;
    @BindView(R.id.ownername)
    TextView mOwnername;
    @BindView(R.id.watch)
    TextView watch;
    @BindView(R.id.star)
    TextView star;
    @BindView(R.id.forks)
    TextView forks;
    @BindView(R.id.ownerpic)
    CircleImageView mOwnerPic;
    Repodetails repodetails = null;
    int position;

    private final Callback callBack = new Callback() {
        @Override
        public void onSuccess() {
            mOwnerPic.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mOwnerPic.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ViewRepoActivity.this.startPostponedEnterTransition();
                    }
                    return true;
                }
            });
        }

        @Override
        public void onError() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        setContentView(R.layout.activity_view_repo);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackground(new ColorDrawable(ContextCompat.getColor(this,R.color.colorPrimaryDark)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(savedInstanceState == null){
            repodetails = this.getIntent().getParcelableExtra(HomeActivty.PAR_KEY);
            position = this.getIntent().getIntExtra(HomeActivty.INT_KEY,0);
        }else{
            repodetails = savedInstanceState.getParcelable(HomeActivty.PAR_KEY);
            position = savedInstanceState.getInt(HomeActivty.INT_KEY);;
        }

        TextView action_bar_title = (TextView) findViewById(R.id.text_actionbar_title);
        action_bar_title.setText(repodetails.getOwner()+'/'+repodetails.getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.transition1));
                mProfilePic.setTransitionName("profile"+position);
            }
        }
        mProfilePic.getDrawable().setColorFilter(repodetails.getColor(), PorterDuff.Mode.MULTIPLY);
        mRepoTitle.setText(repodetails.getTitle());
        mRepodescription.setText(repodetails.getDescription());
        mOwnername.setText(repodetails.getOwner());
        watch.setText(""+repodetails.getWatchers());
        star.setText(""+repodetails.getStars());
        forks.setText(""+repodetails.getForks());

//        Picasso.with(this).load(repodetails.getAvatarurl()).into(mOwnerPic);
//        Picasso.with(this).load(repodetails.getAvatarurl()).placeholder(R.drawable.progress_animation).into(mOwnerPic);

        RequestCreator requestCreator = Picasso.with(this).load(repodetails.getAvatarurl());
        requestCreator.into(mOwnerPic, callBack);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
//        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(HomeActivty.PAR_KEY,repodetails);
        outState.putInt(HomeActivty.INT_KEY,position);
        super.onSaveInstanceState(outState);
    }
}
