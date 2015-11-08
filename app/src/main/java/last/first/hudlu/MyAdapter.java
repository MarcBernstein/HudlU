package last.first.hudlu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class2, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mMyText.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mMyText;

        public MyViewHolder(View v) {
            super(v);
            mMyText = (TextView) v.findViewById(R.id.item_my_text);
        }
    }

}
