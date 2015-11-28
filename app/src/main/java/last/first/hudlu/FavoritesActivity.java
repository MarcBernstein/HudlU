package last.first.hudlu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import last.first.hudlu.models.Favorite;

public class FavoritesActivity extends AppCompatActivity implements MyFavoriteAdapter.OnAdapterInteractionListener{
    private List<Favorite> myDataset = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        mAdapter = new MyFavoriteAdapter(this, myDataset);
        mRecyclerView.setAdapter(mAdapter);

        RealmResults<Favorite> favorites = FavoriteUtil.getAllFavorites(this);
        myDataset.addAll(favorites);
        mAdapter.notifyDataSetChanged();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClicked(View view, int position) {
        Favorite item = myDataset.get(position);
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(item.getLink()));
        startActivity(browserIntent);
    }
}
