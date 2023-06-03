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
import android.graphics.Bitmap
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.google.zxing.oned.Code39Writer
import okhttp3.*
import java.io.IOException

//--------------------

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val client = OkHttpClient()
    private var membercodabar = ""
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

        _binding!!.layoutCodabarBt.setOnClickListener {
            if(membercodabar == ""){
                showToast("尚未設定手機載具")
            }else{
                if(_binding!!.layoutCodabar.visibility == View.GONE){
                    _binding!!.layoutCodabar.visibility = View.VISIBLE
                }else{
                    _binding!!.layoutCodabar.visibility = View.GONE
                }
            }
        }

        _binding!!.layoutMemberBt.setOnClickListener {
            if(_binding!!.layoutMember.visibility == View.GONE){
                _binding!!.textMemberNickname.setText(_binding!!.textMember.text.substring(0,_binding!!.textMember.text.length-3))
                _binding!!.textMemberCode.setText(membercodabar)
                _binding!!.layoutMember.visibility = View.VISIBLE
            }else{
                _binding!!.layoutMember.visibility = View.GONE
            }
        }

        _binding!!.btLayoutMember.setOnClickListener {
            if(_binding!!.textMemberNickname.text.toString() == ""){
                showToast("請輸入暱稱")
            }else{
                Thread{
                    val mainActivity = activity as MainActivity
                    mainActivity.progressbar()

                    val formBody = FormBody.Builder()
                        .add("uid", Firebase.auth.currentUser?.uid.toString())
                        .add("nickname", _binding!!.textMemberNickname.text.toString())
                        .add("mobile_barcode", _binding!!.textMemberCode.text.toString() )
                        .build()

                    OkHttpClient().newCall(Request.Builder()
                        .url("https://hoshisora000.lionfree.net/api/update_member_inf.php")
                        .post(formBody)
                        .build()).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }
                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                val responseBody = response.body?.string()
                                println(responseBody)

                                getmember()
                            } else {
                                println("Request failed")
                            }
                        }
                    })

                    Thread.sleep(1000)
                    requireActivity().runOnUiThread {
                        _binding!!.layoutMember.visibility =View.GONE
                        _binding!!.textMember.setText(_binding!!.textMemberNickname.text.toString()+" 您好")
                    }
                    mainActivity.progressbar()
                }.start()
            }
        }

        _binding!!.layoutMemberpasswordBt.setOnClickListener {
            if(_binding!!.layoutMemberpassword.visibility == View.GONE){
                _binding!!.layoutMemberpassword.visibility = View.VISIBLE
            }else{
                _binding!!.layoutMemberpassword.visibility = View.GONE
            }
        }

        _binding!!.btLayoutMemberpassword.setOnClickListener {
            if(_binding!!.textMemberPassword.text.toString() == ""){
                showToast("請輸入新密碼")
            }else if(_binding!!.textMemberPasswordcheck.text.toString() == ""){
                showToast("請重新輸入密碼")
            }else if(_binding!!.textMemberPassword.text.toString() != _binding!!.textMemberPasswordcheck.text.toString()){
                showToast("密碼輸入不一致")
            }else{
                Thread{
                    val mainActivity = activity as MainActivity
                    mainActivity.progressbar()

                    val user = FirebaseAuth.getInstance().currentUser
                    user?.updatePassword(_binding!!.textMemberPassword.text.toString())?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // 密碼修改成功
                            showToast("密碼修改成功")

                            _binding!!.textMemberPassword.setText("")
                            _binding!!.textMemberPasswordcheck.setText("")
                        } else {
                            // 密碼修改失敗
                            showToast("登入超時，請重新登入以修改密碼")
                            _binding!!.textMemberPassword.setText("")
                            _binding!!.textMemberPasswordcheck.setText("")
                        }
                    }

                    Thread.sleep(1000)
                    requireActivity().runOnUiThread {
                        _binding!!.layoutMemberpassword.visibility =View.GONE
                    }
                    mainActivity.progressbar()
                }.start()
            }
        }

        _binding!!.layoutAppupdateBt.setOnClickListener{
            showToast("您已擁有最新版本")
        }

        _binding!!.layoutAppverBt.setOnClickListener{
            showToast("Ver 1.0.0")
        }

        _binding!!.layoutInfoBt.setOnClickListener{
            showToast("            -製作者-\nCHIH-HSUAN WANG\n      YOU-CHEN LIN ")
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
                    try {
                        val responseBody = response.body?.string()
                        val gson = Gson()
                        val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                        println(jsonObject)

                        val nickname = jsonObject
                            .getAsJsonArray("data")[0]
                            .asJsonObject
                            .get("nickname")
                            .asString

                        membercodabar = jsonObject
                            .getAsJsonArray("data")[0]
                            .asJsonObject
                            .get("mobile_barcode")
                            .asString

                        requireActivity().runOnUiThread {
                            _binding!!.textMember.setText(nickname+" 您好")
                        }

                        set_member_codabar(membercodabar)
                    }catch (e:Exception){

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

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun set_member_codabar(value: String) {
        val barcodeColor = ContextCompat.getColor(requireContext(), R.color.black)
        val backgroundColor = ContextCompat.getColor(requireContext(), android.R.color.white)

        val bitMatrix = Code39Writer().encode(value,BarcodeFormat.CODE_39,dpToPx(300),dpToPx(80))

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrix.width,bitMatrix.height,Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels,0,bitMatrix.width,0,0,bitMatrix.width,bitMatrix.height)

        requireActivity().runOnUiThread{
            _binding!!.imageBarcode.setImageBitmap(bitmap)
            _binding!!.textBarcodeNumber.text = value
        }
    }
}