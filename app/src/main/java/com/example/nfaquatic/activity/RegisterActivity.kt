package com.example.nfaquatic.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.nfaquatic.MainActivity
import com.example.nfaquatic.R
import com.example.nfaquatic.helper.SharedPref
import com.example.nfaquatic.model.ResponModel
import com.example.nfaquatic.retrofit.ApiConfig
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var s: SharedPref
    lateinit var fcm: String
    private var pb: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        s = SharedPref(this)
        pb = findViewById<ProgressBar>(R.id.pb) as ProgressBar
        val btn_register: Button = findViewById(R.id.btn_register)
        val btn_google: RelativeLayout = findViewById(R.id.btn_google)

        getFcm()

        btn_register.setOnClickListener {
            register()
        }

        btn_google.setOnClickListener {
            dataDummy()
        }

    }


    private fun getFcm() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Respon", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            fcm = token.toString()
            // Log and toast
            Log.d("respon fcm:", token.toString())
        })
    }


    fun dataDummy() {
        var namaDummy: EditText = findViewById(R.id.edt_nama)
        var emailDummy: EditText = findViewById(R.id.edt_email)
        var phoneDummy: EditText = findViewById(R.id.edt_phone)
        var passwordDummy: EditText = findViewById(R.id.edt_password)

        namaDummy.setText("admin")
        emailDummy.setText("admin@gmail.com")
        phoneDummy.setText("0123456789")
        passwordDummy.setText("admin123")
    }

    fun register() {
        var edt_nama: EditText = findViewById(R.id.edt_nama)
        var edt_email: EditText = findViewById(R.id.edt_email)
        var edt_phone: EditText = findViewById(R.id.edt_phone)
        var edt_password: EditText = findViewById(R.id.edt_password)

        if (edt_nama.text.isEmpty()) {
            edt_nama.error = "Kolom Nama tidak boleh kosong"
            edt_nama.requestFocus()
            return
        } else if (edt_email.text.isEmpty()) {
            edt_email.error = "Kolom Email tidak boleh kosong"
            edt_email.requestFocus()
            return
        } else if (edt_phone.text.isEmpty()) {
            edt_phone.error = "Kolom Nomor Telepon tidak boleh kosong"
            edt_phone.requestFocus()
            return
        } else if (edt_password.text.isEmpty()) {
            edt_password.error = "Kolom Password tidak boleh kosong"
            edt_password.requestFocus()
            return
        }

        pb!!.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(edt_nama.text.toString(), edt_email.text.toString(), edt_phone.text.toString(), edt_password.text.toString(), fcm).enqueue(object :
            Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb!!.visibility = View.GONE
                val respon = response.body()!!
                if (respon.success == 1) {
                    s.setStatusLogin(true)
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity, "Selamat datang " + respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb!!.visibility = View.GONE
                Toast.makeText(this@RegisterActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

}