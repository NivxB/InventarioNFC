package com.redes.proyecto.inventorynfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Luis Martinez on 09/11/2015.
 */
public class Inventory extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_inventory);
    }
    @Override
    protected void onResume(){
        super.onResume();
        TableLayout tl = (TableLayout)findViewById(R.id.inventory_table_layout);
        fillTable();
    }
    public void addProductToDB(View view) {
        Intent getAddProductScreenIntent = new Intent(this, AddProduct.class);
        final int result = 1;
        //startActivityForResult(getAddProductScreenIntent, result);
        startActivity(getAddProductScreenIntent);

    }
    public void fillTable(){
        final Inventory self=this;
        String url="http://fia.unitec.edu:8082/InventarioRedes/phpFiles/getInventory.php";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
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

                final String finalResponse=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray inventory = new JSONArray(finalResponse);
                            for (int i = 0; i < inventory.length(); i++) {
                                JSONObject invRegiter = new JSONObject(inventory.get(i).toString());
                                String barcode = invRegiter.getString("product_barcode");
                                String name = invRegiter.getString("product_name");
                                String category = invRegiter.getString("category_name");
                                renderTable(self, barcode, name, category, i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    public void renderTable(Context self,String barcode,String name,String category, int i){
        TableLayout tl = (TableLayout) findViewById(R.id.inventory_table_layout);
        TableRow tr = new TableRow(self);

        TextView labelBarcode = new TextView(self);
        labelBarcode.setText(barcode);
        labelBarcode.setPadding(2, 0, 5, 0);
        tr.addView(labelBarcode);

        TextView labelName = new TextView(self);
        labelName.setText(name);
        labelName.setPadding(2, 0, 5, 0);
        tr.addView(labelName);

        TextView labelCategory = new TextView(self);
        labelCategory.setText(category);
        labelCategory.setPadding(2, 0, 5, 0);
        tr.addView(labelCategory);

        tl.addView(tr, i + 1);

    }
}
