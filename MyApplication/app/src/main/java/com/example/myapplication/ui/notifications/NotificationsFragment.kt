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
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    private val requestDataLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK){
            val data = it.data
            //showToast(""+data?.getStringExtra("data_en"))

            val client = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("uid", "O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6")
                .add("invoice_number", ""+data?.getStringExtra("data_en")+data?.getStringExtra("data_int"))
                .add("date", ""+data?.getStringExtra("data_day"))
                .add("time", ""+data?.getStringExtra("data_time")+":00")
                .add("money",""+data?.getStringExtra("data_coast"))
                .build()

            val url = "https://hoshisora000.lionfree.net/api/add_invoice.php"
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
                        //findViewById<TextView>(R.id.TextView).text = responseBody
                    } else {
                        println("Request failed")
                    }
                }
            })
        }
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bt_click(root)

        return root
    }

    private fun bt_click(root:View){
        val bt_creat = root.findViewById<Button>(R.id.bt_create)
        val bundle_creat = Bundle()
        val intent_creat = Intent(requireActivity(),create::class.java)
        bt_creat.setOnClickListener {
            intent_creat.putExtras(bundle_creat)
            requestDataLauncher.launch(intent_creat)
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