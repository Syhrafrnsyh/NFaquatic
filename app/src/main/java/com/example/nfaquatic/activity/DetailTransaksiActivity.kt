package com.example.nfaquatic.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nfaquatic.R
import com.example.nfaquatic.adapter.ProdukTransaksiAdapter
import com.example.nfaquatic.helper.Helper
import com.example.nfaquatic.model.DetailTransaksi
import com.example.nfaquatic.model.ResponModel
import com.example.nfaquatic.model.Transaksi
import com.example.nfaquatic.retrofit.ApiConfig
import com.google.gson.Gson
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTransaksiActivity : AppCompatActivity() {

    var transaksi = Transaksi()
    lateinit var rv_produk: RecyclerView
    lateinit var div_footer: LinearLayout
    lateinit var tv_penerima: TextView
    lateinit var tv_alamat: TextView
    lateinit var tv_tgl: TextView
    lateinit var tv_kodeUnik: TextView
    lateinit var tv_totalBelanja: TextView
    lateinit var tv_ongkir: TextView
    lateinit var tv_total: TextView
    lateinit var tv_status: TextView
    lateinit var toolbar: Toolbar
    lateinit var btn_batal: Button



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi)
        initez()
        Helper().setToolbar(this, toolbar, "Detail Transaksi")

        val json = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson(json, Transaksi::class.java)

        setData(transaksi)
        displayProduk(transaksi.details)
        mainButton()
    }

    fun initez(){
        toolbar = findViewById(R.id.toolbar)
        rv_produk = findViewById(R.id.rv_produk)
        div_footer = findViewById(R.id.div_footer)
        tv_penerima = findViewById(R.id.tv_penerima)
        tv_alamat = findViewById(R.id.tv_alamat)
        tv_tgl = findViewById(R.id.tv_tgl)
        tv_kodeUnik = findViewById(R.id.tv_kodeUnik)
        tv_totalBelanja = findViewById(R.id.tv_totalBelanja)
        tv_ongkir = findViewById(R.id.tv_ongkir)
        tv_total = findViewById(R.id.tv_total)
        tv_status = findViewById(R.id.tv_status)
        btn_batal = findViewById(R.id.btn_batal)
    }

    private fun mainButton() {
        btn_batal.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Transaksi akan di batalkan dan tidak bisa di kembalikan!")
                .setConfirmText("Yes, Batalkan")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                    batalTransaksi()
                }
                .setCancelText("Tutup")
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }.show()
        }
    }

    fun batalTransaksi() {
        val loading = SweetAlertDialog(this@DetailTransaksiActivity, SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Loading...").show()
        ApiConfig.instanceRetrofit.batalChekout(transaksi.id).enqueue(object :
            Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
                error(t.message.toString())
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                val res = response.body()!!
                if (res.success == 1) {
                    SweetAlertDialog(this@DetailTransaksiActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success...")
                        .setContentText("Transaksi berhasil dibatalakan")
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                            onBackPressed()
                        }
                        .show()

//                    Toast.makeText(this@DetailTransaksiActivity, "Transaksi berhasil di batalkan", Toast.LENGTH_SHORT).show()
//                    onBackPressed()
//                    displayRiwayat(res.transaksis)
                } else {
                    error(res.message)
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun setData(t: Transaksi) {
        tv_status.text = t.status

        val formatBaru = "dd MMMM yyyy, kk:mm:ss"
        tv_tgl.text = Helper().convertTanggal(t.created_at, formatBaru)

        tv_penerima.text = t.name + " - " + t.phone
        tv_alamat.text = t.detail_lokasi
        tv_kodeUnik.text = Helper().gantiRupiah(t.kode_unik)
        tv_totalBelanja.text = Helper().gantiRupiah(t.total_harga)
        tv_ongkir.text = Helper().gantiRupiah(t.ongkir)
        tv_total.text = Helper().gantiRupiah(t.total_transfer)

        if (t.status != "MENUNGGU") div_footer.visibility = View.GONE

        var color = getColor(R.color.menungu)
        if (t.status == "SELESAI") color = getColor(R.color.selesai)
        else if (t.status == "BATAL") color = getColor(R.color.batal)

        tv_status.setTextColor(color)
    }

    fun displayProduk(transaksis: ArrayList<DetailTransaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_produk.adapter = ProdukTransaksiAdapter(transaksis)
        rv_produk.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}