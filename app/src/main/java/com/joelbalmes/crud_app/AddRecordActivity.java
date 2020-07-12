package com.joelbalmes.crud_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;

public class AddRecordActivity extends BaseActivity {

  EditText edtAddName, edtAddDescription, edtAddPrice, edtAddImageUrl;
  Button btnAddSubmit;
  RatingBar ratingAddRating;
  String imageUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_record);

    edtAddName = findViewById( R.id.edtAddName );
    edtAddDescription = findViewById( R.id.edtAddDescription );
    edtAddPrice = findViewById( R.id.edtAddPrice );
    edtAddImageUrl = findViewById( R.id.edtAddImageUrl );
    btnAddSubmit = findViewById( R.id.btnAddSubmit );
    ratingAddRating = findViewById( R.id.ratingAddRating );

    imageUrl = "https://i.imgur.com/xot6mUE.jpg";
    edtAddImageUrl.setText( imageUrl );

    edtAddPrice.setFilters( new InputFilter[]{new CurrencyFilter()});
  }


  public void onSubmitClicked( View v ) {
    String url = baseUrl + "records";
    final String recordName = edtAddName.getText().toString();
    final String recordDescription = edtAddDescription.getText().toString();
    final String recordImageUrl = edtAddImageUrl.getText().toString();
    final int recordRating = (int) ratingAddRating.getRating();
    double price = 0.0;

    if( !edtAddPrice.getText().toString().equals( "" )) {
      price = Double.parseDouble( edtAddPrice.getText().toString() );
    }

    final double recordPrice = price;

    if( !recordName.equals("") ) {
      HashMap<Object, Object> params = new HashMap<>();
      params.put( "name", recordName );
      params.put( "description", recordDescription );
      params.put( "rating", recordRating );
      params.put( "price", recordPrice );
      params.put( "image", recordImageUrl );


      JsonObjectRequest request = new JsonObjectRequest( Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          Log.d("EVENT", response.toString());
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          Log.d("EVENT", error.toString());
        }
      });

      requestQueue.add( request );

      Intent homeIntent = new Intent( getApplicationContext(), MainActivity.class );
      startActivity( homeIntent );
    } else {
      toastIt( "Name is required. Please enter a Name." );
    }
  }
}
