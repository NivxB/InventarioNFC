package com.redes.proyecto.inventorynfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import javax.xml.transform.Result;

/**
 * Created by Luis Martinez on 14/11/2015.
 */
public class ResultNFCReading extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_nfc_product_data);
    }

    @Override
    protected void onResume(){
        super.onResume();
        getFields();
    }

    public void getFields(){
        Bundle extras = getIntent().getExtras();
        String barcode="";
        if(extras == null) {
            barcode= "";
        } else {
            barcode= extras.getString("product_barcode");
        }
        String url="http://fia.unitec.edu:8082/InventarioRedes/phpFiles/getInventoryByBarcode.php";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("product_barcode", barcode)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                final String finalResponse = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray inventory = new JSONArray(finalResponse);
                            for (int i = 0; i < inventory.length(); i++) {
                                JSONObject invRegister = new JSONObject(inventory.get(i).toString());
                                String product_name = invRegister.getString("product_name");
                                String purchase_price = invRegister.getString("purchase_price");
                                String sell_price = invRegister.getString("sell_price");
                                String category_name = invRegister.getString("category_name");
                                String quantity = invRegister.getString("quantity");
                                renderFields(product_name, purchase_price, sell_price, category_name, quantity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    public void renderFields(String product_name, String purchase_price,String sell_price,String category_name,String quantity){
        Bundle extras = getIntent().getExtras();
        String barcode="";
        if(extras == null) {
            barcode= "";
        } else {
            barcode= extras.getString("product_barcode");
        }
        TextView txtBarcode= (TextView) findViewById(R.id.read_nfc_barcode);
        txtBarcode.setText(barcode);
        txtBarcode.setEnabled(false);

        TextView txtName= (TextView) findViewById(R.id.read_nfc_product_name);
        txtName.setText(product_name);
        txtName.setEnabled(false);

        TextView txtPurchase= (TextView) findViewById(R.id.read_nfc_purchase_price);
        txtPurchase.setText(purchase_price);
        txtPurchase.setEnabled(false);

        TextView txtSell= (TextView) findViewById(R.id.read_nfc_sell_price);
        txtSell.setText(sell_price);
        txtSell.setEnabled(false);

        TextView txtCategory= (TextView) findViewById(R.id.read_nfc_catergory);
        txtCategory.setText(category_name);
        txtCategory.setEnabled(false);

        TextView txtQuantity= (TextView) findViewById(R.id.read_nfc_quantity);
        txtQuantity.setText(quantity);
        txtQuantity.setEnabled(false);

    }
}
