package com.example.grooming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.grooming.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ServicesAdapter servicesAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;
    ListView price_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_menu);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }


        getList();

        servicesAdapter = new ServicesAdapter(MenuActivity.this, dataArrayList);
        price_list = findViewById(R.id.price_list);
        price_list.setAdapter(servicesAdapter);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            servicesAdapter.notifyDataSetChanged();
        }



    }

    private void getList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GET_PRICELIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("success")){
                                JSONArray priceListArray = jsonObject.getJSONArray("list");

                                // Очистим список перед добавлением новых данных
                                dataArrayList.clear();

                                // Обрабатываем каждый объект из массива
                                for (int i = 0; i < priceListArray.length(); i++) {
                                    JSONObject priceItem = priceListArray.getJSONObject(i);
                                    String title = priceItem.getString("title");
                                    String price = priceItem.getString("price");

                                    // Добавляем данные в ваш список
                                    listData = new ListData(R.drawable.grooming, title, price);
                                    dataArrayList.add(listData);
                                }

                                // Обновляем адаптер после добавления данных
                                servicesAdapter.notifyDataSetChanged();
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
                    Toast.makeText(MenuActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                });

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }



    public void onLogoutClick (View view){
        SharedPrefManager.getInstance(this).logout();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onBookClick (View view){
        finish();
        startActivity(new Intent(this, BookActivity.class));
    }
}