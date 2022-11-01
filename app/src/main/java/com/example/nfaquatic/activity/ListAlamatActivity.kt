package com.example.nfaquatic.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nfaquatic.R
import com.example.nfaquatic.adapter.AlamatAdapter
import com.example.nfaquatic.helper.Helper
import com.example.nfaquatic.model.Alamat
import com.example.nfaquatic.retrofit.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListAlamatActivity : AppCompatActivity() {

    lateinit var myDb : MyDatabase
    lateinit var toolbar: Toolbar
    lateinit var div_kosong: LinearLayout
    lateinit var rv_alamat: RecyclerView
    lateinit var btn_tambahAlamat: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_alamat)
        toolbar = findViewById(R.id.toolbar)
        div_kosong = findViewById(R.id.div_kosong)
        rv_alamat = findViewById(R.id.rv_alamat)
        btn_tambahAlamat = findViewById(R.id.btn_tambahAlamat)
        Helper().setToolbar(this, toolbar, "Pilih Alamat")
        myDb = MyDatabase.getInstance(this)!!
        mainButton()
    }

    private fun displayAlamat() {
        val arrayList = myDb.daoAlamat().getAll() as ArrayList

        if (arrayList.isEmpty()) div_kosong.visibility = View.VISIBLE
        else div_kosong.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_alamat.adapter = AlamatAdapter(arrayList, object : AlamatAdapter.Listeners {
            override fun onClicked(data: Alamat) {
                if (myDb.daoAlamat().getByStatus(true) != null){
                    val alamatActive = myDb.daoAlamat().getByStatus(true)!!
                    alamatActive.isSelected = false
                    updateActive(alamatActive, data)
                }
            }
        })
        rv_alamat.layoutManager = layoutManager
    }

    private fun updateActive(dataActive: Alamat, dataNonActive: Alamat) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(dataActive) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                updateNonActive(dataNonActive)
            })
    }

    private fun updateNonActive(data: Alamat) {
        data.isSelected = true
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onBackPressed()
            })
    }

    override fun onResume() {
        displayAlamat()
        super.onResume()
    }

    private fun mainButton() {
        btn_tambahAlamat.setOnClickListener {
            startActivity(Intent(this, TambahAlamatActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}