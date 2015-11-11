package com.redes.proyecto.inventorynfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGetNameClick(View view) {
        Intent getNameScreenIntent = new Intent(this, AddProduct.class);

        final int result = 1;
        startActivityForResult(getNameScreenIntent, result);
        //startActivity(getNameScreenIntent);
    }

    public void showInventory(View view) {
        Intent getInventoryScreenIntent = new Intent(this, Inventory.class);

        final int result = 1;
        //startActivityForResult(getInventoryScreenIntent, result);
        startActivity(getInventoryScreenIntent);
    }
}
