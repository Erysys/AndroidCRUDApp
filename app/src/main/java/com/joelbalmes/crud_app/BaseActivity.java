package com.joelbalmes.crud_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

  public RequestQueue requestQueue;
  static Record[] records;
  public String baseUrl = "https://jbalmesrecordsapi.azurewebsites.net/";
  Gson gson;
  ArrayList<String> itemNames = new ArrayList<>();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base);

    Cache cache = new DiskBasedCache( getCacheDir(), 1024 * 1024 );
    Network network = new BasicNetwork( new HurlStack() );

    requestQueue = new RequestQueue( cache, network );
    requestQueue.start();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    //Inflate menu
    getMenuInflater().inflate( R.menu.menu, menu );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    Intent intent;
    switch ( item.getItemId() ) {
      case R.id.menuAllRecords:
        toastIt("You selected All Records");
        intent = new Intent( this, MainActivity.class );
        startActivity( intent );
        return true;
      case R.id.menuAddRecord:
        toastIt("You selected Add Record");
        intent = new Intent( this, AddRecordActivity.class );
        startActivity( intent );
        return true;

    }
    return super.onOptionsItemSelected(item);
  }

  public void refreshRecords( View v ) {
    String url = "http://10.0.0.13:3000/records";
    StringRequest request = new StringRequest(
        Request.Method.GET, url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            Log.d("INTERNET", response);
            records = gson.fromJson( response, Record[].class );
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Log.d("ERROR", error.toString());
            toastIt( "Internet Failure" + error.toString() );
          }
        });

    requestQueue.add( request );
  }

  public void toastIt(String msg ){
    Toast.makeText( getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
  }
}
