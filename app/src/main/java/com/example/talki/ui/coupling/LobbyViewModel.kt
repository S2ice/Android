package com.example.talki.ui.coupling

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

class LobbyViewModel : ViewModel() {
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraDevice: CameraDevice
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler

    var isCameraPermissionGranted: Boolean = false
    var isCameraOpen: Boolean = false

    fun initCamera(context: Context) {
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("CameraThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    fun requestCameraPermission(activity: AppCompatActivity) {
        if (!isCameraPermissionGranted) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun checkCameraPermission(context: Context): Boolean {
        isCameraPermissionGranted =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        return isCameraPermissionGranted
    }

    fun openCamera(cameraId: String, surfaceTexture: SurfaceTexture, activity: AppCompatActivity) {
        if (!isCameraPermissionGranted) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
            return
        }

        try {
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    isCameraOpen = true
                    createCameraPreviewSession(surfaceTexture)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                    isCameraOpen = false
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camera.close()
                    isCameraOpen = false
                }
            }, handler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun createCameraPreviewSession(surfaceTexture: SurfaceTexture) {
        surfaceTexture.setDefaultBufferSize(640, 480)
        val surface = Surface(surfaceTexture)

        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder.addTarget(surface)

            cameraDevice.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameraCaptureSession = session
                    updatePreview()
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    // Обработать сбой конфигурации
                }
            }, handler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun updatePreview() {
        try {
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, handler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun closeCamera() {
        if (isCameraOpen) {
            cameraCaptureSession.close()
            cameraDevice.close()
            isCameraOpen = false
        }
        handlerThread.quitSafely()
    }

    override fun onCleared() {
        super.onCleared()
        closeCamera()
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }
}