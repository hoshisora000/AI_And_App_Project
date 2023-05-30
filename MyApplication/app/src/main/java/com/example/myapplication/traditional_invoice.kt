package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ActivityTraditionalInvoiceBinding
import java.io.ByteArrayOutputStream
import java.io.File

class traditional_invoice : AppCompatActivity() {

    private lateinit var binding: ActivityTraditionalInvoiceBinding
    private  lateinit var  imageView: ImageView
    lateinit var imageUri: Uri
    lateinit var outputImage: File

    private val modelPath = "best-fp16.tflite"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traditional_invoice)

        binding = ActivityTraditionalInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding!!.imageView

        binding!!.button.setOnClickListener {
            outputImage = File(externalCacheDir, "output_image.jpg")
            if (outputImage.exists()) {
                outputImage.delete()
            }
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
    }

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Photo was taken successfully
                var bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                imageView.setImageBitmap(rotateIfRequired(bitmap))

            }
        }

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
        // 将不需要的Bitmap对象回收
        bitmap.recycle()
        return rotatedBitmap
    }


}