package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var data_invoice : String
    private lateinit var data_realtime : String

    private var navController: NavController? = null

    private var progressbar_value = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        val viewModelProvider = ViewModelProvider(this)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        FirebaseApp.initializeApp(this)

        if(Firebase.auth.currentUser == null){
            binding.linearLogin.visibility = View.VISIBLE
            binding.navView.visibility = View.GONE
            binding.mainLyfragement.visibility = View.GONE
        }

        re_data_invoice()
        re_data_realtime()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications ,R.id.navigation_award
            )
        )

        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //這冊頁面按鈕
        binding!!.butSignup.setOnClickListener {
            var intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        //登入按鈕
        binding!!.butLogin.setOnClickListener {
            val email = binding!!.editTextTextEmailAddress.text.toString()
            val password = binding!!.editTextTextPassword.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this, "請輸入帳號", Toast.LENGTH_SHORT).show()
            }else if(password.isEmpty()){
                Toast.makeText(this, "請輸入密碼", Toast.LENGTH_SHORT).show()
            }else{
                Firebase.auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val user = Firebase.auth.currentUser
                        re_data_invoice()
                        binding.linearLogin.visibility = View.GONE
                        binding.navView.visibility = View.VISIBLE
                        binding.mainLyfragement.visibility = View.VISIBLE
                    }else{
                        Toast.makeText(this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //取得資料庫發票資料
    public fun re_data_invoice (){
        progressbar(1)
        val request = Request.Builder()
            .url("https://hoshisora000.lionfree.net/api/query_invoice.php?uid="+ Firebase.auth.currentUser?.uid.toString())
            .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    data_invoice = response.body?.string().toString()
                    progressbar(-1)
                }
            }
        })
    }

    //取得資料庫真實世界日期資料
    public fun re_data_realtime (){
        progressbar(1)
        OkHttpClient().newCall(Request.Builder().url("https://hoshisora000.lionfree.net/api/get_time.php").build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val gson = Gson()
                val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                data_realtime = jsonObject.getAsJsonObject("data").getAsJsonPrimitive("day").asString
                progressbar(-1)
            }
        })
    }

    //回傳發票資料
    public fun get_data_invoice () : String{
        var temp = ""
        try {
            temp = data_invoice
        }catch (e: Exception){

        }
        return temp
    }

    //回傳時間資料
    public fun get_data_realtime () : String{
        var temp = ""
        try {
            temp = data_realtime
        }catch (e: Exception){

        }
        return temp
    }

    //等待畫面處理
    public fun progressbar(flag : Int){
        if(progressbar_value + flag >= 0) progressbar_value += flag

        this.runOnUiThread {
            if(progressbar_value <= 0){
                binding!!.progressBar2.visibility = View.GONE
                binding!!.butFask1.visibility = View.GONE
                binding!!.butFask2.visibility = View.GONE
            }else{
                binding!!.progressBar2.visibility = View.VISIBLE
                binding!!.butFask1.visibility = View.VISIBLE
                binding!!.butFask2.visibility = View.VISIBLE
            }
        }
    }

    public fun set_logout(){
        navController?.navigate(R.id.navigation_notifications)
        binding.linearLogin.visibility = View.VISIBLE
        binding.navView.visibility = View.GONE
        binding.mainLyfragement.visibility = View.GONE
    }
}