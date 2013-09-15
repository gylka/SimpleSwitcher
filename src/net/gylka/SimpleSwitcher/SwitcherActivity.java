package net.gylka.SimpleSwitcher;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import static android.hardware.Camera.*;

public class SwitcherActivity extends Activity {

    private boolean mIsFlashlightOn;
    private boolean mIsWiFiOn;
    private boolean mIsBluetoothOn;

    private Camera mCamera;
    private SurfaceView mSurfaceViewCamera;
    private SurfaceHolder mHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switcher);

        //Flashlight section
        Button btnFlashlight = (Button) findViewById(R.id.btnFlashlight);
        btnFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    if ( ! mIsFlashlightOn) {
                        mCamera = Camera.open();
                        Camera.Parameters cameraParams = mCamera.getParameters();
                        cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(cameraParams);
                        mSurfaceViewCamera = (SurfaceView) findViewById(R.id.surfaceViewCamera);
                        mHolder = mSurfaceViewCamera.getHolder();
                        mHolder.addCallback(new SurfaceHolder.Callback() {
                            @Override
                            public void surfaceCreated(SurfaceHolder holder) {
                                try {
                                    mCamera.setPreviewDisplay(holder);
                                    mCamera.startPreview();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                            }

                            @Override
                            public void surfaceDestroyed(SurfaceHolder holder) {
                            }

                        } );
                        mIsFlashlightOn = true;
                        ((Button) v).setText(R.string.turn_off);
                    } else {
                        Camera.Parameters cameraParams = mCamera.getParameters();
                        cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(cameraParams);
                        mCamera.stopPreview();
                        mCamera.release();
                        mIsFlashlightOn = false;
                        ((Button)v).setText(R.string.turn_on);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Your device does not have camera flash", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Wi-Fi section
        Button btnWiFi = (Button) findViewById(R.id.btnWiFi);
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI)) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                btnWiFi.setText(R.string.turn_off);
            } else {
                btnWiFi.setText(R.string.turn_on);
            }
        } else {
            btnWiFi.setText(R.string.turn_on);
        }
        btnWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI)) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(false);
                        ((Button)v).setText(R.string.turn_on);
                    } else {
                        wifiManager.setWifiEnabled(true);
                        ((Button)v).setText(R.string.turn_off);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Your device does not have Wi-Fi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Bluetooth section
        Button btnBluetooth = (Button) findViewById(R.id.btnBluetooth);
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                btnBluetooth.setText(R.string.turn_off);
            } else {
                btnBluetooth.setText(R.string.turn_on);
            }
        } else {
            btnBluetooth.setText(R.string.turn_on);
        }
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.disable();
                        ((Button)v).setText(R.string.turn_on);
                    } else {
                        bluetoothAdapter.enable();
                        ((Button)v).setText(R.string.turn_off);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Your device does not have Bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
