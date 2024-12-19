package com.example.grooming;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookActivity extends AppCompatActivity {
    EditText editDate;
    EditText editTime;
    ArrayList<String> TitleList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner spinner;
    String SelecterService;
    int userId;
    String selectedDate;
    String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        userId = SharedPrefManager.getInstance(getApplicationContext()).getUserID();

        // Инициализация EditText
        editDate = findViewById(R.id.editTextDate);
        editTime = findViewById(R.id.editTextTime);

        // Устанавливаем слушатель нажатия
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendar();  // Вызываем метод открытия календаря
            }
        });
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTime();  // Вызываем метод открытия времени
            }
        });

        // Получаем данные для Spinner
        getList();

        // Создаем адаптер для Spinner
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TitleList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // Устанавливаем стиль выпадающего списка

        // Инициализация Spinner и установка адаптера
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                SelecterService = item;
                Toast.makeText(BookActivity.this, "Selected Item: " + item, Toast.LENGTH_LONG).show();
                TextView textView = findViewById(R.id.textView7);
                textView.setText(item + " " + userId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Необходимо для корректной работы адаптера
            }
        });
    }

    private void openCalendar() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Форматируем и устанавливаем дату в EditText
                // Месяц начинается с 0, поэтому добавляем 1 к месяцу
                editDate.setText(year + "-" + (month + 1) + "-" + day);
                selectedDate = year + "-" + (month + 1) + "-" + day;
            }
        }, 2024, 9, 9);

        dialog.show();  // Показываем диалог
    }

    private void openTime() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                editTime.setText(hours + ":" + minutes);
                selectedTime = hours + ":" + minutes;
            }
        }, 15, 30, true);

        dialog.show();  // Показываем диалог
    }

    private void getList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GET_PRICELIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONArray priceListArray = jsonObject.getJSONArray("list");

                                // Обрабатываем каждый объект из массива
                                for (int i = 0; i < priceListArray.length(); i++) {
                                    JSONObject priceItem = priceListArray.getJSONObject(i);
                                    String title = priceItem.getString("title");

                                    // Добавляем данные в список
                                    TitleList.add(title);
                                }

                                // После добавления всех данных уведомляем адаптер об изменении данных
                                adapter.notifyDataSetChanged();  // Обновляем Spinner
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(BookActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                });

        // Отправляем запрос
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void BookService() {
        // Проверяем, выбраны ли все необходимые данные
        if (SelecterService == null || selectedDate == null || selectedTime == null) {
            Toast.makeText(BookActivity.this, "Please select all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_BOOK_SERVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(BookActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_acc", String.valueOf(userId));
                params.put("title", SelecterService);
                params.put("date", selectedDate);
                params.put("time", selectedTime);
                return params;
            }
        };

        // Отправляем запрос
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void onBookClick(View view) {
        BookService();
        finish();
        startActivity(new Intent(this,MenuActivity.class));
    }
}
