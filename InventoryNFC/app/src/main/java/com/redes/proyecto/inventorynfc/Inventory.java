package com.redes.proyecto.inventorynfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Luis Martinez on 09/11/2015.
 */
public class Inventory extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_inventory);
    }

    public void addProductToDB(View view) {
        Intent getAddProductScreenIntent = new Intent(this, AddProduct.class);

        final int result = 1;
        //startActivityForResult(getAddProductScreenIntent, result);
        startActivity(getAddProductScreenIntent);
    }
}
