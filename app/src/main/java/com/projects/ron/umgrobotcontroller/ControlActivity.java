package com.projects.ron.umgrobotcontroller;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControlActivity extends AppCompatActivity{


    private JoystickView joystickGuid, joystickStreng;
    //ProgressDialog progressDialog;
    MyBluetooth conBluetooth;
    TextView deviceName, deviceStatus,txtDireccion,txtStreng;
    String addr;
    String name;
    int intentos = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_control);
        //getActionBar().hide();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            hideVirtualButtons();
        }

        Intent intentReceived = getIntent();
        addr = intentReceived.getStringExtra(MainActivity.EXTRA_ADDR);

        //this.nombre = intentReceived.getStringExtra(MainActivity.EXTRA_NAME);
        //addr = getArguments().getString("addr");

        joystickGuid = (JoystickView) findViewById(R.id.joystickView2);
        joystickStreng = (JoystickView) findViewById(R.id.joystickView);

        deviceName = (TextView) findViewById(R.id.title_device);
        deviceStatus = (TextView) findViewById(R.id.status_device);
        this.txtDireccion = (TextView) findViewById(R.id.textView4);
        this.txtStreng = (TextView) findViewById(R.id.textView6);


        deviceName.setText(intentReceived.getStringExtra(MainActivity.EXTRA_NAME));


        //conectar();
       // txtDistancia = (TextView) findViewById(R.id.txtDistancia);
        //txtAngulo = (TextView) findViewById(R.id.txtAngulo);

        joystickStreng.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {

                if(angle >= 5 && angle <= 175){
                    txtStreng.setText(String.valueOf(strength+100));
                }
                else if(angle >= 190 && angle <= 355){
                    txtStreng.setText(String.valueOf(strength+200));
                }
                else{
                    txtStreng.setText("0");
                }
            }
        });
        joystickGuid.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                System.out.println("angulo: " + angle + " Distancia: " + strength);
                //txtAngulo.setText(Integer.toString(angle));
               // txtDistancia.setText(Integer.toString(strength));
                // do whatever you want
                if(strength >= 50 && strength <= 100){
                    //Adelante
                    /*if(angle >= 70 && angle <= 110){
                        //Toast.makeText(this,"btn1",Toast.LENGTH_SHORT).show();
                        try {
                            conBluetooth.enviarDatos("{1:adelante}");
                            txtDireccion.setText("");
                        } catch (IOException e) {
                            System.out.println("error env: " + e.getMessage());
                        }
                    }*/
                    //izquierda
                    if(angle >= 160 && angle <= 200){
                        //Toast.makeText(this,"btn1",Toast.LENGTH_SHORT).show();
                       // try {
                            //conBluetooth.enviarDatos("{1:izquierda}");
                            txtDireccion.setText("Izquierda");
                       // } catch (IOException e) {
                         //   System.out.println("error env: " + e.getMessage());
                        //}
                    }
                    //Derecha
                    if((angle >= 340 && angle <= 360) || (angle >= 0 && angle <= 20)){
                        //Toast.makeText(this,"btn1",Toast.LENGTH_SHORT).show();
                        //try {
                           // conBluetooth.enviarDatos("{1:derecha}");
                            txtDireccion.setText("Derecha");
                       // } catch (IOException e) {
                        //    System.out.println("error env: " + e.getMessage());
                        //}
                    }
                    //atras
                    /*if(angle >= 250 && angle <= 290){
                        //Toast.makeText(this,"btn1",Toast.LENGTH_SHORT).show();
                        try {
                            conBluetooth.enviarDatos("{1:atras}");
                        } catch (IOException e) {
                            System.out.println("error env: " + e.getMessage());
                        }
                    }*/
                }else{
                    txtDireccion.setText("Centro");
                   // try {
                        //conBluetooth.enviarDatos("{1:off}");
                    //} catch (IOException e) {
                      //  System.out.println("error env: " + e.getMessage());
                    //}
                }
            }
        });
    }


    public void conectar(){
        connectAsync cAsync = new connectAsync();
        cAsync.execute(addr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // In KITKAT (4.4) and next releases, hide the virtual buttons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideVirtualButtons();
            }
        }
    }

    @TargetApi(19)
    private void hideVirtualButtons() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    public void toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

    };

    private class connectAsync extends AsyncTask<String, Void, Void>  // UI thread
    {
        private boolean status = true;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ControlActivity.this, "Conectando...", "");
            deviceStatus.setText("Conectando...");
            conBluetooth = new MyBluetooth();
            if (!conBluetooth.btDisponible()) {
                //Intent intentBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //startActivityForResult(intentBt, PICK_BLUETOOTH_REQUEST);
                //toast("No se a podido establecer la coneccion");
                //this.cancel(true);
                //progressDialog.dismiss();
                status = false;
                /*try {
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }*/
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                conBluetooth.connectar(params[0]);
                // bt.enviarDatos(params[1]);

            } catch (IOException e) {
                System.out.println("error env: " + e.getMessage());
                status = false;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!status) {
                try {
                    conBluetooth.desconectar();
                } catch (IOException e) {
                    System.out.println("error: " + e.getMessage());
                    toast("Error desconectando...");
                    e.printStackTrace();
                }
                toast("No se pudo establecer la conexión...");
                if(intentos > 2)
                    finish();
                else{
                    progressDialog.dismiss();
                    intentos++;
                    conectar();
                }

                //habilitarBTN(false);
                //textBt.setText("No se pudo establecer Conexion...");

                //finish();
            } else {
                toast("Start the Game");
                deviceStatus.setText("Conectado");
                intentos = 0;
                //habilitarBTN(true);
                //textBt.setText("Conectado: " + getArguments().getString("nombre"));
                /*try {
                    bt.desconectar();
                    msg("desconectado");
                } catch (IOException e) {
                    msg("Error desconectando...");
                    System.out.println("error: " + e.getMessage());
                    e.printStackTrace();
                }*/
                //isBtConnected = true;
            }
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            conBluetooth.desconectar();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        //if(conBluetooth != null)
            //if(!conBluetooth.estado())
              //  conectar();
        //if (VERBOSE) Log.v(TAG, "+ ON RESUME +");
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            conBluetooth.desconectar();
        } catch (IOException e) {
            System.out.println("erorr: " + e.getMessage());
        }
        //if (VERBOSE) Log.v(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            conBluetooth.desconectar();
        } catch (IOException e) {
            System.out.println("erorr: " + e.getMessage());
        }
        //if (VERBOSE) Log.v(TAG, "-- ON STOP --");
    }
}
