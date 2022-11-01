package com.example.nfaquatic.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "keranjang") // the name of tabel
public class Produk implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int idTb;

    public int id;
    public String code;
    public String name;
    public String description;
    public int stock;
    public int price;
    public int category_id;
    public String photo;
    public String created_at;
    public String updated_at;
    public int discount = 0;
    public int jumlah = 1;
    public boolean selected = true;
}