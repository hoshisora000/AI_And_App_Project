package com.example.myapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentDashboardBinding
import com.example.myapplication.R
import android.widget.LinearLayout
import java.io.IOException
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.myapplication.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var btn_invoice: Array<Button?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if(Firebase.auth.currentUser != null){
            set_spinner(root)

            //更新按鈕
            _binding!!.btReData.setOnClickListener{
                _binding!!.linearLayout.removeAllViews() //移除目前所有按鈕
                val mainActivity = activity as MainActivity

                Thread{
                    mainActivity.re_data_invoice()
                    mainActivity.progressbar()
                    Thread.sleep(500)
                    re_btn(root) //重新取得資料更新
                    Thread.sleep(500)
                    mainActivity.progressbar()
                    re_btn_UI()
                }.start()
            }

        }else{
            _binding!!.btReData.visibility = View.GONE
            _binding!!.spinner.visibility = View.GONE
        }

        return root
    }

    //取得資料庫的發票資料 並動態生成發票按鈕
    private fun re_btn(root:View){
        val mainActivity = activity as MainActivity
        val gson = Gson()
        val jsonObject = gson.fromJson(mainActivity.get_data_invoice(), JsonObject::class.java)

        //取得目前資料筆數
        val max = jsonObject
            .getAsJsonObject("status")
            .asJsonObject
            .get("amount")
            .asString
            .toInt()

        btn_invoice = arrayOfNulls<Button>(max)
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
                btn_invoice[i] = Button(requireContext())
                btn_invoice[i]?.text = ""+en+"-"+num
                btn_invoice[i]?.tag = day.substring(0,7)
                btn_invoice[i]?.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, dpToPx(10))
                }
                btn_invoice[i]?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.LightCoral))
                /*
                val identifier = context?.resources?.getIdentifier("round_back_white", "drawable", context?.packageName)
                identifier?.let { resourceId ->
                    btn_invoice[i]?.setBackgroundResource(resourceId)
                }
                */
                btn_invoice[i]?.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                btn_invoice[i]?.visibility =View.GONE

                //設定按鈕監聽行為
                btn_invoice[i]?.setOnClickListener {
                    AlertDialog.Builder(requireContext())
                        .setTitle(""+en+"-"+num)
                        .setMessage("購買日期:"+day+"\n購買時間:"+time+"\n購買金額:"+coast)
                        .setNegativeButton("刪除"){ dialog,which->
                            AlertDialog.Builder(requireContext())
                                .setTitle("警告")
                                .setMessage("確定要刪除發票資料嗎？")
                                .setPositiveButton("確定") { dialog, which ->
                                    Thread{
                                        val formBody = FormBody.Builder()
                                            .add("uid",Firebase.auth.currentUser?.uid.toString())
                                            .add("invoice_number",""+en+num)
                                            .build()

                                        val request = Request.Builder()
                                            .url("https://hoshisora000.lionfree.net/api/delete_invoice.php")
                                            .post(formBody)
                                            .build()

                                        OkHttpClient().newCall(request).enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {
                                                e.printStackTrace()
                                            }
                                            override fun onResponse(call: Call, response: Response) {
                                                if (response.isSuccessful) {
                                                    requireActivity().runOnUiThread {
                                                        _binding!!.linearLayout.removeAllViews()

                                                    }
                                                    Thread{
                                                        mainActivity.re_data_invoice()
                                                        mainActivity.progressbar()
                                                        Thread.sleep(500)
                                                        re_btn(root) //重新取得資料更新
                                                        Thread.sleep(500)
                                                        mainActivity.progressbar()
                                                        re_btn_UI()
                                                    }.start()
                                                    val responseBody = response.body?.string()
                                                    println(responseBody)
                                                } else {
                                                    println("Request failed")
                                                }
                                            }
                                        })
                                    }.start()
                                }
                                .setNegativeButton("取消") { dialog, which ->

                                }
                                .show()
                        }.setPositiveButton("取消") { dialog, which ->

                        }.show()
                }
                _binding!!.linearLayout.addView(btn_invoice[i])
            }
        }
    }

    //下拉式選單資料更新
    private fun set_spinner(root: View){
        val mainActivity = activity as MainActivity
        val options = arrayOfNulls<String>(10)
        var y = mainActivity.get_data_realtime().substring(0,4).toInt()
        var m :Int
        if(mainActivity.get_data_realtime().substring(5,7).toInt() % 2 == 0){
            m = mainActivity.get_data_realtime().substring(5,7).toInt() - 1
        }else{
            m = mainActivity.get_data_realtime().substring(5,7).toInt()
        }
        for (i in 0 until 10 ){
            options[i] = y.toString()+"年 "
            if(m<10) options[i] += "0"
            options[i] += m.toString()+"月-"
            if(m+1<10) options[i] += "0"
            options[i] += (m+1).toString()+"月"
            if(m==1){
                y -= 1
                m = 11
            }else{
                m -= 2
            }
        }
        requireActivity().runOnUiThread {
            val adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, options){
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val textView = view.findViewById<TextView>(android.R.id.text1)
                    textView.textSize = 18f // 設定字體大小為 18sp，你可以根據需要調整數值
                    return view
                }
            }
            _binding!!.spinner.adapter = adapter
            re_btn(root)
        }

        //下拉式選單監聽行為
        _binding!!.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                re_btn_UI()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    //更新按鈕顯示
    private fun re_btn_UI(){
        try {
            for(i in 0 until btn_invoice.size){
                val spi = _binding!!.spinner.selectedItem.toString()
                val btntag = btn_invoice[i]?.tag.toString()
                if(btntag.substring(0,4) == spi.substring(0,4) && (btntag.substring(5,7) == spi.substring(6,8) || btntag.substring(5,7) == spi.substring(10,12))){
                    requireActivity().runOnUiThread {
                        btn_invoice[i]?.visibility = View.VISIBLE
                    }
                }else{
                    requireActivity().runOnUiThread {
                        btn_invoice[i]?.visibility = View.GONE
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
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