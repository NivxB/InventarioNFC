package com.redes.proyecto.inventorynfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Luis Martinez on 14/11/2015.
 */
public class ReadNFC extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.read_nfc);
    }

    //TODO: codigo para leer un sticker nfc
    public void readNFCSticker(View view) {
        //...
        Intent showProductDataScreenIntent = new Intent(this, ResultNFCReading.class);
        startActivity(showProductDataScreenIntent);
        //...
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0)
            finish();
    }
}
