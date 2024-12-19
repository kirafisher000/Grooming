package com.example.grooming;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailedActivity extends AppCompatActivity {
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView phone = findViewById(R.id.Phone);
            TextView service = findViewById(R.id.Service);
            TextView price = findViewById(R.id.Price);
            TextView date = findViewById(R.id.Date);
            TextView time = findViewById(R.id.Time);
            phone.setText("+" + extras.getString("phone"));
            service.setText(extras.getString("title"));
            price.setText(extras.getString("price"));
            date.setText(extras.getString("date"));
            time.setText(extras.getString("time"));
            id = extras.getInt("id");
        }
    }

    public void onBackClick(View view) {
        finish();
        startActivity(new Intent(this, AdminActivity.class));
    }

    public void onDeleteClick(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("deleted_item_id", id);
        setResult(RESULT_OK, resultIntent);
        finish(); // Закрываем DetailedActivity
    }
}
