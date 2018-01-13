package com.iwillow.app.samples.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iwillow.app.android.ui.view.CenterRecyclerView;
import com.iwillow.app.samples.R;

public class CenterItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_item);
        final CenterRecyclerView centerRecyclerView = (CenterRecyclerView) findViewById(R.id.recyclerView);
        centerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CenterItemAdapter adapter = new CenterItemAdapter();
        centerRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CenterItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                centerRecyclerView.smoothScrollItemToCenter(position);
                adapter.setCheckedIndex(position);
            }
        });

    }


    public static class CenterItemAdapter extends RecyclerView.Adapter<CenterItemAdapter.CenterItemViewHolder> {
        public OnItemClickListener onItemClickListener;
        private int checkedIndex;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public CenterItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_center, parent, false);
            return new CenterItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CenterItemViewHolder holder, int position) {
            holder.mTvIndex.setText("index-" + position);
            if (checkedIndex == position) {
                holder.mTvIndex.setBackgroundColor(Color.RED);
            } else {
                holder.mTvIndex.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorDefault));
            }
            final int index = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(index);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return 30;
        }

        public void setCheckedIndex(int index) {
            if (checkedIndex != index && index >= 0 && index < getItemCount()) {
                checkedIndex = index;
                notifyDataSetChanged();
            }
        }


        static class CenterItemViewHolder extends RecyclerView.ViewHolder {
            final TextView mTvIndex;

            public CenterItemViewHolder(View itemView) {
                super(itemView);
                mTvIndex = (TextView) itemView.findViewById(R.id.tv_index);
            }
        }

        interface OnItemClickListener {
            public void onItemClick(int position);
        }
    }
}
