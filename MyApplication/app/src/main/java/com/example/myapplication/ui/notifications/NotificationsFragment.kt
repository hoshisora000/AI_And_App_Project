package com.example.myapplication.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.create
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.example.myapplication.ui.home.HomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

class NotificationsFragment : Fragment() {

    private val client = OkHttpClient()
    private val url = "https://hoshisora000.lionfree.net/api/add_invoice.php"
    private var user_id = ""

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        user_id = Firebase.auth.currentUser?.uid.toString()

        bt_click(root) //按下按鈕

        return root
    }

    //接收creat表單之資料並上傳新增資料
    private val requestDataLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK){
            val data = it.data

            val formBody = FormBody.Builder()
                .add("uid", user_id)
                .add("invoice_number", ""+data?.getStringExtra("data_en")+data?.getStringExtra("data_int"))
                .add("date", ""+data?.getStringExtra("data_day"))
                .add("time", ""+data?.getStringExtra("data_time")+":00")
                .add("money",""+data?.getStringExtra("data_coast"))
                .build()

            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        println(responseBody)
                    } else {
                        println("Request failed")
                    }
                }
            })
        }
    }

    private fun bt_click(root:View){
        val bt_creat = root.findViewById<Button>(R.id.bt_create)
        val bt_create_ai = root.findViewById<Button>(R.id.bt_create_ai)
        val bt_create_qr = root.findViewById<Button>(R.id.bt_create_qr)

        val bundle_creat = Bundle()
        val intent_creat = Intent(requireActivity(),create::class.java)

        //手動更新按鈕 開啟creat表單並要求回傳質
        bt_creat.setOnClickListener {
            if(Firebase.auth.currentUser != null){
                intent_creat.putExtras(bundle_creat)
                requestDataLauncher.launch(intent_creat)
            }else{
                showToast("請先登入帳號")
            }
        }

        //傳統發票掃描按鈕
        bt_create_ai.setOnClickListener {

        }

        //電子發票掃描按鈕
        bt_create_qr.setOnClickListener {

        }
    }

    private fun showToast(message: String) {
        val context = requireContext()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}