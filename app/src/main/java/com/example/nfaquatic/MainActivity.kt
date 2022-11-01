package com.example.nfaquatic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.nfaquatic.activity.MasukActivity
import com.example.nfaquatic.fragment.AkunFragment
import com.example.nfaquatic.fragment.HomeFragment
import com.example.nfaquatic.fragment.KeranjangFragment
import com.example.nfaquatic.helper.SharedPref
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val fragmentHome: Fragment = HomeFragment()
    private val fragmentKeranjang: Fragment = KeranjangFragment()
    private var fragmentAkun: Fragment = AkunFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    private var statusLogin = false
    private lateinit var s : SharedPref
    private var dariDetail: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        s = SharedPref(this)
        setUpBottomNav()
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessage, IntentFilter("event:keranjang"))
        /**
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d("FCM", token.toString())
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
        **/
    }

    private val mMessage: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            dariDetail = true
        }
    }

    private fun setUpBottomNav() {
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentKeranjang).hide(fragmentKeranjang).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnItemSelectedListener{ item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    callFragment(0, fragmentHome)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_keranjang -> {
                    callFragment(1, fragmentKeranjang)
                    return@setOnItemSelectedListener true

                }
                R.id.navigation_akun -> {
                    if (s.getStatusLogin()){
                        callFragment(2, fragmentAkun)
                        return@setOnItemSelectedListener true
                    }else {
                        startActivity(Intent(this, MasukActivity::class.java))
                    }

                }
            }
            //return@setOnItemSelectedListener true
            false
        }
    }


    private fun callFragment(int: Int, fragment: Fragment) {
        Log.d("Respons", "Akun")
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    override fun onResume() {
        if (dariDetail) {
            dariDetail = false
            callFragment(1, fragmentKeranjang)
        }
        super.onResume()
    }

}