package com.travelmantics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.travelmantics.models.TravelDeal;

public class InsertActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    EditText etTitle, etPrice, etDescription, etImageUrl;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        etDescription = findViewById(R.id.etDescription);
        etImageUrl = findViewById(R.id.etImageUrl);
        etPrice = findViewById(R.id.etPrice);
        etTitle = findViewById(R.id.etTitle);
        btnSave = findViewById(R.id.btn_save);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("travelDeals");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDeal();
                clean();
            }
        });
    }

    private void clean() {
        etTitle.setText("");
        etPrice.setText("");
        etDescription.setText("");
        etImageUrl.setText("");
    }

    private void saveDeal() {
        String title = etTitle.getText().toString();
        String price = etPrice.getText().toString();
        String description = etDescription.getText().toString();
        String imageUrl = etImageUrl.getText().toString();
        TravelDeal deal = new TravelDeal(title, description, price, imageUrl);
        mDatabaseReference.push().setValue(deal);
    }
}