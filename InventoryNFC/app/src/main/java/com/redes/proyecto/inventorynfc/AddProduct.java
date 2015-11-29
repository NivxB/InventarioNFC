package com.redes.proyecto.inventorynfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Luis Martinez on 09/11/2015.
 */
public class AddProduct extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_product);

        TextView txtBarcode=(TextView)findViewById(R.id.barcode_edit_text);
        String barcode="";
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            barcode= "";
        } else {
            barcode= extras.getString("product_barcode");
            txtBarcode.setEnabled(false);
        }
        txtBarcode.setText(barcode);
        Spinner categories_spinner = (Spinner)findViewById(R.id.categories_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_spinner.setAdapter(adapter);
    }

    public void addProductToDB(View view) {
        if(validFields()){
            EditText productNameEditText = (EditText) findViewById(R.id.product_name_edit_text);
            String productName = String.valueOf(productNameEditText.getText());
            EditText productBarcodeEditText = (EditText) findViewById(R.id.barcode_edit_text);
            String productBarcode = String.valueOf(productBarcodeEditText.getText());
            EditText productPurchasePriceEditText = (EditText) findViewById(R.id.purchase_price_edit_text);
            Double productPurchasePrice = Double.parseDouble(productPurchasePriceEditText.getText().toString());
            EditText productSellPriceEditText = (EditText) findViewById(R.id.sell_price_edit_e);
            Double productSellPrice = Double.parseDouble(productSellPriceEditText.getText().toString());
            final Product product=new Product(0,productBarcode,productName,productPurchasePrice,productSellPrice,2);
            //Log.d("Product", product.toString());
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject(product.toJson());
                        post("http://fia.unitec.edu:8082/InventarioRedes/phpFiles/insertProduct.php", json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            Intent goingBack = new Intent();
            goingBack.putExtra("ProductName", productName);
            setResult(RESULT_OK, goingBack);
            finish();
        }
    }
    public boolean validFields(){
        try {
            EditText productNameEditText = (EditText) findViewById(R.id.product_name_edit_text);
            String productName = String.valueOf(productNameEditText.getText());
            EditText productBarcodeEditText = (EditText) findViewById(R.id.barcode_edit_text);
            String productBarcode = String.valueOf(productBarcodeEditText.getText());
            EditText productPurchasePriceEditText = (EditText) findViewById(R.id.purchase_price_edit_text);
            Double productPurchasePrice = Double.parseDouble(productPurchasePriceEditText.getText().toString());
            EditText productSellPriceEditText = (EditText) findViewById(R.id.sell_price_edit_e);
            Double productSellPrice = Double.parseDouble(productSellPriceEditText.getText().toString());
        }catch(Exception e){
            return false;
        }
        return true;
    }


    public void post(String url, JSONObject json) throws Exception {
        OkHttpClient client = new OkHttpClient();
        //final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
        //RequestBody body = RequestBody.create(JSON, json);
        RequestBody formBody = new FormEncodingBuilder()
                .add("product_barcode", json.getString("product_barcode"))
                .add("product_name", json.getString("product_name"))
                .add("product_category", json.getString("product_category"))
                .add("purchase_price", json.getString("purchase_price"))
                .add("sell_price", json.getString("sell_price"))
                .build();
        //Log.d("JSON",json);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        final Response response = client.newCall(request).execute();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String result=response.body().string();
                    if(result.split(";")[0].split(":")[1].equals("0")){
                        Toast.makeText(AddProduct.this, "El producto fue ingresado anteriormente", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(AddProduct.this, "Se ha ingresado un nuevo producto al inventario", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
