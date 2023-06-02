package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.R

//-------------------
import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.myapplication.MainActivity
import com.example.myapplication.SignUp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.myapplication.databinding.ActivityMainBinding
//import com.google.android.play.core.integrity.client.R
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

//--------------------

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val client = OkHttpClient()
    private val url_query_member = "https://hoshisora000.lionfree.net/api/query_member.php"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //----------------------
        auth = Firebase.auth
        _binding!!.butSignup.setOnClickListener {
            var intent = Intent(requireActivity(), SignUp::class.java)
            startActivity(intent)
        }

        updateUI_home(Firebase.auth.currentUser)

        _binding!!.butLogout.setOnClickListener {
            auth.signOut()
            updateUI_home(Firebase.auth.currentUser)

            val mainActivity = activity as MainActivity
            mainActivity.re_data_invoice()
        }

        _binding!!.butLogin.setOnClickListener {
            val email = _binding!!.editTextTextEmailAddress.text.toString()
            val password = _binding!!.editTextTextPassword.text.toString()

            if(email.isEmpty()){
                showToast("請輸入帳號")
            }else if(password.isEmpty()){
                showToast("請輸入密碼")
            }else{
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val user = auth.currentUser
                        updateUI_home(Firebase.auth.currentUser)

                        val mainActivity = activity as MainActivity
                        mainActivity.re_data_invoice()
                    }else{
                        showToast("登入失敗，帳號或密碼錯誤")
                        updateUI_home(null)
                    }
                }
            }
        }

        return root
    }

    private fun updateUI_home(user:FirebaseUser?){
        if(user != null){
            _binding!!.linearLogin.visibility = View.GONE

            _binding!!.butLogout.visibility = View.VISIBLE
            _binding!!.textMember.visibility =View.VISIBLE

            getmember()
        }else{
            _binding!!.linearLogin.visibility = View.VISIBLE

            _binding!!.butLogout.visibility = View.GONE
            _binding!!.textMember.visibility =View.GONE
        }
    }

    private fun getmember(){
        val request = Request.Builder()
            .url(url_query_member+"?uid="+Firebase.auth.currentUser?.uid.toString())
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val gson = Gson()
                    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                    println(jsonObject)

                    val nickname = jsonObject
                        .getAsJsonArray("data")[0]
                        .asJsonObject
                        .get("nickname")
                        .asString

                    val mobile_barcode = jsonObject
                        .getAsJsonArray("data")[0]
                        .asJsonObject
                        .get("mobile_barcode")
                        .asString

                    requireActivity().runOnUiThread {
                        _binding!!.textMember.setText(nickname+" 您好")
                    }
                } else {
                    println("Request failed")
                }
            }
        })
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