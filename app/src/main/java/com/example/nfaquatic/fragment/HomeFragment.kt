package com.example.nfaquatic.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.nfaquatic.R
import com.example.nfaquatic.adapter.ProdukAdapter
import com.example.nfaquatic.model.Produk
import com.example.nfaquatic.model.ResponModel
import com.example.nfaquatic.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    lateinit var vpSlider: ViewPager
    lateinit var rvProduk: RecyclerView
    lateinit var rvProdukTerlasir: RecyclerView
    lateinit var rvElektronik: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        getProduk()

        return view
    }

    fun displayProduk() {
        Log.d("cekini", "size:" + listProduk.size)
        /**
        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slider1)
        arrSlider.add(R.drawable.slider2)
        arrSlider.add(R.drawable.slider3)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        vpSlider.adapter = adapterSlider
        **/

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = LinearLayoutManager(activity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL

        rvProduk.adapter = ProdukAdapter(requireActivity(), listProduk)
        rvProduk.layoutManager = layoutManager
        rvProduk.setHasFixedSize(true)
        rvProdukTerlasir.adapter = ProdukAdapter(requireActivity(), listProduk)
        rvProdukTerlasir.layoutManager = layoutManager2

        rvElektronik.adapter = ProdukAdapter(requireActivity(), listProduk)
        rvElektronik.layoutManager = layoutManager3


    }

    private var listProduk: ArrayList<Produk> = ArrayList()
    /**
    fun getProduk() {
        ApiConfig.instanceRetrofit.getProduk().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arrayProduk = ArrayList<Produk>()
                    for (p in res.produks) {
                        p.discount = 100000
                        arrayProduk.add(p)
                    }
                    listProduk = arrayProduk
                    displayProduk()
                }
            }
        })
    }
    **/
    private fun getProduk() {
        val call: Call<ResponModel> = ApiConfig.instanceRetrofit.getProduk()
        call.enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arrayProduk = ArrayList<Produk>()
                    for (p in res.produks) {
                        //p.discount = 100000
                        arrayProduk.add(p)
                    }
                    listProduk = arrayProduk
                    displayProduk()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }
        })

    }

    fun init(view: View) {
        //vpSlider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvProdukTerlasir = view.findViewById(R.id.rv_produkTerlasir)
        rvElektronik = view.findViewById(R.id.rv_elektronik)
    }

}