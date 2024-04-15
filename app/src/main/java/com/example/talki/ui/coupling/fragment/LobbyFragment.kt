package com.example.talki.ui.coupling.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.talki.R
import com.example.talki.databinding.FragmentLobbyBinding
import com.example.talki.ui.coupling.LobbyViewModel

class LobbyFragment : Fragment(){
    private var _binding: FragmentLobbyBinding? = null
    private val binding get() = _binding!!

    private var cameraCharacteristics: CameraCharacteristics? = null

    lateinit var capReq: CaptureRequest.Builder
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var cameraDevice: CameraDevice

    lateinit var defaultImageView: ImageView

    private val viewModel: LobbyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lobbyViewModel =
            ViewModelProvider(this).get(LobbyViewModel::class.java)
        get_permissions()
        _binding = FragmentLobbyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        textureView = root.findViewById(R.id.localVideoContainer)
        defaultImageView = root.findViewById<ImageView>(R.id.defaultImageView)

        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler((handlerThread).looper)

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(pO: SurfaceTexture, p1: Int, p2: Int) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    defaultImageView.visibility = View.VISIBLE
                } else {

                    defaultImageView.visibility = View.GONE
                    open_camera()
                }
            }

            override fun onSurfaceTextureSizeChanged(pO: SurfaceTexture, p1: Int, p2: Int) {

            }

            override fun onSurfaceTextureDestroyed(pO: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(pO: SurfaceTexture) {

            }
        }

        return root
    }

    private fun setAspectRatioTextureView(cameraCharacteristics: CameraCharacteristics) {
        val streamConfigurationMap =
            cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                ?: return
        val sizes = streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)
        if (sizes.isNotEmpty()) {
            val previewSize = sizes.first()
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textureView.setAspectRatio(previewSize.width, previewSize.height)
            } else {
                textureView.setAspectRatio(previewSize.height, previewSize.width)
            }
        }
    }

    private var aspectRatioWidth = 0
    private var aspectRatioHeight = 0

    private fun TextureView.setAspectRatio(width: Int, height: Int) {
        val viewWidth = width
        val viewHeight = height

        if (viewWidth > 0 && viewHeight > 0 && aspectRatioWidth > 0 && aspectRatioHeight > 0) {
            val aspectRatioView = aspectRatioWidth.toFloat() / aspectRatioHeight.toFloat()
            val aspectRatioTexture = viewWidth.toFloat() / viewHeight.toFloat()

            val newWidth: Int
            val newHeight: Int
            if (aspectRatioView > aspectRatioTexture) {
                newWidth = viewWidth
                newHeight = (viewWidth / aspectRatioView).toInt()
            } else {
                newWidth = (viewHeight * aspectRatioView).toInt()
                newHeight = viewHeight
            }

            val xoff = (viewWidth - newWidth) / 2
            val yoff = (viewHeight - newHeight) / 2
            val txform = Matrix()
            getTransform(txform)
            txform.setScale(newWidth.toFloat() / viewWidth, newHeight.toFloat() / viewHeight)
            txform.postTranslate(xoff.toFloat(), yoff.toFloat())
            setTransform(txform)
        }
    }

    @SuppressLint("MissingPermission")
    private fun open_camera() {
        for (cameraId in cameraManager.cameraIdList) {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
            val facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                cameraManager.openCamera(
                    cameraId,
                    object : CameraDevice.StateCallback() {
                        override fun onOpened(pO: CameraDevice) {
                            cameraDevice = pO
                            capReq =
                                cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                            var surface = Surface(textureView.surfaceTexture)
                            capReq.addTarget(surface)

                            val streamConfigurationMap =
                                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                                    ?: return
                            val sizes = streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)
                            val previewSize = chooseOptimalSize(sizes, textureView.width, textureView.height)
                            textureView.setAspectRatio(previewSize.width, previewSize.height)

                            cameraDevice.createCaptureSession(
                                listOf(surface),
                                object : CameraCaptureSession.StateCallback() {
                                    override fun onConfigured(pO: CameraCaptureSession) {
                                        cameraCaptureSession = pO
                                        cameraCaptureSession.setRepeatingRequest(
                                            capReq.build(),
                                            null,
                                            null
                                        )
                                    }

                                    override fun onConfigureFailed(pO: CameraCaptureSession) {

                                    }
                                },
                                handler
                            )
                            val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
                            setAspectRatioTextureView(cameraCharacteristics)
                        }

                        override fun onDisconnected(pO: CameraDevice) {

                        }

                        override fun onError(pO: CameraDevice, p1: Int) {

                        }
                    },
                    handler
                )
                return
            }
        }
    }


    private fun get_permissions() {
        val permissionsList = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.CAMERA)
        }

        if (permissionsList.isNotEmpty()) {
            requestPermissions(permissionsList.toTypedArray(), 101)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Если разрешение на камеру получено, отображаем TextureView и открываем камеру
            defaultImageView.visibility = View.GONE
            open_camera()
        } else {
            // Если разрешение на камеру не получено, оставляем ImageView
            textureView.visibility=View.GONE
            defaultImageView.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(R.drawable.pacifier)
            .into(binding.gifImageView);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val characteristics = cameraCharacteristics
        characteristics?.let { setAspectRatioTextureView(it) }
    }

}

private const val MINIMUM_PREVIEW_WIDTH = 320
private const val MINIMUM_PREVIEW_HEIGHT = 240

private class CompareSizesByArea : Comparator<Size> {
    override fun compare(lhs: Size, rhs: Size): Int {
        return java.lang.Long.signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
    }
}

private fun chooseOptimalSize(choices: Array<Size>, textureViewWidth: Int, textureViewHeight: Int): Size {
    val minSize = Math.max(Math.min(textureViewWidth, textureViewHeight), MINIMUM_PREVIEW_WIDTH)
    val aspectRatio = textureViewWidth.toFloat() / textureViewHeight.toFloat()

    var optimalSize: Size? = null
    var minDiff = Integer.MAX_VALUE
    for (size in choices) {
        val area = size.width * size.height
        val diff = Math.abs(area - minSize)
        if (diff < minDiff) {
            optimalSize = size
            minDiff = diff
        }
    }

    return optimalSize ?: choices[0]
}