package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class create : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        val bundle = intent.extras
        val intent = Intent(this,MainActivity::class.java)

        val but = findViewById<Button>(R.id.bt_create_submit)
        val text1 = findViewById<EditText>(R.id.editTextTextPersonName)
        val text2 = findViewById<EditText>(R.id.editTextTextPersonName2)
        val text3 = findViewById<EditText>(R.id.editTextTextPersonName3)
        val text4 = findViewById<EditText>(R.id.editTextTextPersonName5)
        val text5 = findViewById<EditText>(R.id.editTextTextPersonName4)

        but.setOnClickListener {
            bundle!!.putString("data_en",text1.text.toString())
            bundle.putString("data_int",text2.text.toString())
            bundle.putString("data_day",text3.text.toString())
            bundle.putString("data_time",text4.text.toString())
            bundle.putString("data_coast",text5.text.toString())
            intent.putExtras(bundle)
            setResult(RESULT_OK,intent)
            finish()
        }

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