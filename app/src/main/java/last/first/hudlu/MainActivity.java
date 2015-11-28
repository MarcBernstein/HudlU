package last.first.hudlu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import last.first.hudlu.models.MashableNews;
import last.first.hudlu.models.MashableNewsItem;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnAdapterInteractionListener {
    private static final String TAG = MainActivity.class.getName();
    private static final String MASHABLE_URL = "http://mashable.com/stories.json?hot_per_page=0&new_per_page=5&rising_per_page=0";
    private static final String SHARED_PREFS = "hudlu_prefs";
    private static final String SHARED_PREFS_FIRST_START = "hudlu_prefs_first_start";

    private List<MashableNewsItem> myDataset = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, myDataset);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(SHARED_PREFS_FIRST_START, true)) {
            sharedPreferences.edit().putBoolean(SHARED_PREFS_FIRST_START, false).apply();
            showFirstRunDialog();
        }

        fetchLatestNews();
    }

    private void showFirstRunDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome to Homework 4")
                .setMessage("This is showing because its the first homework 4 has been run!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(View view, int position) {
        MashableNewsItem item = myDataset.get(position);
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(item.link));
        startActivity(browserIntent);
    }

    private void fetchLatestNews() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // you've got an internet connection so let's get the latest news from mashable!
            Toast.makeText(this, R.string.fetch_latest_news, Toast.LENGTH_LONG).show();

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest mashableNewsRequest = new StringRequest(Request.Method.GET, MASHABLE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    MashableNews mashableNews = new Gson().fromJson(response, MashableNews.class);
                    Log.d(TAG, String.format("First title %s", mashableNews.newsItems.get(0).title));
                    myDataset.addAll(mashableNews.newsItems);
                    mAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                }
            });

            requestQueue.add(mashableNewsRequest);
        } else {
            // bummer no connection! tell the user!
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }
}
