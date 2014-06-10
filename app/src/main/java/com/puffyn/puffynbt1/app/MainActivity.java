package com.puffyn.puffynbt1.app;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private BluetoothAdapter btAdapter;

    public TextView statusUpdate;
    public Button connect;
    public Button disconnect;
    public ImageView logo;

    BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String prevStateExtra=BluetoothAdapter.EXTRA_PREVIOUS_STATE;
            String stateExtra=BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateExtra, -1);
            // int previousState = intent.getIntExtra(prevStateExtra, -1);
            String toastText ="";
            switch(state){
                case(BluetoothAdapter.STATE_TURNING_ON) :
                {
                    toastText = "Bluetooth turning on";
                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    break;
                }
                case(BluetoothAdapter.STATE_ON) :
                {
                    toastText = "Bluetooth is on";
                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    setupUI();
                    break;
                }
                case(BluetoothAdapter.STATE_TURNING_OFF) :
                {
                    toastText = "Bluetooth turning off";
                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    break;
                }
                case(BluetoothAdapter.STATE_OFF) :
                {
                    toastText = "Bluetooth off";
                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    setupUI();
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
    }

    private void setupUI() {
        //get references
        final TextView statusUpdate = (TextView) findViewById(R.id.result);
        final Button connect = (Button) findViewById(R.id.connect);
        final Button disconnect = (Button) findViewById(R.id.disconnect);
        final ImageView logo = (ImageView) findViewById(R.id.logo);

        //set display view
        disconnect.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter.isEnabled()) {
            String address = btAdapter.getAddress();
            String name = btAdapter.getName();
            String statusText = name + " : " + address;
            statusUpdate.setText(statusText);
            logo.setVisibility(View.VISIBLE);
            connect.setVisibility(View.GONE);
            disconnect.setVisibility(View.VISIBLE);
        }
        else {
            connect.setVisibility(View.VISIBLE);
            statusUpdate.setText("Bluetooth is off");
        }

        connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
                String actionRequestEnabled = BluetoothAdapter.ACTION_REQUEST_ENABLE;
                IntentFilter filter = new IntentFilter(actionStateChanged);
                registerReceiver(bluetoothState, filter);
                startActivityForResult(new Intent(actionRequestEnabled), 0);
                logo.setVisibility(View.VISIBLE);
            }
        }); //end connect onClickListener

        disconnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btAdapter.disable();
                disconnect.setVisibility(View.GONE);
                connect.setVisibility(View.VISIBLE);
                logo.setVisibility(View.GONE);
                statusUpdate.setText("Bluetooth is Off");
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
