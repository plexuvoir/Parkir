package com.example.parkir;

public class DataParkir {
    private int nomorParkir;
    private boolean isAvailable;

    public DataParkir(int nomorParkir, boolean isAvailable) {
        this.nomorParkir = nomorParkir;
        this.isAvailable = isAvailable;
    }

    public int getNomorParkir() {
        return nomorParkir;
    }

    public void setNomorParkir(int nomorParkir) {
        this.nomorParkir = nomorParkir;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
