package com.example.nfaquatic.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.nfaquatic.R
import com.example.nfaquatic.helper.Helper
import com.example.nfaquatic.model.Produk
import com.example.nfaquatic.retrofit.MyDatabase
import com.example.nfaquatic.utils.Config
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class KeranjangAdapter(var activity: Activity, private var barangList:ArrayList<Produk>, var listener: Listeners):
    RecyclerView.Adapter<KeranjangAdapter.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tv_nama)
        val tvStock: TextView = view.findViewById(R.id.tv_stock)
        val tvHarga: TextView = view.findViewById(R.id.tv_harga)
        val imgProduk: ImageView = view.findViewById(R.id.img_produk)
        val layout: CardView = view.findViewById(R.id.layout)
        val btnTambah = view.findViewById<ImageView>(R.id.btn_tambah)
        val btnKurang = view.findViewById<ImageView>(R.id.btn_kurang)
        val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.keranjang_rv_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val barang = barangList[position]
        val price = Integer.valueOf(barang.price)
        var stock   = Integer.valueOf(barang.stock)

        holder.tvNama.text = barangList[position].name
        holder.tvHarga.text = Helper().gantiRupiah(price * barang.jumlah)
        holder.tvStock.text = barangList[position].stock.toString()
        var jumlah = barangList[position].jumlah
        holder.tvJumlah.text = jumlah.toString()

        holder.checkBox.isChecked = barang.selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            barang.selected = isChecked
            update(barang)
        }

        val image = Config.produkUrl + barangList[position].photo
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.hp_notebook_14_bs709tu)
            .error(R.drawable.hp_notebook_14_bs709tu)
            .into(holder.imgProduk)

        holder.btnTambah.setOnClickListener{
            if (jumlah >= stock) return@setOnClickListener
            //qty--
            jumlah++
            barang.jumlah = jumlah
            barang.stock = stock
            update(barang)
            holder.tvStock.text = stock.toString()
            holder.tvJumlah.text = jumlah.toString()
            holder.tvHarga.text = Helper().gantiRupiah(price * jumlah)
        }

        holder.btnKurang.setOnClickListener {
            if (jumlah <= 1) return@setOnClickListener
            jumlah--
            //qty++
            barang.jumlah = jumlah
            barang.stock = stock
            update(barang)
            holder.tvStock.text = stock.toString()
            holder.tvJumlah.text = jumlah.toString()
            holder.tvHarga.text = Helper().gantiRupiah(price * jumlah)
        }

        holder.btnDelete.setOnClickListener {
            delete(barang)
            listener.onDelete(position)

        }

    }

    override fun getItemCount(): Int {
        return barangList.size
    }

    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)
    }

    private fun update(data: Produk) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

    private fun delete(data: Produk) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            })
    }


}