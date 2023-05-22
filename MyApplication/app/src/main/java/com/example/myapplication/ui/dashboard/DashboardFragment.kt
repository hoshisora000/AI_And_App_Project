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
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.home.HomeViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val client = OkHttpClient()
    private var url = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        url = "https://hoshisora000.lionfree.net/api/query_invoice.php?uid="+homeViewModel.USER_ID

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val linearLayout = root.findViewById<LinearLayout>(R.id.linearLayout)

        re_btn(linearLayout,root)

        val btn = root.findViewById<Button>(R.id.bt_re_data)
        btn.setOnClickListener{
            linearLayout.removeAllViews()
            re_btn(linearLayout,root)
        }

        return root
    }

    private fun re_btn(linearLayout:LinearLayout,root:View){
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    println(responseBody)

                    val gson = Gson()
                    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)

                    val max = jsonObject
                        .getAsJsonObject("status")
                        .asJsonObject
                        .get("amount")
                        .asString
                        .toInt()

                    for (i in 0 until max) {
                        val temp = jsonObject
                            .getAsJsonArray("data")[i]
                            .asJsonObject
                            .get("invoice_number")
                            .asString

                        val en = temp.take(2)
                        val num = temp.substring(2)

                        val day = jsonObject
                            .getAsJsonArray("data")[i]
                            .asJsonObject
                            .get("date")
                            .asString

                        val time = jsonObject
                            .getAsJsonArray("data")[i]
                            .asJsonObject
                            .get("time")
                            .asString

                        val coast = jsonObject
                            .getAsJsonArray("data")[i]
                            .asJsonObject
                            .get("money")
                            .asString

                        requireActivity().runOnUiThread {
                            val button = Button(requireContext())
                            button.text = ""+en+"-"+num
                            button.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 0, 0, dpToPx(10))
                            }
                            button.setBackgroundResource(android.R.color.holo_blue_dark)
                            button.setOnClickListener {
                                AlertDialog.Builder(requireContext())
                                    .setTitle(""+en+"-"+num)
                                    .setMessage("購買日期:"+day+"\n購買時間:"+time+"\n購買金額:"+coast)
                                    .show()
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