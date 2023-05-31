package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityTraditionalInvoiceBinding
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList


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
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                imageView.setImageBitmap(rotateIfRequired(bitmap))

                val file = File(externalCacheDir, "output_image.jpg")
                /*
                val client = OkHttpClient()

                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull()))
                    .build()

                val request: Request = Request.Builder()
                    .url("https://34.150.40.187:8000/upload")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        // 处理请求失败的情况
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        // 处理请求成功的情况
                        if (response.isSuccessful) {
                            val responseBody = response.body?.string()
                            // 处理服务器响应的数据
                            println(responseBody)
                        }
                    }
                })

                try {
                    val tfliteModel: ByteBuffer = FileUtil.loadMappedFile(this, "best-fp16.tflite")
                    val tflite = Interpreter(tfliteModel, Interpreter.Options())
                    val associatedAxisLabels = FileUtil.loadLabels(this, "labels.txt")
                    val OUTPUT_SIZE = intArrayOf(1, 6300, 85)
                    val INPNUT_SIZE = Size(416, 416)

                    var yolov5sTfliteInput: TensorImage
                    val imageProcessor: ImageProcessor = ImageProcessor.Builder()
                        .add(ResizeOp(INPNUT_SIZE.getHeight(), INPNUT_SIZE.getWidth(), ResizeOp.ResizeMethod.BILINEAR))
                        .add(NormalizeOp(0f, 255f))
                        .build()
                    yolov5sTfliteInput = TensorImage(DataType.FLOAT32)
                    yolov5sTfliteInput.load(bitmap)
                    yolov5sTfliteInput = imageProcessor.process(yolov5sTfliteInput)

                    val probabilityBuffer = TensorBuffer.createFixedSize(OUTPUT_SIZE, DataType.FLOAT32)
                    tflite.run(yolov5sTfliteInput.buffer, probabilityBuffer.buffer)

                    val recognitionArray = probabilityBuffer.floatArray

                    val allRecognitions = ArrayList<Recognition>()
                    for (i in 0 until OUTPUT_SIZE[1]) {
                        val gridStride = i * OUTPUT_SIZE[2]
                        // 由於 YOLOv5 在導出 TFLite 時將輸出除以了圖像大小，因此這裡需要乘回去
                        val x = recognitionArray[0 + gridStride] * INPNUT_SIZE.getWidth()
                        val y = recognitionArray[1 + gridStride] * INPNUT_SIZE.getHeight()
                        val w = recognitionArray[2 + gridStride] * INPNUT_SIZE.getWidth()
                        val h = recognitionArray[3 + gridStride] * INPNUT_SIZE.getHeight()
                        val xmin = (x - w / 2.0f).coerceAtLeast(0F).toInt()
                        val ymin = (y - h / 2.0f).coerceAtLeast(0F).toInt()
                        val xmax = (x + w / 2.0f).coerceAtMost(416F).toInt()
                        val ymax = (y + h / 2.0f).coerceAtMost(416F).toInt()
                        val confidence = recognitionArray[4 + gridStride]

                        val classScores: FloatArray = Arrays.copyOfRange(
                            recognitionArray,
                            5 + gridStride,
                            OUTPUT_SIZE.get(2) + gridStride
                        )

                        // 找到最大分數對應的類別標籤
                        var labelId = 0
                        var maxLabelScores = 0.0f
                        for (j in classScores.indices) {
                            if (classScores[j] > maxLabelScores) {
                                maxLabelScores = classScores[j]
                                labelId = j
                            }
                        }

                        // 創建 Recognition 物件並將結果加入到 allRecognitions 列表中
                        val r = Recognition(
                            labelId,
                            "",
                            maxLabelScores,
                            confidence,
                            RectF(xmin.toFloat(), ymin.toFloat(), xmax.toFloat(), ymax.toFloat())
                        )
                        allRecognitions.add(r)
                    }
                    Log.i("tfliteSupport", "recognize data size: "+allRecognitions.size);
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                */
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