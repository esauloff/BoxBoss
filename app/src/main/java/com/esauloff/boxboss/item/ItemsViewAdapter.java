package com.esauloff.boxboss.item;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esauloff.boxboss.R;
import com.esauloff.boxboss.model.Item;

import java.util.List;

public class ItemsViewAdapter extends RecyclerView.Adapter<ItemsViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayout;
        public TextView nameLabel;
        public TextView createdDateLabel;

        public ViewHolder(View view) {
            super(view);

            this.relativeLayout = view.findViewById(R.id.item);
            this.nameLabel = view.findViewById(R.id.lbl_name);
            this.createdDateLabel = view.findViewById(R.id.lbl_createdDate);
        }

        public void bind(final Item item, final OnItemClickListener listener) {
            relativeLayout.setBackgroundColor(item.getColor());
            nameLabel.setText(item.getName());
            createdDateLabel.setText(item.getCreatedDate().toString());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    private final List<Item> items;
    private final OnItemClickListener listener;

    public ItemsViewAdapter(List<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_items_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

