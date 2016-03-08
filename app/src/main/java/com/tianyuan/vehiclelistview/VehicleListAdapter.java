package com.tianyuan.vehiclelistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tianyuan.vehiclelistview.model.Vehicle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by churyan on 16-03-06.
 */
public class VehicleListAdapter extends ArrayAdapter<Vehicle> {

    private List<Vehicle> vehicles;
    private Context context;
    private LruCache<Integer, Bitmap> imageCache;

    public VehicleListAdapter(Context context, int resource, List<Vehicle> objects) {
        super(context, resource, objects);
        this.context = context;
        this.vehicles = objects;

        final int maxMemory =(int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }
        Vehicle vehicle = vehicles.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.textView);
        name.setText(vehicle.getDetails().getStructures().toString());
        TextView price = (TextView) convertView.findViewById(R.id.textView2);
        price.setText(vehicle.getDetails().getPriceFormat());

        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
        Picasso.with(context)
                .load(MainActivity.IMAGE_BASE + vehicle.getDetails().getImageUrl() + "?w=96&h=72")
                .into(image);

        return convertView;
    }
}
