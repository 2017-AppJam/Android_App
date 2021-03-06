package com.example.parktaejun.remind.Tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.parktaejun.remind.MainActivity;
import com.example.parktaejun.remind.R;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class Tutorial_4_Fragment extends Fragment {

    LinearLayout linearLayout;
    public static BluetoothSPP bt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tutorial_4_fragment, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.activity_tutorial_4_fragment);

        bt = new BluetoothSPP(getContext());

        if (!bt.isBluetoothAvailable()) {
            Toast.makeText(getContext(), "블루투스를 켜주세요", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener()

        {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getContext(), "연결되었습니다", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getContext(), "연결이끊겼습니다", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getContext(), "연결에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        });

        bt.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bt.send("z", true);
                    }
                });
            }

            public void onAutoConnectionStarted() {
            }
        });

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(final byte[] data, final String message) {
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                mainIntent.putExtra("message", message);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });
        return view;

    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }
    public void setup() {
        bt.autoConnect("si_ba");
    }

}
