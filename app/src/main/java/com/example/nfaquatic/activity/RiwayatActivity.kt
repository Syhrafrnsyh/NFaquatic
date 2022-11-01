package com.example.nfaquatic.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nfaquatic.R
import com.example.nfaquatic.adapter.RiwayatAdapter
import com.example.nfaquatic.helper.Helper
import com.example.nfaquatic.helper.SharedPref
import com.example.nfaquatic.model.ResponModel
import com.example.nfaquatic.model.Transaksi
import com.example.nfaquatic.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var rv_riwayat: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)
        toolbar = findViewById(R.id.toolbar)
        Helper().setToolbar(this, toolbar, "Riwayat Belanja")
        rv_riwayat = findViewById(R.id.rv_riwayat)

    }

    fun getRiwayat() {
        val id = SharedPref(this).getUser()!!.id
        ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    displayRiwayat(res.transaksis)
                }
            }
        })
    }

    fun displayRiwayat(transaksis: ArrayList<Transaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_riwayat.adapter = RiwayatAdapter(transaksis, object : RiwayatAdapter.Listeners {
            override fun onClicked(data: Transaksi) {
                val json = Gson().toJson(data, Transaksi::class.java)
                val intent = Intent(this@RiwayatActivity, DetailTransaksiActivity::class.java)
                intent.putExtra("transaksi", json)
                startActivity(intent)
            }
        })
        //rv_riwayat.adapter = adapter
        rv_riwayat.layoutManager = layoutManager
    }

    override fun onResume() {
        getRiwayat()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}