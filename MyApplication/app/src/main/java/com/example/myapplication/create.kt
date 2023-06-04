package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.myapplication.databinding.ActivityCreateBinding
import java.util.*

class create : AppCompatActivity() {

    private lateinit var binding:ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val intent = Intent(this,MainActivity::class.java)
        val text3 = findViewById<EditText>(R.id.editTextTextPersonName3)
        val text4 = findViewById<EditText>(R.id.editTextTextPersonName5)

        if(bundle?.getString("Scan_en") != null){
            binding!!.editTextTextPersonName.setText(bundle?.getString("Scan_en"))
            binding!!.editTextTextPersonName2.setText(bundle?.getString("Scan_num"))
        }

        if(bundle?.getString("Scan_year") != null){
            binding!!.editTextTextPersonName3.setText(bundle?.getString("Scan_year")+"-"+bundle?.getString("Scan_month")+"-"+bundle?.getString("Scan_day"))
            binding!!.editTextTextPersonName4.setText(bundle?.getString("Scan_cost"))
        }
        //回傳表單資料
        binding.btCreateSubmit.setOnClickListener {
            bundle!!.putString("data_en",binding!!.editTextTextPersonName.text.toString())
            bundle.putString("data_int",binding!!.editTextTextPersonName2.text.toString())
            bundle.putString("data_day",binding!!.editTextTextPersonName3.text.toString())
            bundle.putString("data_time",binding!!.editTextTextPersonName5.text.toString())
            bundle.putString("data_coast",binding!!.editTextTextPersonName4.text.toString())
            intent.putExtras(bundle)
            setResult(RESULT_OK,intent)
            finish()
        }

        //連接DatePicker
        text3.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this,
                { view, year, month, day ->
                    text3.setText("")
                    text3.setText(year.toString()+ "-")
                    if((month + 1).toInt()<10) text3.setText(text3.text.toString()+"0")
                    text3.setText(text3.text.toString()+(month + 1).toString() + "-")
                    if(day.toInt()<10) text3.setText(text3.text.toString()+"0")
                    text3.setText(text3.text.toString()+ day.toString())
                },year,month,day)
            datePickerDialog.show()
        }

        //連接TimePicker
        text4.setOnClickListener {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                this,
                { view, hourOfDay, minute ->
                    text4.setText("")
                    if (hourOfDay.toInt()<10) text4.setText(text4.text.toString()+"0")
                    text4.setText(text4.text.toString()+"$hourOfDay:")
                    if (minute.toInt()<10) text4.setText(text4.text.toString()+"0")
                    text4.setText(text4.text.toString()+"$minute")
                }, hour, minute,
                //true
                false
            )
            timePickerDialog.show()
        }
    }
}