package com.example.parkir;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity implements ResultHandler{


    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Log.v("TAG", result.getText()); // Prints scan results
        Log.v("TAG", result.getBarcodeFormat().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage("Berhasil keluar. Nomor parkir: "+ MainActivity.dataParkirs.get(Integer.parseInt(result.getText())).getNomorParkir());
        AlertDialog alert1 = builder.create();
        alert1.show();
        MainActivity.dataParkirs.get(Integer.parseInt(result.getText())).setAvailable(true);
        Log.d("availAfter", "np: "+MainActivity.dataParkirs.get(Integer.parseInt(result.getText())).getNomorParkir()+" isAvail :"+MainActivity.dataParkirs.get(Integer.parseInt(result.getText())).isAvailable());
        MainActivity.pQueue.add(Integer.parseInt(result.getText()));
        Log.d("peek2After", String.valueOf(MainActivity.pQueue.peek()));
        mScannerView.resumeCameraPreview(this);
    }
}
