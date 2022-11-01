package com.example.nfaquatic.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.nfaquatic.R
import com.example.nfaquatic.activity.RiwayatActivity
import com.example.nfaquatic.helper.SharedPref

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AkunFragment : Fragment() {

    lateinit var s: SharedPref
    lateinit var btnLogout: TextView
    lateinit var tvNama: TextView
    lateinit var tvEmail: TextView
    lateinit var tvPhone: TextView

    lateinit var btnRiwayat: RelativeLayout

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)
        init(view)

        s = SharedPref(requireActivity())

        mainButton()
        setData()
        return view
    }


    fun mainButton() {
        btnLogout.setOnClickListener {
            s.setStatusLogin(false)
        }

        btnRiwayat.setOnClickListener {
            startActivity(Intent(requireActivity(), RiwayatActivity::class.java))
        }
    }

    fun setData() {

        if (s.getUser() == null) {
//            val intent = Intent(activity, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
            return
        }

        val user = s.getUser()!!

        tvNama.text = user.name
        tvEmail.text = user.email
//        tvPhone.text = user.phone
    }

    private fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        btnRiwayat = view.findViewById(R.id.btn_riwayat)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}