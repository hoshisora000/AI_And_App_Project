package com.example.myapplication.ui.analyze

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.FragmentAnalyzeBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mainActivity = activity as MainActivity

        requireActivity().runOnUiThread {
            binding.analyzeTime.setText((mainActivity.get_data_realtime().substring(0,4).toInt()-1911).toString()+"年"+mainActivity.get_data_realtime().substring(5,7)+"月")
            get_analyze_data()
        }

        binding.analyzeBtleft.setOnClickListener{
            if(binding.analyzeTime.text.substring(4,6).toInt()-1 == 0){
                binding.analyzeTime.setText((binding.analyzeTime.text.substring(0,3).toInt()-1).toString()+"年12月")
            }else if(binding.analyzeTime.text.substring(4,6).toInt()-1 < 10){
                binding.analyzeTime.setText((binding.analyzeTime.text.substring(0,3)+"年0"+(binding.analyzeTime.text.substring(4,6).toInt()-1).toString()+"月"))
            }else{
                binding.analyzeTime.setText((binding.analyzeTime.text.substring(0,3)+"年"+(binding.analyzeTime.text.substring(4,6).toInt()-1).toString()+"月"))
            }
            get_analyze_data()
        }

        binding.analyzeBtright.setOnClickListener{
            if(binding.analyzeTime.text.substring(4,6).toInt()+1 == 13){
                binding.analyzeTime.setText((binding.analyzeTime.text.substring(0,3).toInt()+1).toString()+"年01月")
            }else if(binding.analyzeTime.text.substring(4,6).toInt()+1 < 10){
                binding.analyzeTime.setText((binding.analyzeTime.text.substring(0,3)+"年0"+(binding.analyzeTime.text.substring(4,6).toInt()+1).toString()+"月"))
            }else{
                binding.analyzeTime.setText((binding.analyzeTime.text.substring(0,3)+"年"+(binding.analyzeTime.text.substring(4,6).toInt()+1).toString()+"月"))
            }
            get_analyze_data()
        }

        return root
    }

    private fun get_analyze_data(){
        val formBody = FormBody.Builder()
            .add("uid", Firebase.auth.currentUser?.uid.toString())
            .add("year_month",binding.analyzeTime.text.substring(0,3)+binding.analyzeTime.text.substring(4,6))
            .build()

        val request = Request.Builder()
            .url("https://hoshisora000.lionfree.net/api/analysis.php")
            .post(formBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val gson = Gson()
                    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)

                    requireActivity().runOnUiThread {
                        try{
                            binding.analyzeText1.setText("總消費次數"+jsonObject.get("record").asInt+"次")

                            binding.analyzeValu1.setText(jsonObject
                                .getAsJsonObject("data")
                                .asJsonObject
                                .get("midnight")
                                .asString)

                            binding.analyzeValu2.setText(jsonObject
                                .getAsJsonObject("data")
                                .asJsonObject
                                .get("morning")
                                .asString)

                            binding.analyzeValu3.setText(jsonObject
                                .getAsJsonObject("data")
                                .asJsonObject
                                .get("afternoon")
                                .asString)

                            binding.analyzeValu4.setText(jsonObject
                                .getAsJsonObject("data")
                                .asJsonObject
                                .get("night")
                                .asString)

                            binding.analyzeValu1.setText(binding.analyzeValu1.text.toString() + "次")
                            binding.analyzeValu2.setText(binding.analyzeValu2.text.toString() + "次")
                            binding.analyzeValu3.setText(binding.analyzeValu3.text.toString() + "次")
                            binding.analyzeValu4.setText(binding.analyzeValu4.text.toString() + "次")

                            binding.analyzeCost1.setText(jsonObject
                                .getAsJsonObject("data")
                                .asJsonObject
                                .get("midnight_money")
                                .asString)

                            binding.analyzeCost2.setText(jsonObject
                                .getAsJsonObject("data")
                                .asJsonObject
                                .get("morning_money")
                                .asString)

                            binding.analyzeCost3.setText(jsonObject
                                .getAsJsonObject("data")
                                .asJsonObject
                                .get("afternoon_money")
                                .asString)

                            binding.analyzeCost4.setText(jsonObject
                                .getAsJsonObject("data")
                                .asJsonObject
                                .get("night_money")
                                .asString)

                            binding.analyzeCost1.setText(binding.analyzeCost1.text.toString() + "元")
                            binding.analyzeCost2.setText(binding.analyzeCost2.text.toString() + "元")
                            binding.analyzeCost3.setText(binding.analyzeCost3.text.toString() + "元")
                            binding.analyzeCost4.setText(binding.analyzeCost4.text.toString() + "元")

                            val analyzeValu1 = binding.analyzeValu1.text.substring(0,binding.analyzeValu1.length()-1).toFloat()
                            val analyzeValu2 = binding.analyzeValu2.text.substring(0,binding.analyzeValu2.length()-1).toFloat()
                            val analyzeValu3 = binding.analyzeValu3.text.substring(0,binding.analyzeValu3.length()-1).toFloat()
                            val analyzeValu4 = binding.analyzeValu4.text.substring(0,binding.analyzeValu4.length()-1).toFloat()
                            val maxValue = maxOf(analyzeValu1, analyzeValu2, analyzeValu3, analyzeValu4)

                            if(maxValue == analyzeValu1){
                                binding.analyzeText2.setText("該月您最熱門的消費時段("+analyzeValu1.toInt().toString()+"次)")
                                binding.analyzeText3.setText(binding.analyzeLabel1.text)
                            }else if(maxValue == analyzeValu2){
                                binding.analyzeText2.setText("該月您最熱門的消費時段("+analyzeValu2.toInt().toString()+"次)")
                                binding.analyzeText3.setText(binding.analyzeLabel2.text)
                            }else if(maxValue == analyzeValu3){
                                binding.analyzeText2.setText("該月您最熱門的消費時段("+analyzeValu3.toInt().toString()+"次)")
                                binding.analyzeText3.setText(binding.analyzeLabel3.text)
                            }else if(maxValue == analyzeValu4){
                                binding.analyzeText2.setText("該月您最熱門的消費時段("+analyzeValu4.toInt().toString()+"次)")
                                binding.analyzeText3.setText(binding.analyzeLabel4.text)
                            }

                            setUpSelectionPieChart()
                        }catch (e:Exception){

                        }
                    }
                } else {
                    println("Request failed")
                }
            }
        })
    }
    private fun setUpSelectionPieChart() {

        val analyzeValu1 = binding.analyzeValu1.text.substring(0,binding.analyzeValu1.length()-1).toFloat()
        val analyzeValu2 = binding.analyzeValu2.text.substring(0,binding.analyzeValu2.length()-1).toFloat()
        val analyzeValu3 = binding.analyzeValu3.text.substring(0,binding.analyzeValu3.length()-1).toFloat()
        val analyzeValu4 = binding.analyzeValu4.text.substring(0,binding.analyzeValu4.length()-1).toFloat()
        val analyzeValueSum = analyzeValu1 + analyzeValu2 + analyzeValu3 + analyzeValu4

        //Create a dataset
        val dataArray = ArrayList<PieEntry>()
        dataArray.add(PieEntry(analyzeValu1 / analyzeValueSum * 100,"凌晨"))
        dataArray.add(PieEntry(analyzeValu2 / analyzeValueSum * 100,"上午"))
        dataArray.add(PieEntry(analyzeValu3 / analyzeValueSum * 100,"下午"))
        dataArray.add(PieEntry(analyzeValu4 / analyzeValueSum * 100,"晚上"))
        val dataSet = PieDataSet(dataArray,"")
        dataSet.valueTextSize=15f
        dataSet.valueTextColor=Color.WHITE

        val valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.1f%%", value) // 格式化為帶有一位小數的百分比
            }
        }

        // 設定數值格式器給 PieData 物件的 DataSet
        dataSet.valueFormatter = valueFormatter

        //Color set for the chart
        val colorSet = java.util.ArrayList<Int>()
        colorSet.add(ContextCompat.getColor(requireContext(),R.color.buttom1))
        colorSet.add(ContextCompat.getColor(requireContext(),R.color.buttom2))
        colorSet.add(ContextCompat.getColor(requireContext(),R.color.buttom3))
        colorSet.add(ContextCompat.getColor(requireContext(),R.color.buttom4))
        dataSet.setColors(colorSet)

        val data = PieData(dataSet)
        val pieChart = binding.pieChart

        //chart description
        pieChart.description.text = "該月分析"
        pieChart.description.textSize = 20f

        //Chart data and other styling
        pieChart.data = data
        pieChart.centerTextRadiusPercent = 0f
        pieChart.isDrawHoleEnabled = true
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = true

        pieChart.invalidate()
    }
}