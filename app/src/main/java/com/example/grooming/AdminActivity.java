package com.example.grooming;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class AdminActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_DETAIL = 1;
    ServicesAdapterAdmin servicesAdapterAdmin;
    ArrayList<ListAdmin> dataArrayList = new ArrayList<>();
    ListView book_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Проверка на авторизацию
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        // Получаем список данных
        getList();

        // Инициализируем адаптер и ListView
        servicesAdapterAdmin = new ServicesAdapterAdmin(AdminActivity.this, dataArrayList);
        book_list = findViewById(R.id.price_list);
        book_list.setAdapter(servicesAdapterAdmin);

        // Настроить обработчик нажатий
        book_list.setClickable(true);
        book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Получаем выбранный элемент из dataArrayList
                ListAdmin selectedItem = dataArrayList.get(position);

                // Логирование
                Log.d("AdminActivity", "Selected Item: " + selectedItem.getTitle());

                // Создаем Intent для перехода в DetailedActivity
                Intent intent = new Intent(AdminActivity.this, DetailedActivity.class);

                // Передаем данные в Intent
                intent.putExtra("phone", selectedItem.getPhone());
                intent.putExtra("title", selectedItem.getTitle());
                intent.putExtra("price", selectedItem.getPrice());
                intent.putExtra("date", selectedItem.getDate());
                intent.putExtra("time", selectedItem.getTime());
                intent.putExtra("id", selectedItem.getId());

                // Логирование перед стартом активности
                Log.d("AdminActivity", "Starting DetailedActivity");

                // Стартуем активность
                startActivityForResult(intent, REQUEST_CODE_DETAIL); // Используем startActivityForResult для получения результата
            }
        });
    }

    // Метод для получения списка данных через Volley
    private void getList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONArray priceListArray = jsonObject.getJSONArray("list");

                                // Очистим список перед добавлением новых данных
                                dataArrayList.clear();

                                // Обрабатываем каждый объект из массива
                                for (int i = 0; i < priceListArray.length(); i++) {
                                    JSONObject priceItem = priceListArray.getJSONObject(i);
                                    String phone = priceItem.getString("phone");
                                    String title = priceItem.getString("title");
                                    String price = priceItem.getString("price");
                                    String date = priceItem.getString("date");
                                    String time = priceItem.getString("time");
                                    int id = priceItem.getInt("id");

                                    // Добавляем данные в список
                                    ListAdmin listData = new ListAdmin(id, phone, price, title, date, time);
                                    dataArrayList.add(listData);
                                }

                                // Обновляем адаптер после добавления данных
                                servicesAdapterAdmin.notifyDataSetChanged();
                            } else {
                                // Если запрос не успешен, показываем сообщение
                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("AdminActivity", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                error -> {
                    Log.e("AdminActivity", "Volley error: " + error.getMessage());
                    Toast.makeText(AdminActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                });

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    // Метод выхода из аккаунта
    public void onLogoutClick(View view) {
        SharedPrefManager.getInstance(this).logout();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK) {
            // Получаем ID удаленного элемента
            int deletedItemId = data.getIntExtra("deleted_item_id", -1);

            // Если ID валидный, удаляем элемент из списка
            if (deletedItemId != -1) {
                removeItemFromList(deletedItemId);
            }
        }
    }

    private void removeItemFromList(int itemId) {
        // Сначала удаляем элемент из списка
        ListAdmin itemToRemove = null;
        for (int i = 0; i < dataArrayList.size(); i++) {
            if (dataArrayList.get(i).getId() == itemId) {
                itemToRemove = dataArrayList.get(i); // Находим элемент для удаления
                dataArrayList.remove(i);  // Удаляем элемент из списка
                break;  // Завершаем цикл после удаления
            }
        }

        // Если элемент был найден и удален из списка, обновляем адаптер
        if (itemToRemove != null) {
            servicesAdapterAdmin.notifyDataSetChanged();  // Обновляем отображение списка
        }

        // Теперь отправляем запрос на сервер для удаления этого элемента
        final String id = String.valueOf(itemId); // Преобразуем ID в строку

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DELETE,  // URL для удаления
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                // Если запрос прошел успешно
                                Toast.makeText(getApplicationContext(), "Item deleted successfully", Toast.LENGTH_LONG).show();
                            } else {
                                // Если сервер вернул ошибку
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("AdminActivity", "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "Error deleting item", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                error -> {
                    // Обработка ошибки Volley
                    Log.e("AdminActivity", "Volley error: " + error.getMessage());
                    Toast.makeText(AdminActivity.this, "Failed to delete item: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);  // Параметр для удаления
                return params;
            }
        };

        // Отправляем запрос на сервер через RequestHandler
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
