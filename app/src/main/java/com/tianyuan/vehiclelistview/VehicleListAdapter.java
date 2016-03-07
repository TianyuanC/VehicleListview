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

        Bitmap bitmap = imageCache.get(vehicle.getId());
        if(bitmap != null){
            ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
            image.setImageBitmap(vehicle.getDetails().getBitmap());
        }
        else{
            VehicleAndView container = new VehicleAndView();
            container.vehicle = vehicle;
            container.view = convertView;

            ImageLoader loader = new ImageLoader();
            loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, container);
        }

        return convertView;
    }

    private class VehicleAndView{
        public Vehicle vehicle;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<VehicleAndView, Void, VehicleAndView>{

        @Override
        protected VehicleAndView doInBackground(VehicleAndView... params) {
            VehicleAndView container = params[0];
            Vehicle vehicle = container.vehicle;
            InputStream in = null;
            try{
                String imageUrl = MainActivity.IMAGE_BASE + vehicle.getDetails().getImageUrl() + "?w=96&h=72";
                in = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                vehicle.getDetails().setBitmap(bitmap);
                container.bitmap = bitmap;

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
            return container;
        }

        @Override
        protected void onPostExecute(VehicleAndView result) {
            if(result!=null && result.bitmap!=null) {
                ImageView image = (ImageView) result.view.findViewById(R.id.imageView);
                image.setImageBitmap(result.bitmap);

                //result.vehicle.getDetails().setBitmap(result.bitmap);
                imageCache.put(result.vehicle.getId(), result.bitmap);
            }
        }
    }
}
