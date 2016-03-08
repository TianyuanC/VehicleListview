package com.tianyuan.vehiclelistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String vehicleTitle = getIntent().getStringExtra(MainActivity.VEHICLE_NAME);
        String vehiclePrice = getIntent().getStringExtra(MainActivity.VEHICLE_PRICE);
        String vehicleImageUrl = getIntent().getStringExtra(MainActivity.VEHICLE_IMAGE);
        String vehicleDescription = getIntent().getStringExtra(MainActivity.VEHICLE_DESC);

        TextView title = (TextView) findViewById(R.id.nameText);
        title.setText(vehicleTitle);
        TextView price = (TextView) findViewById(R.id.priceText);
        price.setText(vehiclePrice);
        TextView description = (TextView) findViewById(R.id.descriptionText);
        description.setText(vehicleDescription);

        ImageView img = (ImageView) findViewById(R.id.imageDetailView);

        if(isOnline()){
            Picasso.with(this).load(MainActivity.IMAGE_BASE+vehicleImageUrl).into(img);
        }else{
            Toast.makeText(findViewById(android.R.id.content).getContext(), "Network is unavailable", Toast.LENGTH_SHORT).show();
        }

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEmail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    Snackbar.make(view, "Interested in this car? Notify dealer now!", Snackbar.LENGTH_LONG)
                            .setAction("Send", new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(v.getContext(), "Email sent", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                } else {
                    Toast.makeText(view.getContext(), "Network is unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}
