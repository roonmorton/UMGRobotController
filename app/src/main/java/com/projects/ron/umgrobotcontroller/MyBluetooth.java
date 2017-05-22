package com.projects.ron.umgrobotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Soporte on 28/04/2017.
 */

public class MyBluetooth {

    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;
    private boolean isBtConnected;


    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public MyBluetooth() {

        /*if(!btAdapter.isEnabled()){
            this.btAdapter.enable();
        }*/
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.btSocket = null;
        this.isBtConnected = false;
        //IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        //IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        //IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        //this.registerReceiver(mReceiver, filter1);
        //this.registerReceiver(mReceiver, filter2);
        //this.registerReceiver(mReceiver, filter3);
    }

    public void connectar(String addr) throws IOException {
        if (this.btSocket == null && !this.isBtConnected) {

            BluetoothDevice dispositivo = this.btAdapter.getRemoteDevice(addr);
            this.btSocket = dispositivo.createRfcommSocketToServiceRecord(myUUID); //dispositivo.createRfcommSocketToServiceRecord(myUUID);

            this.btAdapter.cancelDiscovery();
            //BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            this.btSocket.connect();
            this.isBtConnected = true;

        } else {
            Log.e("msg ", "emparejado");
        }
    }

    public void enviarDatos(String data) throws IOException {
        if (this.btSocket != null) {
            this.btSocket.getOutputStream().write(data.toString().getBytes());
        }
    }

    public void desconectar() throws IOException {
        if (this.btSocket != null) {
            //this.btAdapter.disable();

            this.btSocket.getOutputStream().close();
            this.btSocket.close();
            this.btSocket = null;

        }
    }

    public boolean btDisponible() {
        return this.btAdapter.isEnabled();
    }

    public BluetoothAdapter getAdaptador() {
        return this.btAdapter;
    }

    public boolean estado() {
        return this.btSocket.isConnected();
    }

    public ArrayList dispositivosPareados() {
        Set<BluetoothDevice> dispositivosVinculados = this.btAdapter.getBondedDevices();
        ArrayList dispotivos = new ArrayList();

        if (dispositivosVinculados.size() > 0) {
            for (BluetoothDevice bt : dispositivosVinculados) {
                dispotivos.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        this.btAdapter.cancelDiscovery();

        return dispotivos;
    }




}
