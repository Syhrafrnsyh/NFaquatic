package com.example.nfaquatic.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.nfaquatic.R
import com.example.nfaquatic.activity.DetailProdukActivity
import com.example.nfaquatic.helper.Helper
import com.example.nfaquatic.model.Produk
import com.example.nfaquatic.utils.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class ProdukAdapter(var activity: Activity, var data: ArrayList<Produk>) : RecyclerView.Adapter<ProdukAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvHargaAsli = view.findViewById<TextView>(R.id.tv_hargaAsli)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.produk_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        val priceAsli = Integer.valueOf(a.price)
        var price = Integer.valueOf(a.price)

        if (a.discount != 0) {
            price -= a.discount
        }

        holder.tvHargaAsli.text = Helper().gantiRupiah(priceAsli)
        holder.tvHargaAsli.paintFlags = holder.tvHargaAsli.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = Helper().gantiRupiah(price)
        val image = Config.produkUrl + data[position].photo
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.hp_notebook_14_bs709tu)
            .error(R.drawable.hp_notebook_14_bs709tu)
            .into(holder.imgProduk)

        holder.layout.setOnClickListener {
            val activiti = Intent(activity, DetailProdukActivity::class.java)
            val str = Gson().toJson(data[position], Produk::class.java)
            activiti.putExtra("extra", str)
            activity.startActivity(activiti)
        }
    }

}