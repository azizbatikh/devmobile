package com.example.power_home;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private List<DayItem> days;
    private Map<String, Integer> consommationMap;


    public CalendarAdapter(List<DayItem> days, Map<String, Integer> consommationMap) {
        this.days = days;
        this.consommationMap = consommationMap;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_day, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        // Gérer les espaces vides
        if (days.get(position) == null) {
            // Rendre l'élément invisible ou vide
            holder.cardView.setVisibility(View.INVISIBLE);
            holder.dayText.setText("");
            return;
        }

        DayItem dayItem = days.get(position);
        String dateStr = String.format("%04d-%02d-%02d", dayItem.getYear(), dayItem.getMonth(), dayItem.getDay());

        holder.cardView.setVisibility(View.VISIBLE);
        holder.dayText.setText(String.valueOf(dayItem.getDay()));

        // Couleur par défaut
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(
                holder.itemView.getContext(), R.color.blue));

        // Vérifier la consommation
        if (consommationMap.containsKey(dateStr)) {
            int consommation = consommationMap.get(dateStr);
            int colorResId = consommation <= 30 ? R.color.low_consumption :
                    consommation <= 70 ? R.color.medium_consumption :
                            R.color.high_consumption;

            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(
                    holder.itemView.getContext(), colorResId));
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;
        CardView cardView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}