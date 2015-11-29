package com.redes.proyecto.inventorynfc;

/**
 * Created by Kevin Barahona on 11/15/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Background task for reading the data. Do not block the UI thread while reading.
 *
 * @author Ralf Wondratschek
 *
 */
public class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    private Activity Actividad;

    public NdefReaderTask(Activity actividad) {
        Actividad = actividad;
    }

    @Override
    protected String doInBackground(Tag... params) {
        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.e("TAG", "Unsupported Encoding", e);
                }
            }
        }

        return null;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String UTF8 = "UTF-8";
        String UTF16 = "UTF-16";
        String textEncoding = ((payload[0] & 128) == 0) ? UTF8 : UTF16;

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            //Toast.makeText(Actividad,result,Toast.LENGTH_LONG).show();
             String[] nfcTagParams=result.split(",");
            if(nfcTagParams.length==3){
                final JSONObject json=new JSONObject();
                try {
                    json.put("order_number",nfcTagParams[0]);
                    json.put("product_barcode",nfcTagParams[1]);
                    json.put("quantity",nfcTagParams[2]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Thread t=new Thread(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            OkHttpClient client = new OkHttpClient();
                            final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
                            //String stringJson=json.toString();
                            //RequestBody body = RequestBody.create(JSON, stringJson);
                            RequestBody formBody = new FormEncodingBuilder()
                                    .add("order_number", json.getString("order_number"))
                                    .add("product_barcode", json.getString("product_barcode"))
                                    .add("quantity", json.getString("quantity"))
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://fia.unitec.edu:8082/InventarioRedes/phpFiles/readNFC.php")
                                    .post(formBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String message=response.body().string();
                            //Log.d("Actividad", "C: Connecting...");

                            Actividad.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(Actividad, message.substring(2,message.length()), Toast.LENGTH_LONG).show();
                                        if (message.startsWith("4")) {
                                            Intent getAddProductScreenIntent = new Intent(Actividad, AddProduct.class);
                                            getAddProductScreenIntent.putExtra("product_barcode", json.getString("product_barcode"));
                                            Actividad.startActivity(getAddProductScreenIntent);
                                        }
                                        if(message.startsWith("1")){
                                            showNFCData(json.getString("product_barcode"));
                                        }
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }else{
                Toast.makeText(Actividad,"La etiqueta no tiene los parámetros adecuados",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void showNFCData(String productBarcode) {
        Intent showProductDataScreenIntent = new Intent(Actividad, ResultNFCReading.class);
        showProductDataScreenIntent.putExtra("product_barcode",productBarcode);
        Actividad.startActivity(showProductDataScreenIntent);
    }

}