package com.example.myapplication

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        val viewModelProvider = ViewModelProvider(this)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Thread{
            re_data_invoice()
            re_data_realtime()
            progressbar()
            Thread.sleep(3000)
            progressbar()
        }.start()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications ,R.id.navigation_award
            )
        )

        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //取得資料庫發票資料
    public fun re_data_invoice (){
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
                }
            }
        })
    }

    //取得資料庫真實世界日期資料
    public fun re_data_realtime (){
        OkHttpClient().newCall(Request.Builder().url("https://hoshisora000.lionfree.net/api/get_time.php").build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val gson = Gson()
                val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                data_realtime = jsonObject.getAsJsonObject("data").getAsJsonPrimitive("day").asString
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
    public fun progressbar(){
        this.runOnUiThread {
            if(binding!!.progressBar2.visibility == View.VISIBLE){
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
}