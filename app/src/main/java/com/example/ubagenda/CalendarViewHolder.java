package com.example.ubagenda;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubagenda.Adapter.CalendarAdapter;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ArrayList<LocalDate> jours;
    public final View parentView;
    public final TextView jourDuMois;
    private final CalendarAdapter.OnItemListener onItemListener;

    /**
     *
     * @param itemView
     * @param onItemListener
     * @param jours
     */
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, ArrayList<LocalDate> jours ) {

        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        jourDuMois = itemView.findViewById(R.id.cellJour);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.jours = jours;

    }

    @Override
    public void onClick(View view) {

        onItemListener.onItemClick(getAdapterPosition(), jours.get(getAdapterPosition()));

    }
}
