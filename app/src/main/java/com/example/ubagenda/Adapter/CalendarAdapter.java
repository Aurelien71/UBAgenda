package com.example.ubagenda.Adapter;

import android.graphics.Color;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubagenda.CalendarViewHolder;
import com.example.ubagenda.R;
import com.example.ubagenda.Util.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<LocalDate> jours;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> jours, OnItemListener onItemListener) {

        this.jours = jours;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cellule_calendrier, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(jours.size()>15) {
            layoutParams.height = (int) (parent.getHeight() * 0.1);
        }else {
            layoutParams.height = (int) parent.getHeight();
        }

        return new CalendarViewHolder(view, onItemListener, jours);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        final LocalDate localDate = jours.get(position);
        if(localDate == null) {
            holder.jourDuMois.setText("");
        }
        else {
            holder.jourDuMois.setText(String.valueOf(localDate.getDayOfMonth()));
            if(localDate.equals(CalendarUtils.selectedDate)){
                holder.parentView.setBackgroundColor(Color.rgb(232, 233, 235));

            }

        }

    }

    @Override
    public int getItemCount() {

        return jours.size();

    }

    public interface OnItemListener{

        void onItemClick(int position, LocalDate localDate);

    }
}
