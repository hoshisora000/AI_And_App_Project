package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Intent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.myapplication.databinding.ActivityScanBinding
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Scan : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private var enableAnalyze = true
    private var continuous = true

    private lateinit var binding: ActivityScanBinding

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .build()
    )

    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()
        imageAnalysis.setAnalyzer(cameraExecutor, this::analyzeBitmap)
        binding.previewView.startCamera(this, imageAnalysis)
        binding.previewView.setOnClickListener {
            startAnalyze()
        }
    }

    private fun startAnalyze() {
        enableAnalyze = true
    }

    private fun stopAnalyze() {
        enableAnalyze = false
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun analyzeBitmap(imageProxy: ImageProxy) {
        if (!enableAnalyze) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener(this::onSuccess)
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    //回傳掃朴結果
    private fun onSuccess(result: List<Barcode>) {
        if (!result.isEmpty()){
            val value = result.joinToString { it.displayValue!! }

            //判斷左右QRcode
            if(value[0] != '*' && value[1] != '*'){
                try {
                    val bundle = intent.extras
                    val intent = Intent(this,MainActivity::class.java)
                    bundle!!.putString("Scan",value)
                    bundle!!.putString("Scan_en",value.substring(0,2))
                    bundle!!.putString("Scan_num",value.substring(2,10))
                    bundle!!.putString("Scan_year",(value.substring(10,13).toInt()+1911).toString())
                    bundle!!.putString("Scan_month",value.substring(13,15))
                    bundle!!.putString("Scan_day",value.substring(15,17))
                    bundle!!.putString("Scan_cost",value.substring(29,37).toInt(16).toString())
                    intent.putExtras(bundle)
                    setResult(RESULT_OK,intent)
                    finish()
                } catch (e: StringIndexOutOfBoundsException) {

                }
            }else{
                return
            }
        }

        if (!continuous) {
            stopAnalyze()
        }
    }

    private fun PreviewView.startCamera(
        lifecycleOwner: LifecycleOwner,
        imageAnalysis: ImageAnalysis
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(surfaceProvider)
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis,
            )
        }, ContextCompat.getMainExecutor(context))
    }

    override fun onDestroy() {
        scanner.close()
        cameraExecutor.shutdown()
        super.onDestroy()
    }
}