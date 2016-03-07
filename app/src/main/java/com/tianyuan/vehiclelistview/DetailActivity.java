package com.tianyuan.vehiclelistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageAndView container = new ImageAndView();
        container.view = img;
        container.path = vehicleImageUrl;
        if(isOnline()){
            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }else{
            Toast.makeText(findViewById(android.R.id.content).getContext(), "Network is unavailable", Toast.LENGTH_SHORT).show();
        }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    private class ImageAndView{
        View view;
        Bitmap bitmap;
        String path;
    }
    private class ImageLoader extends AsyncTask<ImageAndView, Void, ImageAndView> {

        @Override
        protected ImageAndView doInBackground(ImageAndView... params) {
            ImageAndView container = params[0];
            InputStream in = null;
            try{
                String imageUrl = MainActivity.IMAGE_BASE + container.path;
                in = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                container.bitmap = bitmap;
                return container;

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                if(in!=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ImageAndView result) {
            if(result!=null) {
                ImageView image = (ImageView) result.view;
                image.setImageBitmap(result.bitmap);

                //result.vehicle.getDetails().setBitmap(result.bitmap);
                //imageCache.put(result.vehicle.getId(), result.bitmap);
            }
        }
    }
}
