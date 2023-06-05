package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityTraditionalInvoiceBinding
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


class traditional_invoice : AppCompatActivity() {

    private lateinit var binding: ActivityTraditionalInvoiceBinding
    private  lateinit var  imageView: ImageView
    lateinit var imageUri: Uri
    lateinit var outputImage: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traditional_invoice)

        binding = ActivityTraditionalInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding!!.imageView

        val bundle = intent.extras
        val intent = Intent(this,MainActivity::class.java)

        //設定輸出圖片檔案
        outputImage = File(externalCacheDir, "output_image.jpg")

        //啟動相機
        binding!!.btTraOpencamera.setOnClickListener {
            //如果圖片已存在就刪除就擋
            if (outputImage.exists()) {
                outputImage.delete()
            }

            //啟動相機並將拍攝照片更新
            outputImage.createNewFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "com.example.cameraalbumtest.fileprovider", outputImage)
            } else {
                Uri.fromFile(outputImage)
            }
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            takePhotoLauncher.launch(intent)
        }

        //新增發票按鈕
        binding!!.btTraSend.setOnClickListener {
            //判斷是否得到辨識結果
            if(binding!!.textView9.text == "請先啟動相機進行拍攝"){
                Toast.makeText(this, "請先拍攝照片", Toast.LENGTH_SHORT).show()
            }else if(binding!!.textView9.text == "辨識結果為：notfound" || binding!!.textView9.text == "辨識結果為：tryagain"){
                Toast.makeText(this, "請重新拍攝", Toast.LENGTH_SHORT).show()
            }else{
                //回傳辨識結果
                val value = binding!!.textView9.text.substring(6,16)
                bundle!!.putString("Scan",value)
                bundle!!.putString("Scan_en",value.substring(0,2))
                bundle!!.putString("Scan_num",value.substring(2,10))
                intent.putExtras(bundle)
                setResult(RESULT_OK,intent)
                finish()
            }
        }
    }

    //解析辨識結果
    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
            imageView.setImageBitmap(rotateIfRequired(bitmap))

            //設定連線超時
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", outputImage.name, outputImage.asRequestBody())
                .build()

            val request: Request = Request.Builder()
                .url("http://34.96.209.0:3000/")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                //更新辨識結果
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val textresult = findViewById<TextView>(R.id.textView9)
                        textresult.setText("辨識結果為："+responseBody)
                    }
                }
            })
        }
    }

    //照片轉向處理
    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }
}