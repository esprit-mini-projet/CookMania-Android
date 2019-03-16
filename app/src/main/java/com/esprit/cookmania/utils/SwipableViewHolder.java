package com.esprit.cookmania.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SwipableViewHolder extends RecyclerView.ViewHolder {
    public View foregroundView;
    public View backgroundView;

    public SwipableViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
