package com.example.vamshi.boxmeapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vamshi.boxmeapp.broadcastreceiver.NetworkChangeReceiver;
import com.example.vamshi.boxmeapp.Util.NetworkUtil;
import com.example.vamshi.boxmeapp.R;
import com.example.vamshi.boxmeapp.Model.Repodetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeInterface {

    public final static String PAR_KEY = "com.example.vamshi.parcelable";
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.button1)
    Button githubbutton;
    @BindView(R.id.button2)
    Button submitbutton;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.activity_main)
    RelativeLayout relativeLayout;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.ll_username)
    LinearLayout ll_username;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    RequestQueue requestQueue;
    NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        networkChangeReceiver = new NetworkChangeReceiver();

//        registerReceiver(networkChangeReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mUsername.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.darkblue), PorterDuff.Mode.SRC_IN);
        githubbutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View view) {
                githubbutton.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                ll_username.startAnimation(animation);
                ll_username.setVisibility(View.VISIBLE);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        githubbutton.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        submitbutton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_expand_anim));
                        submitbutton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View view) {

                final String username = mUsername.getText().toString();
                if (!NetworkUtil.getConnectivityStatusString(getApplicationContext())) {
                    Snackbar.make(coordinatorLayout, "Network not available!!", Snackbar.LENGTH_LONG).show();
                } else {
                    if (!username.isEmpty()) {

                        progressBar.setVisibility(View.VISIBLE);
                        String url = "https://api.github.com/users/" + username;
                        JsonObjectRequest loginrequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
//                            Log.d(TAG, "onResponse: "+response.toString());
                                progressBar.setVisibility(View.INVISIBLE);
                                try {
                                    Log.d(TAG, "onResponse: " + response.getString("repos_url"));
                                    getrepos(response.getString("repos_url"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                progressBar.setVisibility(View.INVISIBLE);
                                Log.d(TAG, "onResponse: " + error.getMessage());
                                try {
                                    String messageString = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject jsonObject = new JSONObject(messageString);
                                    String message = jsonObject.getString("message");

                                    if (!message.isEmpty() && message.equals("Not Found")) {
                                        Snackbar.make(relativeLayout, "User not found", Snackbar.LENGTH_LONG).show();
                                    }

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, "onResponse: " + error.getLocalizedMessage());
                            }
                        });

                        requestQueue.add(loginrequest);
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter a valid email id", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getrepos(String url) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest reposrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                ArrayList<Repodetails> repodetailses = new ArrayList<>();
                Repodetails repodetails = null;
                int[] androidColors = getResources().getIntArray(R.array.androidcolors);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("name");
                            String description = jsonObject.getString("description");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("owner");
                            String repo_url = jsonObject1.getString("repos_url");
                            int watchers = jsonObject.getInt("watchers");
                            int stars = jsonObject.getInt("stargazers_count");
                            int forks = jsonObject.getInt("forks");
                            String avatar_url = jsonObject1.getString("avatar_url");
                            String owner = jsonObject1.getString("login");

                            repodetails = new Repodetails();
                            repodetails.setTitle(title);
                            repodetails.setDescription(description);
                            repodetails.setRepourl(repo_url);
                            repodetails.setColor(randomAndroidColor);
                            repodetails.setWatchers(watchers);
                            repodetails.setStars(stars);
                            repodetails.setForks(forks);
                            repodetails.setAvatarurl(avatar_url);
                            repodetails.setOwner(owner);

                            repodetailses.add(repodetails);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this, HomeActivty.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(PAR_KEY, repodetailses);
                intent.putExtras(bundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, (View) submitbutton, "fab");
                startActivity(intent, options.toBundle());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onResponse: " + error.getMessage());
                try {
                    Log.d(TAG, "onResponse: " + new String(error.networkResponse.data, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse: " + error.getLocalizedMessage());
            }
        });

        requestQueue.add(reposrequest);
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void networkavailablelistener() {
//        submitbutton.setEnabled(true);
        Snackbar.make(coordinatorLayout, "Network is available!!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void networknotavailablelistener() {
//        submitbutton.setEnabled(false);
        Snackbar.make(coordinatorLayout, "Network not available!!", Snackbar.LENGTH_LONG).show();
    }
}
