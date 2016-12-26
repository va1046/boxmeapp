package com.example.vamshi.boxmeapp.activity;

import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vamshi.boxmeapp.broadcastreceiver.NetworkChangeReceiver;
import com.example.vamshi.boxmeapp.R;
import com.example.vamshi.boxmeapp.adapter.RepoListAdapter;
import com.example.vamshi.boxmeapp.Model.Repodetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivty extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeInterface,RepoListAdapter.NoNetwork {

    public final static String PAR_KEY = "com.example.vamshi.parcelable1";
    public final static String INT_KEY = "com.example.vamshi.int1";
    ArrayList<Repodetails> repodetailses = new ArrayList<>();
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.content_home_activty)
    RelativeLayout relativeLayout;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorLayout;
    NetworkChangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            repodetailses = this.getIntent().getParcelableArrayListExtra(MainActivity.PAR_KEY);
        }else{
            repodetailses = savedInstanceState.getParcelableArrayList(MainActivity.PAR_KEY);
        }
        setContentView(R.layout.activity_home_activty);
        networkChangeReceiver = new NetworkChangeReceiver();
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackground(new ColorDrawable(ContextCompat.getColor(this,R.color.colorPrimaryDark)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (repodetailses.size() > 0) {

            TextView action_bar_title = (TextView) findViewById(R.id.text_actionbar_title);
            action_bar_title.setText(repodetailses.get(0).getOwner());

            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            RepoListAdapter repoListAdapter = new RepoListAdapter(this, repodetailses);
            recyclerView.setAdapter(repoListAdapter);

//            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    ImageView ivProfile = (ImageView) view.findViewById(R.id.repoimageview);
////                    Intent intent = new Intent(HomeActivty.this, ViewRepoActivity.class);
////                    Bundle bundle = new Bundle();
////                    bundle.putParcelable(PAR_KEY, repodetailses.get(position));
////                    intent.putExtras(bundle);
////                    startActivity(intent);
//
//
//                }
//            }));

        } else {
            Snackbar.make(relativeLayout, "Repository empty!", Snackbar.LENGTH_LONG).show();
        }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(networkChangeReceiver != null)
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(networkChangeReceiver != null)
            unregisterReceiver(networkChangeReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MainActivity.PAR_KEY,repodetailses);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void networkavailablelistener() {
        Snackbar.make(coordinatorLayout, "Network is available!!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void networknotavailablelistener() {
        Snackbar.make(coordinatorLayout, "Network not available!!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void nonetwork() {
        Snackbar.make(coordinatorLayout, "Network not available!!", Snackbar.LENGTH_LONG).show();
    }
}
