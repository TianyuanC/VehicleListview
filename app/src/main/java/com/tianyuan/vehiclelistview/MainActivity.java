package com.tianyuan.vehiclelistview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tianyuan.vehiclelistview.model.Vehicle;
import com.tianyuan.vehiclelistview.parser.VehicleJSONParser;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String VEHICLE_NAME = "VEHICLE_NAME";
    public static final String VEHICLE_PRICE = "VEHICLE_PRICE";
    public static final String VEHICLE_IMAGE = "VEHICLE_IMAGE";
    public static final String VEHICLE_DESC = "VEHICLE_DESC";

    ProgressBar pb;
    List<FetchTask> tasks;
    List<Vehicle> vehicleList = new ArrayList<>();

    public static final String IMAGE_BASE = "http://azr.cdnmedia.autotrader.ca/5";
    private static final String SEARCH_QUERY = "https://search.beta.autotrader.ca/api/ad/?v=2&t=25&d=1&hcp=1&vp=1&haspr=true&srt=2";
    VehicleListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        tasks = new ArrayList<>();

        ListView lv = (ListView) findViewById(R.id.listView);
        adapter = new VehicleListAdapter(this, R.layout.list_item, vehicleList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Vehicle vehicle = vehicleList.get(position);
                intent.putExtra(VEHICLE_NAME, vehicle.getDetails().getStructures().toString());
                intent.putExtra(VEHICLE_PRICE, vehicle.getDetails().getPriceFormat());
                intent.putExtra(VEHICLE_IMAGE, vehicle.getDetails().getImageUrl());
                intent.putExtra(VEHICLE_DESC, vehicle.getDetails().getDescription());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    requestData();
                } else {
                    Toast.makeText(view.getContext(), "Network is unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    private void requestData() {
        FetchTask task = new FetchTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    private class FetchTask extends AsyncTask<String, String, List<Vehicle>> {

        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Vehicle> doInBackground(String... params) {
            String data = HttpManager.getData(SEARCH_QUERY);
            vehicleList.addAll(VehicleJSONParser.parseFeed(data));

            return vehicleList;
        }

        @Override
        protected void onPostExecute(List<Vehicle> vehicles) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
            tasks.remove(this);
            if(tasks.size()==0){
                pb.setVisibility(View.INVISIBLE);
            }
        }
    }
}
