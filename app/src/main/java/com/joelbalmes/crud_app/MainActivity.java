package com.joelbalmes.crud_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

  Button btnAddRecord;
  ListView lstRecords;
  ArrayAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btnAddRecord = findViewById( R.id.btnAddRecord );
    lstRecords = findViewById( R.id.lstRecords );

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    gson = gsonBuilder.create();

    getRecordsFromApi( null );

    lstRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent showIntent = new Intent( getApplicationContext(), ShowRecordActivity.class );
        showIntent.putExtra("position", position );
        startActivity( showIntent );
      }
    });

  }

  public void getRecordsFromApi( View v ) {
    String url = baseUrl + "records";
    StringRequest request = new StringRequest(
        Request.Method.GET, url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            Log.d("INTERNET", response);
            records = gson.fromJson( response, Record[].class );

            for ( int i = 0; i < records.length; ++i) {
              itemNames.add(records[i].getName());
            }

            adapter = new ArrayAdapter<String>( getApplicationContext(), R.layout.activity_list_item, itemNames );
            lstRecords.setAdapter( adapter );
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

  public void onAddRecordClicked( View v ) {

    Intent intent = new Intent( this, AddRecordActivity.class );
    startActivity( intent );
  }

}
