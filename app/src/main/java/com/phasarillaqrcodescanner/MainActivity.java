package com.phasarillaqrcodescanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewNama, textViewKelas, textViewNim;

    private IntentIntegrator qrScan = new IntentIntegrator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewNama = (TextView) findViewById(R.id.textViewNama);
        textViewKelas = (TextView) findViewById(R.id.textViewKelas);
        textViewNim = (TextView) findViewById(R.id.textViewNim);

        qrScan = new IntentIntegrator(this);

        buttonScan.setOnClickListener(this);

    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
       if (result != null) {
           if (result.getContents() == null) {
               Toast.makeText(this, "hasil SCAN tidak ada", Toast.LENGTH_LONG).show();
           } else {
               try {
                   JSONObject obj = new JSONObject(result.getContents());
                   textViewNama.setText(obj.getString("nama"));
                   textViewKelas.setText(obj.getString("kelas"));
                   textViewNim.setText(obj.getString("nim"));
               } catch (JSONException e) {
                   e.printStackTrace();
                   Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                   String url = new String(result.getContents());
                   String address;
                   String http = "http://";
                   String https = "https://";
                   address = new String(result.getContents());
                   if (address.contains(http) || address.contains(https)) {
                       Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                       startActivity(browserIntent);
                   }

                   String number;
                   number = new String(result.getContents());

                   if (number.matches("^[0-9]*$") && number.length() > 11) {
                       Intent callIntent = new Intent(Intent.ACTION_DIAL);
                       Intent dialIntent = new Intent(Intent.ACTION_CALL);
                       dialIntent.setData(Uri.parse("tel:" + number));
                       callIntent.setData(Uri.parse("tel:" + number));
                       startActivity(callIntent);
                       startActivity(dialIntent);
                   }
               }
           }
               }else{
           super.onActivityResult(requestCode, resultCode, data);
       }
   }
   @Override
    public void onClick(View view) {
        qrScan.initiateScan();
    }
    }