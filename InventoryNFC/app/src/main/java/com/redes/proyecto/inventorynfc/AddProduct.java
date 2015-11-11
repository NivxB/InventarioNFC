package com.redes.proyecto.inventorynfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Luis Martinez on 09/11/2015.
 */
public class AddProduct extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_product);

        Spinner categories_spinner = (Spinner)findViewById(R.id.categories_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_spinner.setAdapter(adapter);
    }

    public void addProductToDB(View view) {
        EditText productNameEditText = (EditText) findViewById(R.id.product_name_edit_text);
        String productName = String.valueOf(productNameEditText.getText());

        Intent goingBack = new Intent();
        goingBack.putExtra("ProductName", productName);
        setResult(RESULT_OK, goingBack);

        finish();
    }
}
