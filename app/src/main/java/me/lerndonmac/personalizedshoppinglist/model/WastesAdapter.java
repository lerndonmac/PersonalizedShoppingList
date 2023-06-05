package me.lerndonmac.personalizedshoppinglist.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import me.lerndonmac.personalizedshoppinglist.R;

public class WastesAdapter extends ArrayAdapter<Wastes> {
    float midlCost;
        public WastesAdapter(@NonNull Context context, @NonNull List<Wastes> objects) {
            super(context, 0, objects);
            for (Wastes w:objects) {
                if (w.getCost()>0) {
                    midlCost += w.getCost();
                }
            }
            midlCost = midlCost/objects.size();
        }
        @SuppressLint({"ResourceAsColor", "SetTextI18n", "ViewHolder"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Wastes waste = getItem(position);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            if (convertView != null) {
                TextView tvName = convertView.findViewById(R.id.textview1);
                TextView tvCost = convertView.findViewById(R.id.textView2);
                if (waste.getCost()>(midlCost / 4)){
                    tvCost.setTextColor(R.color.red);
                }
                    tvName.setText(waste.getName());
                    tvCost.setText("x"+waste.getCount()+" "+ waste.getCost()* waste.getCount());
            }
            return convertView;
        }
    }