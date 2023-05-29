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
import com.example.myapplication.SignUp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.myapplication.databinding.ActivityMainBinding
//import com.google.android.play.core.integrity.client.R
import com.google.firebase.auth.FirebaseUser

//--------------------

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

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
            _binding!!.editTextTextPassword.visibility = View.GONE
            _binding!!.editTextTextEmailAddress.visibility = View.GONE
            _binding!!.butLogin.visibility = View.GONE
            _binding!!.butSignup.visibility = View.GONE

            _binding!!.butLogout.visibility = View.VISIBLE
        }else{
            _binding!!.editTextTextPassword.visibility = View.VISIBLE
            _binding!!.editTextTextEmailAddress.visibility = View.VISIBLE
            _binding!!.butLogin.visibility = View.VISIBLE
            _binding!!.butSignup.visibility = View.VISIBLE

            _binding!!.butLogout.visibility = View.GONE
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