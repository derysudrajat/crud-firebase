package com.derysudrajat.crudfirebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Crew implements Parcelable {
    public String id;
    public int nomor;
    public String nama;
    public String date;
    public String jk;
    public String alamat;

    public Crew() {
    }

    public Crew(int nomor, String nama, String date, String jk, String alamat) {
        this.nomor = nomor;
        this.nama = nama;
        this.date = date;
        this.jk = jk;
        this.alamat = alamat;
    }

    public String getId() {
        return id;
    }

    public int getNomor() {
        return nomor;
    }

    public String getNama() {
        return nama;
    }

    public String getDate() {
        return date;
    }

    public String getJk() {
        return jk;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNomor(int nomor) {
        this.nomor = nomor;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.nomor);
        dest.writeString(this.nama);
        dest.writeString(this.date);
        dest.writeString(this.jk);
        dest.writeString(this.alamat);
    }

    protected Crew(Parcel in) {
        this.id = in.readString();
        this.nomor = in.readInt();
        this.nama = in.readString();
        this.date = in.readString();
        this.jk = in.readString();
        this.alamat = in.readString();
    }

    public static final Parcelable.Creator<Crew> CREATOR = new Parcelable.Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel source) {
            return new Crew(source);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };
}
