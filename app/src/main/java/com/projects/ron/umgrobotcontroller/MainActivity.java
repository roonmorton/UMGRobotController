package com.projects.ron.umgrobotcontroller;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    static final int PICK_BLUETOOTH_REQUEST = 1;  // The request code
    private ListView listParedDevices;
    public static final String EXTRA_ADDR = "BT_ADRESS";
    public static final String EXTRA_NAME = "BT_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listParedDevices = (ListView) findViewById(R.id.list_device_pared);
        cargarDispositivos();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarDispositivos();
                //Snackbar.make(view, "Actualizando...", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();
            }
        });
    }



    private void cargarDispositivos(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);

        MyBluetooth bluetooth = new MyBluetooth();

        if (bluetooth.getAdaptador() == null) {
            toast("Dispositivo no se encuentra disponible");
            finish();
        } else if (!bluetooth.btDisponible()) {
            Intent intentBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentBt, PICK_BLUETOOTH_REQUEST);
            // progressDialog.onStart();
        }else{
            ArrayList devices = bluetooth.dispositivosPareados();
            ArrayAdapter adapter;
            if (devices.size() > 0) {
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, devices);
                listParedDevices.setAdapter(adapter);
                listParedDevices.setOnItemClickListener(myListClickListener);
            } else {
                ArrayList list = new ArrayList();
                list.add("No has dispositivos pareados...");
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
                this.listParedDevices.setAdapter(adapter);
                toast("Ningun dispositivo Pareado...");
            }
        }
        progressDialog.dismiss();
    }
    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_BLUETOOTH_REQUEST) {
            // Make sure the request was successful
            //msg("return requestcode");
            if (resultCode == RESULT_OK) {
                //msg("result: ok");
                cargarDispositivos();
                //setDispotitivos();
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String addr = info.substring(info.length() - 17);
            String name = info.split("\n")[0];
            // Make an intent to start next activity.
            Intent intent = new Intent(MainActivity.this, ControlActivity.class);
            intent.putExtra(EXTRA_ADDR,addr);
            intent.putExtra(EXTRA_NAME,name);
            startActivity(intent);
            //Change the activity.
            //i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity

            //i.putExtra(EXTRA_NAME, name);
            //startActivity(i);
        }
    };

}



