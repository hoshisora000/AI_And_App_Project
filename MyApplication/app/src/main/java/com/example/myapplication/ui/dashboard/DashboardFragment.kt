package com.example.myapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentDashboardBinding
import com.example.myapplication.R
import android.widget.LinearLayout
import java.io.IOException
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.home.HomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val client = OkHttpClient()
    private var url_query_invoice = "https://hoshisora000.lionfree.net/api/query_invoice.php?uid="+Firebase.auth.currentUser?.uid.toString()
    private var url_delete_invoice = "https://hoshisora000.lionfree.net/api/delete_invoice.php"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val linearLayout = root.findViewById<LinearLayout>(R.id.linearLayout)

        //動態更新發票
        if(Firebase.auth.currentUser != null){
            re_btn(linearLayout,root)
            _binding!!.btReData.visibility = View.VISIBLE
        }else{
            _binding!!.btReData.visibility = View.GONE
        }


        //更新按鈕
        val btn = root.findViewById<Button>(R.id.bt_re_data)
        btn.setOnClickListener{
            linearLayout.removeAllViews() //移除目前所有按鈕
            re_btn(linearLayout,root) //重新取得資料更新
        }

        return root
    }

    //取得資料庫的發票資料 並動態生成發票按鈕
    private fun re_btn(linearLayout:LinearLayout,root:View){
        val request = Request.Builder()
            .url(url_query_invoice)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    //println(responseBody)

                    val gson = Gson()
                    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)

                    //取得目前資料筆數
                    val max = jsonObject
                        .getAsJsonObject("status")
                        .asJsonObject
                        .get("amount")
                        .asString
                        .toInt()

                    //解析資料並動態生成按鈕
                    for (i in 0 until max) {
                        //取得發票內容
                        val temp = jsonObject
                            .getAsJsonArray("data")[i]
                            .asJsonObject
                            .get("invoice_number")
                            .asString

                        //拆解發票資料 分為英文跟數字的部分
                        val en = temp.take(2)
                        val num = temp.substring(2)

                        //取得發票日期
                        val day = jsonObject
                            .getAsJsonArray("data")[i]
                            .asJsonObject
                            .get("date")
                            .asString

                        //取得發票時間
                        val time = jsonObject
                            .getAsJsonArray("data")[i]
                            .asJsonObject
                            .get("time")
                            .asString

                        //取得發票金額
                        val coast = jsonObject
                            .getAsJsonArray("data")[i]
                            .asJsonObject
                            .get("money")
                            .asString

                        //動態生成按鈕
                        requireActivity().runOnUiThread {
                            val button = Button(requireContext())
                            button.text = ""+en+"-"+num
                            button.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 0, 0, dpToPx(10))
                            }
                            button.setBackgroundResource(R.color.LightCoral)
                            button.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

                            //設定按鈕監聽行為
                            button.setOnClickListener {
                                AlertDialog.Builder(requireContext())
                                    .setTitle(""+en+"-"+num)
                                    .setMessage("購買日期:"+day+"\n購買時間:"+time+"\n購買金額:"+coast)
                                    .setPositiveButton("刪除"){ dialog,which->
                                        AlertDialog.Builder(requireContext())
                                            .setTitle("警告")
                                            .setMessage("確定要刪除發票資料嗎？")
                                            .setPositiveButton("確定") { dialog, which ->
                                                val formBody = FormBody.Builder()
                                                    .add("uid",Firebase.auth.currentUser?.uid.toString())
                                                    .add("invoice_number",""+en+num)
                                                    .build()

                                                val request = Request.Builder()
                                                    .url(url_delete_invoice)
                                                    .post(formBody)
                                                    .build()

                                                client.newCall(request).enqueue(object : Callback {
                                                    override fun onFailure(call: Call, e: IOException) {
                                                        e.printStackTrace()
                                                    }
                                                    override fun onResponse(call: Call, response: Response) {
                                                        if (response.isSuccessful) {
                                                            requireActivity().runOnUiThread {
                                                                button.visibility = View.GONE
                                                            }
                                                            val responseBody = response.body?.string()
                                                            println(responseBody)
                                                        } else {
                                                            println("Request failed")
                                                        }

                                                    }
                                                })
                                            }
                                            .setNegativeButton("取消") { dialog, which ->

                                            }
                                            .show()
                                    }.show()
                            }
                            linearLayout.addView(button)
                        }
                    }

                } else {
                    println("Request failed")
                }
            }
        })
    }

    //計算dp資料
    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
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