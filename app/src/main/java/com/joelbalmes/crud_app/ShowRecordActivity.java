package com.joelbalmes.crud_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ShowRecordActivity extends BaseActivity {

  TextView lblShowName, lblShowDescription, lblShowPrice, lblShowCreatedDate, lblShowModifiedDate;
  RatingBar ratingShowRating;
  ImageView showImage;
  String itemName, imageUrl;
  double price;
  int recordID;
  int position;
  String priceString;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_record);

    lblShowName = findViewById( R.id.lblShowName );
    lblShowDescription = findViewById( R.id.lblShowDescription );
    lblShowPrice = findViewById( R.id.lblShowPrice );
    lblShowCreatedDate = findViewById( R.id.lblShowCreatedDate );
    lblShowModifiedDate = findViewById( R.id.lblShowModifiedDate );
    ratingShowRating = findViewById( R.id.ratingShowRating );
    showImage = findViewById( R.id.showImage );

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    gson = gsonBuilder.create();

    if( getIntent().getExtras() != null ) {
      position = getIntent().getExtras().getInt( "position" );
    }

    recordID = records[position].getId();

    getRecordFromApi( null );


  }

  @Override
  public void onBackPressed() {
    Intent backIntent = new Intent( this, MainActivity.class );
    startActivity( backIntent );
  }

  public void getRecordFromApi( View v ) {
    String url = baseUrl + "records";
    StringRequest request = new StringRequest(
        Request.Method.GET, url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            Log.d("INTERNET", response);
            records = gson.fromJson( response, Record[].class );

            price = records[position].getPrice();
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            priceString = format.format( price );

            itemName = records[position].getName();
            lblShowName.setText( itemName );
            lblShowDescription.setText( records[position].getDescription() );
            lblShowPrice.setText( priceString );
            lblShowCreatedDate.setText( String.valueOf( records[position].getCreatedAt() ));
            lblShowModifiedDate.setText( String.valueOf( records[position].getUpdatedAt() ));
            ratingShowRating.setRating( records[position].getRating() );
            imageUrl = records[position].getImage();
            if ( imageUrl != null && imageUrl != "") {
              Picasso.get().load( imageUrl ).into( showImage );
            } else {
              Picasso.get().load( "https://i.imgur.com/xot6mUE.jpg" ).into( showImage );
            }


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

  public void onEditClicked( View v ) {
    Intent editIntent = new Intent( this, EditRecordActivity.class );
    editIntent.putExtra( "position", position );
    startActivity( editIntent );
  }


  public void onDeleteClicked( View v ) {
    AlertDialog.Builder builder = new AlertDialog.Builder(ShowRecordActivity.this);
    builder.setMessage( "Are you sure you want to delete " + itemName + "? This action cannot be undone.");
    builder.setTitle( "Confirmation" );
    builder.setCancelable( false );
    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        deleteRecord( recordID );
        Intent homeIntent = new Intent( getApplicationContext(), MainActivity.class );
        startActivity( homeIntent );
      }
    });
    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });

    AlertDialog confirmationDialog = builder.create();
    confirmationDialog.show();


  }

  private void deleteRecord(final int recordID ) {
    String url = baseUrl + "records/" + recordID;
    StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        Log.d("EVENT", response);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d("EVENT", error.toString());
        toastIt( "Internet Failure" + error.toString() );
      }
    });

    requestQueue.add( request );


//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        Record recordToDelete = new Record();
//        recordToDelete.setRecordID( recordID );
//
//        recordDatabase.recordDao().deleteRecord( recordToDelete );
//      }
//    }).start();
  }



}
