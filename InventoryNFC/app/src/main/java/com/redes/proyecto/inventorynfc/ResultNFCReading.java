package com.redes.proyecto.inventorynfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Luis Martinez on 14/11/2015.
 */
public class ResultNFCReading extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_nfc_product_data);
    }

    //TODO: agregar la data del producto a la db
    public void addNFCProductDataToDB(View view) {
        Intent goingBack = new Intent();
        setResult(RESULT_OK, goingBack);

        finish();
    }
}
