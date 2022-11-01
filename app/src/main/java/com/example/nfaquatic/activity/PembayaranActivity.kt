package com.example.nfaquatic.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nfaquatic.R
import com.example.nfaquatic.adapter.BankAdapter
import com.example.nfaquatic.helper.Helper
import com.example.nfaquatic.model.Bank
import com.example.nfaquatic.model.Chekout
import com.example.nfaquatic.model.ResponModel
import com.example.nfaquatic.model.Transaksi
import com.example.nfaquatic.retrofit.ApiConfig
import com.google.gson.Gson
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PembayaranActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var rv_data: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        toolbar = findViewById(R.id.toolbar)
        Helper().setToolbar(this, toolbar, "Pembayaran")
        rv_data = findViewById(R.id.rv_data)
        displayBank()
    }

    fun displayBank() {
        val arrBank = ArrayList<Bank>()
        arrBank.add(Bank("BCA", "12345678987", "Penerima", R.drawable.logo_bca))
        arrBank.add(Bank("BRI", "12345678987", "Penerima", R.drawable.logo_bri))
        arrBank.add(Bank("Mandiri", "12345678987", "Penerima", R.drawable.logo_madiri))

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_data.layoutManager = layoutManager
        rv_data.adapter = BankAdapter(arrBank, object : BankAdapter.Listeners {
            override fun onClicked(data: Bank, index: Int) {
                bayar(data)
            }
        })
    }

    fun bayar(bank: Bank) {
        val json = intent.getStringExtra("extra")!!.toString()
        val chekout = Gson().fromJson(json, Chekout::class.java)
        chekout.bank = bank.nama

        val loading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()

        ApiConfig.instanceRetrofit.chekout(chekout).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
//                Toast.makeText(this, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                if (!response.isSuccessful) {
                    error(response.message())
                    return
                }

                val respon = response.body()!!
                if (respon.success == 1) {

                    val jsBank = Gson().toJson(bank, Bank::class.java)
                    val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)
                    val jsChekout = Gson().toJson(chekout, Chekout::class.java)

                    val intent = Intent(this@PembayaranActivity, SuccessActivity::class.java)
                    intent.putExtra("bank", jsBank)
                    intent.putExtra("transaksi", jsTransaksi)
                    intent.putExtra("chekout", jsChekout)
                    startActivity(intent)

                } else {
                    error(respon.message)
                    Toast.makeText(this@PembayaranActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun error(pesan: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(pesan)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}