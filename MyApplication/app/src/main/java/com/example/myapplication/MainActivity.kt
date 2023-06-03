package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.home.HomeViewModel
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

    public fun re_data_realtime (){
        OkHttpClient().newCall(Request.Builder().url("https://hoshisora000.lionfree.net/api/get_time.php").build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 請求失敗時的處理
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val gson = Gson()
                val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                data_realtime = jsonObject.getAsJsonObject("data").getAsJsonPrimitive("day").asString
            }
        })
    }

    public fun get_data_invoice () : String{
        var temp = ""
        try {
            temp = data_invoice
        }catch (e: Exception){

        }
        return temp
    }

    public fun get_data_realtime () : String{
        var temp = ""
        try {
            temp = data_realtime
        }catch (e: Exception){

        }
        return temp
    }

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