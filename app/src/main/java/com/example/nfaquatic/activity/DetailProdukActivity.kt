package com.example.nfaquatic.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.nfaquatic.R
import com.example.nfaquatic.helper.Helper
import com.example.nfaquatic.model.Produk
import com.example.nfaquatic.retrofit.MyDatabase
import com.example.nfaquatic.utils.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailProdukActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase
    lateinit var produk: Produk
    lateinit var tvnama: TextView
    lateinit var tvharga: TextView
    lateinit var tvKode: TextView
    lateinit var tvStock: TextView
    lateinit var tvdeskripsi: TextView
    lateinit var imagex: ImageView
    lateinit var btnKeranjang: RelativeLayout
    lateinit var btnFavorit: RelativeLayout
    lateinit var divAngka: RelativeLayout
    lateinit var tvAngka: TextView
    lateinit var btntoKeranjang: RelativeLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)

        myDb = MyDatabase.getInstance(this)!! // call database
        tvnama = findViewById(R.id.tv_nama)
        tvharga = findViewById(R.id.tv_harga)
        tvKode = findViewById(R.id.tvKode)
        tvStock = findViewById(R.id.tvStock)
        tvdeskripsi = findViewById(R.id.tv_deskripsi)
        imagex = findViewById(R.id.image)
        btnKeranjang = findViewById(R.id.btn_keranjang)
        btnFavorit = findViewById(R.id.btn_favorit)
        divAngka = findViewById(R.id.div_angka)
        tvAngka = findViewById(R.id.tv_angka)
        btntoKeranjang = findViewById(R.id.btn_toKeranjang)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        getInfo()
        mainButton()

    }

    private fun mainButton() {

        btnKeranjang.setOnClickListener {

            val data = myDb.daoKeranjang().getProduk(produk.id)
            if (data == null) {
                insert()
            } else {
                //data.jumlah += 1
                val stock = Integer.valueOf(data.stock)
                data.jumlah += (stock - data.jumlah)
                update(data)
            }

        }

        btnFavorit.setOnClickListener {
            val listData = myDb.daoKeranjang().getAll() // get All data
            for (note: Produk in listData) {
                println("-----------------------")
                println(note.name)
                println(note.price)
            }
        }

        btntoKeranjang.setOnClickListener {
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }

    }

    private fun insert() {
        val myDb: MyDatabase = MyDatabase.getInstance(this)!! // call database
        CompositeDisposable().add(Observable.fromCallable {
            myDb.daoKeranjang().insert(produk)
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkKeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this, "Berhasil menambah keranjang", Toast.LENGTH_SHORT).show()
            })
    }

    private fun update(data: Produk) {
        CompositeDisposable().add(Observable.fromCallable {
            myDb.daoKeranjang().update(data)
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkKeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this, "Berhasil update keranjang", Toast.LENGTH_SHORT).show()
            })
    }

    private fun checkKeranjang() {
        val dataKranjang = myDb.daoKeranjang().getAll()
        if (dataKranjang.isNotEmpty()) {
            divAngka.visibility = View.VISIBLE
            tvAngka.text = dataKranjang.size.toString()
        } else {
            divAngka.visibility = View.GONE
        }
    }

    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        produk = Gson().fromJson(data, Produk::class.java)
        tvnama.text = produk.name
        tvharga.text = Helper().gantiRupiah(produk.price)
        tvKode.text = produk.code
        tvdeskripsi.text = produk.description
        tvStock.text = produk.stock.toString()
        val img = Config.produkUrl + produk.photo
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.hp_notebook_14_bs709tu)
            .error(R.drawable.hp_notebook_14_bs709tu)
            //.resize(400, 400)
            .into(imagex)
        Helper().setToolbar(this, toolbar, produk.name)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}