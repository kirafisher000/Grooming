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

public class ServicesAdapterAdmin extends ArrayAdapter<ListAdmin> {

    // Конструктор адаптера
    public ServicesAdapterAdmin(@NonNull Context context, ArrayList<ListAdmin> data) {
        super(context, R.layout.list_item_admin, data);  // Передаем данные в базовый класс ArrayAdapter
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Получаем элемент по позиции
        ListAdmin listData = getItem(position);

        // Если представление не создано, создаем его
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_admin, parent, false);
        }

        // Находим элементы UI для каждого элемента списка
        TextView listPhone = convertView.findViewById(R.id.listTitle);
        TextView textTitle = convertView.findViewById(R.id.listService);
        //TextView textPrice = convertView.findViewById(R.id.listTitle);

        // Устанавливаем данные в элементы UI
        if (listData != null) {
            listPhone.setText("+"+listData.getPhone());
            textTitle.setText(listData.getTitle());
            //textPrice.setText(listData.getPrice());
        }

        return convertView;
    }
}
