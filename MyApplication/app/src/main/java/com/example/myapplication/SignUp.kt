package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//-------------------
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//--------------------

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        val but = findViewById<Button>(R.id.but_login_send)

        but.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress_signup).text.toString()
            val password = findViewById<EditText>(R.id.editTextTextPassword_signup).text.toString()
            val reenterpassword = findViewById<EditText>(R.id.editTextTextPassword_signup_reenter).text.toString()

            if(email.isNullOrEmpty()){
                Toast.makeText(this, "請輸入信箱", Toast.LENGTH_SHORT).show()
            }else if(password.isNullOrEmpty()){7
                Toast.makeText(this, "請輸入密碼", Toast.LENGTH_SHORT).show()
            }else if(reenterpassword.isNullOrEmpty()){
                Toast.makeText(this, "請重新輸入密碼", Toast.LENGTH_SHORT).show()
            }else if(password == reenterpassword){
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                    if (it.isSuccessful){
                        Log.d("Test","成功註冊")
                        finish()
                    }else{
                        Log.w("Test","註冊失敗", it.exception) //it.exception是把錯誤原因記下來
                        showMessage("註冊會員失敗")
                    }
                }
            }else{
                Toast.makeText(this, "密碼輸入不一致", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMessage(message: String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("確定") { dialog, which -> }
        alertDialog.show()
    }
}