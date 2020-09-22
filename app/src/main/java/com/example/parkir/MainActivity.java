package com.example.parkir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity{

    public static List<DataParkir> dataParkirs = new ArrayList<>();
    public static PriorityQueue<Integer> pQueue = new PriorityQueue<>();
    private Button btnScan, btnGenerate, btnReset;
    private ImageView imgQR;
    private TextView txtSlot;
    private int max = 10;
    boolean isDefined = false;
    private static int nomorParkir=1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String slot="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGenerate = this.findViewById(R.id.btn_generate);
        btnScan = this.findViewById(R.id.btn_scan);
        btnReset = this.findViewById(R.id.btn_reset);
        txtSlot = this.findViewById(R.id.txt_slot);
        imgQR = this.findViewById(R.id.img_QR);
        checkAndRequestPermissions();
        if (!isDefined){
            for (int i = 1; i <= 10; i++) {
                dataParkirs.add(new DataParkir(i, true));
                pQueue.add(i-1);
            }
            isDefined=true;
        }
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pQueue.clear();
                for (int i = 0; i < dataParkirs.size(); i++) {
                    dataParkirs.get(i).setAvailable(true);
                    pQueue.add(i);

                }
                displaySlot();
                imgQR.setImageBitmap(null);
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pQueue.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Parkir penuh");
                    builder.setMessage("Parkir penuh");
                    AlertDialog alert1 = builder.create();
                    alert1.show();
                } else {
                    String txtQR = String.valueOf(pQueue.peek());
                    Log.d("peek", String.valueOf(pQueue.peek()));
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(txtQR, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imgQR.setImageBitmap(bitmap);
                        dataParkirs.get(pQueue.peek()).setAvailable(false);
                        Log.d("avail", "np: "+dataParkirs.get(pQueue.peek()).getNomorParkir()+" isAvail :"+dataParkirs.get(pQueue.peek()).isAvailable());
                        pQueue.poll();
                        Log.d("peek2", String.valueOf(pQueue.peek()));
                        displaySlot();
                    } catch (WriterException e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displaySlot();
    }

    private void displaySlot(){
        slot="";
        for (int i = 0; i < dataParkirs.size(); i++) {
            if (dataParkirs.get(i).isAvailable()){
                slot +=dataParkirs.get(i).getNomorParkir()+"  ";
            }
        }
        txtSlot.setText(slot);
    }
    private  boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
