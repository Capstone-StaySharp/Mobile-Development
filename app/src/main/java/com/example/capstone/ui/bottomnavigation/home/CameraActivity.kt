package com.example.capstone.ui.bottomnavigation.home

import android.Manifest
//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.capstone.data.room.DrowsinessDatabase
import com.example.capstone.data.room.DrowsinessRecord
import com.example.capstone.databinding.ActivityCameraBinding
import com.example.capstone.ui.bottomnavigation.home.Utils.bitmapToFile
import com.example.capstone.ui.bottomnavigation.home.Utils.isInternetAvailable
import com.example.capstone.ui.bottomnavigation.home.Utils.rotateBitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Suppress("DEPRECATION", "DEPRECATION")
class CameraActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var faceDetector: FaceDetector
    private lateinit var binding: ActivityCameraBinding
    private val viewModel: CameraViewModel by viewModels ()

    private val cameraPermissionRequestCode = 1001
    private val lastCapturedFaces: ArrayList<CapturedFace> = ArrayList()
    private val lastCapturedDrowsy: ArrayList<Boolean> = ArrayList()
    private val lastCapturedYawning: ArrayList<Boolean> = ArrayList()
    private val handler = Handler(Looper.getMainLooper())
    private var isSending = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        faceDetector = FaceDetection.getClient(options)

        if (isCameraPermissionGranted()) {
            startCamera()
        } else {
            requestCameraPermission()
            finish()
        }

        if (isInternetAvailable(this)) {
            startPeriodicUpload()
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()
            showNoInternetDialog()
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraPermissionRequestCode)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImage(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )
            } catch (e: Exception) {
                // Handle error
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("SetTextI18n", "RestrictedApi")
    @OptIn(ExperimentalGetImage::class)
    private fun processImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        val bitmap = imageProxy.toBitmap()
                        val rotatedBitmap = rotateBitmap(bitmap, imageProxy.imageInfo.rotationDegrees)

                        val timestamp = System.currentTimeMillis()
                        lastCapturedFaces.add(CapturedFace(rotatedBitmap, timestamp))

                        val currentTime = System.currentTimeMillis()
                        lastCapturedFaces.removeAll { it.timestamp < currentTime - 1000 }
                        binding.statusText.text = "Face detected"
                    } else {
                        isSending = false
                        lastCapturedFaces.clear()
                        binding.statusText.text = "No face detected"
                    }
                }
                .addOnFailureListener {
                    binding.statusText.text = "Error detecting face"
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun startPeriodicUpload() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (lastCapturedFaces.isNotEmpty() && !isSending) {
                    isSending = true
                    val latestFace = lastCapturedFaces.last()
                    val file = bitmapToFile(latestFace.bitmap, this@CameraActivity)

                    uploadFace(file)
                }
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    @SuppressLint("SetTextI18n")
    fun uploadFace(file: File) {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val startTime = System.currentTimeMillis()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = viewModel.uploadFace(body)
                Log.d("RESPONSE", response.toString())
                val endTime = System.currentTimeMillis()
                val latency = endTime - startTime

                withContext(Dispatchers.Main) {
                    if (response.success == true) {
                        response.message?.let { Log.d("RESPONSE_MESSAGE", it) }

                        if (response.data?.isBothEyesClosed == true) {
                            lastCapturedDrowsy.add(true)
                            Log.d("LastCaptureddrowsy", "$lastCapturedDrowsy")

                            if (lastCapturedDrowsy.size >= 4) {
                                showDrowsyAlertDialog(file, "You have been detected drowsy")
                            }
                        } else {
                            lastCapturedDrowsy.clear()
                            Log.d("LastCaptureddrowsy", "$lastCapturedDrowsy")
                        }
                        if (response.data?.isYawning == true) {
                            lastCapturedYawning.add(true)

                            if (lastCapturedYawning.size >= 3) {
                                showDrowsyAlertDialog(file, "You have been detected yawning")
                            }
                        }else{
                            lastCapturedYawning.clear()
                        }
                    } else {
                        response.message?.let { Log.d("RESPONSE_MESSAGE", it) }
                    }
                    Log.d("LATENCY", "Response latency: ${latency}ms")
                }
            } catch (e: Exception) {
                val endTime = System.currentTimeMillis()
                val latency = endTime - startTime
                withContext(Dispatchers.Main) {
                    binding.statusText.text = "Upload error: ${e.message}\nLatency: ${latency}ms"
                    Log.d("LATENCY", "Error latency: ${latency}ms")
                }
                Log.e("ERROR", e.message.toString())
            } finally {
                isSending = false
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun showDrowsyAlertDialog(file: File, message: String) {
        lastCapturedDrowsy.clear()
        lastCapturedYawning.clear()

        val imageFile = file
        val dateFormat = SimpleDateFormat("HH:mm MM-dd-yyyy", Locale.getDefault())
        val timestamp = dateFormat.format(Date())

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(android.os.VibrationEffect.createWaveform(longArrayOf(0, 500, 500), 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 500, 500), 0)
        }

        val mediaPlayer = MediaPlayer()
        try {
            val assetFileDescriptor = assets.openFd("alarm.mp3")
            mediaPlayer.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
            )
            mediaPlayer.prepare()
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Drowsiness Detected")
            .setMessage("You have been detected as drowsy. Please take a break.")
            .setPositiveButton("OK") { _, _ ->
                vibrator.cancel()
                mediaPlayer.stop()
                mediaPlayer.release()

                // Simpan ke database
                saveDrowsinessRecord(imageFile, timestamp, message)
            }
            .setIcon(R.drawable.ic_dialog_alert)
            .create()

        dialog.setCancelable(false)
        dialog.show()
    }



    private fun showNoInternetDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
                finish()
        }
        dialog.create().show()
    }

    private fun saveDrowsinessRecord(imageFile: File, timestamp: String, message: String) {
        val database = DrowsinessDatabase.getDatabase(this)
        val dao = database.drowsinessDao()

        CoroutineScope(Dispatchers.IO).launch {
            val record = DrowsinessRecord(
                image = imageFile,
                timestamp = timestamp,
                message = message
            )
            dao.insertRecord(record)
            Log.d("DATABASE", "Record saved: $record")
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
        isSending = false
    }

}