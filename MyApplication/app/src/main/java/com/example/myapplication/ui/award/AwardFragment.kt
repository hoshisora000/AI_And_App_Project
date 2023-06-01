package com.example.myapplication.ui.award

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAwardBinding

class AwardFragment : Fragment() {

    private var _binding: FragmentAwardBinding? = null
    private val binding get() = _binding!!

    private val btn_award = arrayOfNulls<Button>(12)
    private  val text_award = arrayOfNulls<EditText>(3)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAwardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        set_award_UI()
        set_spinner()

        for (i in 0 until 10){
            btn_award[i]?.setOnClickListener {
                if(text_award[0]?.text.toString() == ""){
                    text_award[0]?.setText(((i+1)%10).toString())
                }else if(text_award[1]?.text.toString()  == ""){
                    text_award[1]?.setText(((i+1)%10).toString())
                }else if(text_award[2]?.text.toString()  == ""){
                    text_award[2]?.setText(((i+1)%10).toString())
                }else {
                    text_award[0]?.setText(null)
                    text_award[1]?.setText(null)
                    text_award[2]?.setText(null)

                    text_award[0]?.setText(((i+1)%10).toString())
                }
            }
        }

        btn_award[10]?.setOnClickListener {
            text_award[0]?.setText(null)
            text_award[1]?.setText(null)
            text_award[2]?.setText(null)
        }

        btn_award[11]?.setOnClickListener {
            if(text_award[2]?.text.toString() != ""){
                text_award[2]?.setText(null)
            }else if(text_award[1]?.text.toString() != ""){
                text_award[1]?.setText(null)
            }else{
                text_award[0]?.setText(null)
            }
        }


        return root
    }

    private fun set_award_UI(){
        btn_award[0] = _binding!!.button2
        btn_award[1] = _binding!!.button3
        btn_award[2] = _binding!!.button4
        btn_award[3] = _binding!!.button5
        btn_award[4] = _binding!!.button6
        btn_award[5] = _binding!!.button7
        btn_award[6] = _binding!!.button8
        btn_award[7] = _binding!!.button9
        btn_award[8] = _binding!!.button10
        btn_award[9] = _binding!!.button12
        btn_award[10] = _binding!!.button11
        btn_award[11] = _binding!!.button13

        text_award[0] = _binding!!.editTextTextPersonName6
        text_award[1] = _binding!!.editTextTextPersonName7
        text_award[2] = _binding!!.editTextTextPersonName8

        text_award[0]?.setText(null)
        text_award[1]?.setText(null)
        text_award[2]?.setText(null)
    }

    private fun set_spinner(){
        val mainActivity = activity as MainActivity
        val options = arrayOfNulls<String>(3)
        var y = mainActivity.get_data_realtime().substring(0,4).toInt()
        var m :Int
        if(mainActivity.get_data_realtime().substring(5,7).toInt() % 2 == 0){
            m = mainActivity.get_data_realtime().substring(5,7).toInt() - 1
        }else{
            m = mainActivity.get_data_realtime().substring(5,7).toInt()
        }
        for (i in 0 until 3 ){
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
            _binding!!.spinner2.adapter = adapter
        }
    }

    private fun showToast(message: String) {
        val context = requireContext()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}