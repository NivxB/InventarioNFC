package com.redes.proyecto.inventorynfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
public class Orders extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_orders);
    }
    @Override
    protected void onResume(){
        super.onResume();
        fillTable();
    }
    public void addOrderToDB(View view) {
        Intent getAddProductScreenIntent = new Intent(this, AddProduct.class);
        //startActivityForResult(getAddProductScreenIntent, result);
        startActivity(getAddProductScreenIntent);

    }
    public void fillTable(){
        final Orders self=this;
        String url="http://fia.unitec.edu:8082/InventarioRedes/phpFiles/getOrders.php";
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
                            JSONArray orders = new JSONArray(finalResponse);
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject invRegiter = new JSONObject(orders.get(i).toString());
                                String code = invRegiter.getString("order_number");
                                JSONArray products = invRegiter.getJSONArray("products");
                                String productsNames="";
                                String date = invRegiter.getString("order_date");
                                double total=0;
                                for(int j=0;j<products.length();j++){
                                    JSONObject prodRegister = products.getJSONObject(j);
                                    if(j<products.length()-1) {
                                        productsNames += prodRegister.getString("product_name") + ", ";
                                    }else{
                                        productsNames += prodRegister.getString("product_name");
                                    }
                                    total+=prodRegister.getDouble("purchase_price");
                                }
                                renderTable(self, code, productsNames, date, total, i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    public void renderTable(Context self,String code,String products,String date,Double total, int i){
        TableLayout tl = (TableLayout) findViewById(R.id.orders_table_layout);
        TableRow tr = new TableRow(self);

        TextView labelCode = new TextView(self);
        labelCode.setText(code);
        labelCode.setPadding(2, 0, 5, 0);
        tr.addView(labelCode);

        TextView labelProducts = new TextView(self);
        labelProducts.setText(products);
        labelProducts.setPadding(2, 0, 5, 0);
        tr.addView(labelProducts);

        TextView labelDate = new TextView(self);
        labelDate.setText(date);
        labelDate.setPadding(2, 0, 5, 0);
        tr.addView(labelDate);

        TextView labelTotal = new TextView(self);
        labelTotal.setText(""+total);
        labelTotal.setPadding(2, 0, 5, 0);
        tr.addView(labelTotal);

        tl.addView(tr, i + 1);

    }
}
