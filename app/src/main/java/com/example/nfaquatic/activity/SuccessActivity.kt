package com.example.nfaquatic.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.nfaquatic.MainActivity
import com.example.nfaquatic.R
import com.example.nfaquatic.helper.Helper
import com.example.nfaquatic.model.Bank
import com.example.nfaquatic.model.Chekout
import com.example.nfaquatic.model.Transaksi
import com.example.nfaquatic.retrofit.MyDatabase
import com.google.gson.Gson

class SuccessActivity : AppCompatActivity() {

    private var nominal = 0
    lateinit var toolbar: Toolbar

    private lateinit var btn_cekStatus: Button
    private lateinit var btn_copyNoRek: ImageView
    private lateinit var tv_nomorRekening: TextView
    private lateinit var btn_copyNominal: ImageView
    private lateinit var tv_namaPenerima: TextView
    private lateinit var tv_nominal: TextView
    private lateinit var image_bank: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        toolbar = findViewById(R.id.toolbar)
        btn_cekStatus = findViewById(R.id.btn_cekStatus)
        btn_copyNoRek = findViewById(R.id.btn_copyNoRek)
        tv_nomorRekening = findViewById(R.id.tv_nomorRekening)
        btn_copyNominal = findViewById(R.id.btn_copyNominal)
        tv_namaPenerima = findViewById(R.id.tv_namaPenerima)
        tv_nominal = findViewById(R.id.tv_nominal)
        image_bank = findViewById(R.id.image_bank)

        Helper().setToolbar(this, toolbar, "Bank Transfer")

        setValues()
        mainButton()
    }

    fun mainButton() {
        btn_copyNoRek.setOnClickListener {
            copyText(tv_nomorRekening.text.toString())
        }

        btn_copyNominal.setOnClickListener {
            copyText(nominal.toString())
        }

        btn_cekStatus.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
        }
    }

    private fun copyText(text: String) {
        val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyText = ClipData.newPlainText("text", text)
        copyManager.setPrimaryClip(copyText)

        Toast.makeText(this, "Text berhasil di Kopi", Toast.LENGTH_LONG).show()
    }

    private fun setValues() {
        val jsBank = intent.getStringExtra("bank")
        val jsTransaksi = intent.getStringExtra("transaksi")
        val jsCheckout = intent.getStringExtra("chekout")

        val bank = Gson().fromJson(jsBank, Bank::class.java)
        val transaksi = Gson().fromJson(jsTransaksi, Transaksi::class.java)
        val chekout = Gson().fromJson(jsCheckout, Chekout::class.java)

        // hapus keranjang
        val myDb = MyDatabase.getInstance(this)!!
        for (barang in chekout.produks){
            myDb.daoKeranjang().deleteById(barang.id)
        }

        tv_nomorRekening.text = bank.rekening
        tv_namaPenerima.text = bank.penerima
        image_bank.setImageResource(bank.image)

        nominal = Integer.valueOf(transaksi.total_transfer) + transaksi.kode_unik
        tv_nominal.text = Helper().gantiRupiah(nominal)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

}