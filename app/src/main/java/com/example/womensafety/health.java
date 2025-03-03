package com.example.womensafety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class health extends AppCompatActivity {

    EditText quantityText1, quantityText2, quantityText3, quantityText4, quantityText5, quantityText6, grandTotalText;
    ImageButton addButton1, reduceButton1, addButton2, reduceButton2, addButton3, reduceButton3, addButton4, reduceButton4;
    ImageButton addButton5, reduceButton5, addButton6, reduceButton6;
    MaterialCardView item1, item2, item3, item4, item5, item6;
    ImageView biller;

    private final int priceItem1 = 10, priceItem2 = 20, priceItem3 = 3, priceItem4 = 50, priceItem5 = 95, priceItem6 = 5;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        quantityText1 = findViewById(R.id.quantity_text);
        addButton1 = findViewById(R.id.add1);
        reduceButton1 = findViewById(R.id.reduce1);
        item1 = findViewById(R.id.item1);

        quantityText2 = findViewById(R.id.quantity_text2);
        addButton2 = findViewById(R.id.add2);
        reduceButton2 = findViewById(R.id.reduce2);
        item2 = findViewById(R.id.item2);

        quantityText3 = findViewById(R.id.quantity_text3);
        addButton3 = findViewById(R.id.add3);
        reduceButton3 = findViewById(R.id.reduce3);
        item3 = findViewById(R.id.item3);

        quantityText4 = findViewById(R.id.quantity_text4);
        addButton4 = findViewById(R.id.add4);
        reduceButton4 = findViewById(R.id.reduce4);
        item4 = findViewById(R.id.item4);

        quantityText5 = findViewById(R.id.quantity_text5);
        addButton5 = findViewById(R.id.add5);
        reduceButton5 = findViewById(R.id.reduce5);
        item5 = findViewById(R.id.item5);

        quantityText6 = findViewById(R.id.quantity_text6);
        addButton6 = findViewById(R.id.add6);
        reduceButton6 = findViewById(R.id.reduce6);
        item6 = findViewById(R.id.item6);

        grandTotalText = findViewById(R.id.grand_total);

        // Biller ImageView setup
        biller = findViewById(R.id.biller);
        biller.setOnClickListener(v -> {
            Intent intent = new Intent(health.this, MainActivity.class);
            startActivity(intent);
        });

        setupQuantityListeners(quantityText1, addButton1, reduceButton1, priceItem1);
        setupQuantityListeners(quantityText2, addButton2, reduceButton2, priceItem2);
        setupQuantityListeners(quantityText3, addButton3, reduceButton3, priceItem3);
        setupQuantityListeners(quantityText4, addButton4, reduceButton4, priceItem4);
        setupQuantityListeners(quantityText5, addButton5, reduceButton5, priceItem5);
        setupQuantityListeners(quantityText6, addButton6, reduceButton6, priceItem6);

        item1.setOnClickListener(v -> Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show());
        item2.setOnClickListener(v -> Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show());
        item3.setOnClickListener(v -> Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show());
        item4.setOnClickListener(v -> Toast.makeText(this, "Item 4 selected", Toast.LENGTH_SHORT).show());
        item5.setOnClickListener(v -> Toast.makeText(this, "Item 5 selected", Toast.LENGTH_SHORT).show());
        item6.setOnClickListener(v -> Toast.makeText(this, "Item 6 selected", Toast.LENGTH_SHORT).show());
    }

    private void setupQuantityListeners(EditText quantityText, ImageButton addButton, ImageButton reduceButton, int itemPrice) {
        addButton.setOnClickListener(v -> {
            int quantity = getQuantity(quantityText);
            quantity++;
            quantityText.setText(String.valueOf(quantity));
            updateGrandTotal();
        });

        reduceButton.setOnClickListener(v -> {
            int quantity = getQuantity(quantityText);
            if (quantity > 0) {
                quantity--;
            }
            quantityText.setText(String.valueOf(quantity));
            updateGrandTotal();
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateGrandTotal() {
        int quantity1 = getQuantity(quantityText1);
        int quantity2 = getQuantity(quantityText2);
        int quantity3 = getQuantity(quantityText3);
        int quantity4 = getQuantity(quantityText4);
        int quantity5 = getQuantity(quantityText5);
        int quantity6 = getQuantity(quantityText6);

        int total = (quantity1 * priceItem1) + (quantity2 * priceItem2) + (quantity3 * priceItem3) +
                (quantity4 * priceItem4) + (quantity5 * priceItem5) + (quantity6 * priceItem6);

        grandTotalText.setText(String.format("₹ %d", total));
    }

    private int getQuantity(EditText quantityText) {
        String quantityStr = quantityText.getText().toString();
        if (quantityStr.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
