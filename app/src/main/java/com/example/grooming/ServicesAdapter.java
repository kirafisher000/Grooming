package com.example.grooming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ServicesAdapter extends ArrayAdapter<ListData> {

    // Конструктор адаптера
    public ServicesAdapter(@NonNull Context context, ArrayList<ListData> data) {
        super(context, R.layout.list_item, data);  // Передаем данные в базовый класс ArrayAdapter
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Получаем элемент по позиции
        ListData listData = getItem(position);

        // Если представление не создано, создаем его
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Находим элементы UI для каждого элемента списка
        ImageView listImage = convertView.findViewById(R.id.listimage);
        TextView textTitle = convertView.findViewById(R.id.listPrice);
        TextView textPrice = convertView.findViewById(R.id.listTitle);

        // Устанавливаем данные в элементы UI
        if (listData != null) {
            listImage.setImageResource(listData.getImage());
            textTitle.setText(listData.getTitle());
            textPrice.setText(listData.getPrice());
        }

        return convertView;
    }
}
