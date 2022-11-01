package com.example.nfaquatic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.nfaquatic.R
import com.example.nfaquatic.helper.Helper
import com.example.nfaquatic.model.DetailTransaksi
import com.example.nfaquatic.utils.Config
import com.squareup.picasso.Picasso

class ProdukTransaksiAdapter(var data: ArrayList<DetailTransaksi>) : RecyclerView.Adapter<ProdukTransaksiAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvTotalHarga = view.findViewById<TextView>(R.id.tv_totalHarga)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk_transaksi, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        val name = a.produk.name
        val p = a.produk
        holder.tvNama.text = name
        holder.tvHarga.text = Helper().gantiRupiah(p.price)
        holder.tvTotalHarga.text = Helper().gantiRupiah(a.total_harga)
        holder.tvJumlah.text = a.total_item.toString() + " Items"

        holder.layout.setOnClickListener {
//            listener.onClicked(a)
        }

        val image = Config.produkUrl + p.photo
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.hp_notebook_14_bs709tu)
            .error(R.drawable.hp_notebook_14_bs709tu)
            .into(holder.imgProduk)
    }

    interface Listeners {
        fun onClicked(data: DetailTransaksi)
    }
}