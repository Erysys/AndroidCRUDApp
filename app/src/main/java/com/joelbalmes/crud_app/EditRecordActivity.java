package com.joelbalmes.crud_app;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class EditRecordActivity extends BaseActivity {

  EditText edtEditName, edtEditDescription, edtEditPrice, edtImageURL;
  RatingBar ratingEditRating;
  String itemName, imageURL;
  int rating;
  double price;
  int recordID;
  int position;
  Date createdDate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_record);

    edtEditName = findViewById( R.id.edtEditName );
    edtEditDescription = findViewById( R.id.edtEditDescription );
    edtEditPrice = findViewById( R.id.edtEditPrice );
    edtImageURL = findViewById( R.id.edtImageURL );
    ratingEditRating = findViewById( R.id.ratingEditRating );

    if( getIntent().getExtras() != null ) {
      position = getIntent().getExtras().getInt( "position" );
    }

    recordID = records[position].getId();
    price = records[position].getPrice();
    rating = records[position].getRating();
    createdDate = records[position].getCreatedAt();
    itemName = records[position].getName();
    imageURL = records[position].getImage();

    edtEditName.setText( records[position].getName() );
    edtEditDescription.setText( records[position].getDescription() );
    edtEditPrice.setText( String.valueOf( price ) );
    edtImageURL.setText( imageURL );
    ratingEditRating.setRating( rating );

  }

  public void onSubmitClicked( View v ) {
    String url = baseUrl + "records/" + recordID;
    final String recordName = edtEditName.getText().toString();
    final String recordDescription = edtEditDescription.getText().toString();
    final String recordImageURL = edtImageURL.getText().toString();
    final int recordRating = (int) ratingEditRating.getRating();

    itemName = recordName;

    if( !edtEditPrice.getText().toString().equals( "" )) {
      price = Double.parseDouble( edtEditPrice.getText().toString() );
    }

    final double recordPrice = price;

    if( !recordName.equals("") ) {

      HashMap<Object, Object> params = new HashMap<>();
      params.put( "name", recordName );
      params.put( "description", recordDescription );
      params.put( "rating", recordRating );
      params.put( "price", recordPrice );
      params.put( "image", recordImageURL );


      JsonObjectRequest request = new JsonObjectRequest( Request.Method.PATCH, url, new JSONObject(params), new Response.Listener<JSONObject>() {
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

      Intent intent = new Intent( this, ShowRecordActivity.class );
      intent.putExtra( "position", position );
      startActivity( intent );

    } else {
      toastIt( "Name is required. Please enter a Name." );
    }
  }

}
