package com.redes.proyecto.inventorynfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void showInventory(View view) {
        Intent getInventoryScreenIntent = new Intent(this, Inventory.class);

        final int result = 1;
        //startActivityForResult(getInventoryScreenIntent, result);
        startActivity(getInventoryScreenIntent);
    }

    public void onAddNFCProducts(View view) {
        Intent readNFCScreenIntent = new Intent(this, ReadNFC.class);
        final int result = 1;
        startActivity(readNFCScreenIntent);
    }
}
