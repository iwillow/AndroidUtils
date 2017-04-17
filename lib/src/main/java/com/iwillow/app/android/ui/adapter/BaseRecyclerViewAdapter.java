package com.iwillow.app.android.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by https://github.com/iwillow/ on 2017/3/11.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    protected View headerView;
    protected View footerView;
    protected List<T> data = new ArrayList<>();

    protected OnHeaderViewClickListener onHeaderViewClickListener;
    protected OnFooterViewClickListener onFooterViewClickListener;
    protected OnItemClickListener onItemClickListener;


    public void addItems(List<T> items) {
        data.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public List<T> getAllItems() {
        return data;
    }

    public T getItem(int index) {
        return data.get(index);
    }


    public View getFooterView() {
        return footerView;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyItemInserted(0);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    public OnHeaderViewClickListener getOnHeaderViewClickListener() {
        return onHeaderViewClickListener;
    }

    public void setOnHeaderViewClickListener(OnHeaderViewClickListener onHeaderViewClickListener) {
        this.onHeaderViewClickListener = onHeaderViewClickListener;
    }

    public OnFooterViewClickListener getOnFooterViewClickListener() {
        return onFooterViewClickListener;
    }

    public void setOnFooterViewClickListener(OnFooterViewClickListener onFooterViewClickListener) {
        this.onFooterViewClickListener = onFooterViewClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER && headerView != null) {
            return createHeaderViewHolder(parent, viewType);
        }
        if (viewType == TYPE_FOOTER && footerView != null) {
            return createFooterViewHolder(parent, viewType);
        }
        return createItemViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            int pos = getActualItemPosition(holder);
            bindItemView(holder, pos);
            if (holder.itemView != null && onItemClickListener != null) {
                final RecyclerView.ViewHolder h = holder;
                final int index = position;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(h, index);
                    }
                });
            }

        } else if (holder.getItemViewType() == TYPE_HEADER && headerView != null) {
            bindHeaderView(holder, position);
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onHeaderViewClickListener != null) {
                        onHeaderViewClickListener.onHeaderViewClick(v);
                    }
                }
            });
        } else if (holder.getItemViewType() == TYPE_FOOTER && footerView != null) {
            bindFooterView(holder, position);
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFooterViewClickListener != null) {
                        onFooterViewClickListener.onFooterViewClick(v);
                    }
                }
            });
        }
    }

    public int getActualItemPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return headerView == null ? position : position - 1;
    }

    public abstract RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent, final int viewType);

    public abstract RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent, final int viewType);

    public abstract RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent, final int viewType);

    public abstract void bindItemView(RecyclerView.ViewHolder holder, int position);

    public abstract void bindHeaderView(RecyclerView.ViewHolder holder, int position);

    public abstract void bindFooterView(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        if (headerView != null && footerView != null) {
            return 2 + data.size();
        } else if (headerView == null && footerView != null) {
            return 1 + data.size();
        } else if (headerView != null && footerView == null) {
            return 1 + data.size();
        } else if (headerView == null && footerView == null) {
            return data.size();
        }
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        //有头有尾
        if (headerView != null && footerView != null) {
            if (position == 0) {
                return TYPE_HEADER;
            }
            if (data.isEmpty() || position == getItemCount() - 1) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }
        //有头无尾
        if (headerView != null && footerView == null) {
            if (data.isEmpty() || position == 0) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }
        //无头有尾
        if (headerView == null && footerView != null) {
            if (data.isEmpty() || position == getItemCount() - 1) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }
        //无头无尾
        return TYPE_ITEM;
    }


    public int getDataCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

    public interface OnHeaderViewClickListener {
        void onHeaderViewClick(View view);
    }

    public interface OnFooterViewClickListener {
        void onFooterViewClick(View view);
    }
}
