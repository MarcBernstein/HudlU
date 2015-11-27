package last.first.hudlu;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import last.first.hudlu.models.MashableNews;
import last.first.hudlu.models.MashableNewsItem;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = MyAdapter.class.getName();

    private final RequestQueue mRequestQueue;
    private OnAdapterInteractionListener mListener;
    private List<MashableNewsItem> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, List<MashableNewsItem> myDataset) {
        mDataset = myDataset;
        mListener = (OnAdapterInteractionListener) context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class4, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MashableNewsItem mashableNewsItem = mDataset.get(position);
        holder.mTitleText.setText(mashableNewsItem.title);
        holder.mAuthorText.setText(mashableNewsItem.author);

        mRequestQueue.add(new ImageRequest(mashableNewsItem.image, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.mImageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        }));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleText;
        TextView mAuthorText;
        ImageView mImageView;

        public MyViewHolder(View v) {
            super(v);
            mTitleText = (TextView) v.findViewById(R.id.item_title);
            mAuthorText = (TextView) v.findViewById(R.id.item_author);
            mImageView = (ImageView) v.findViewById(R.id.item_image);
        }
    }

    public interface OnAdapterInteractionListener {
        void onItemClicked(View view, int position);
    }

}
