package uz.gita.rounded_internship.presentation.screen

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.rounded_internship.R
import uz.gita.rounded_internship.data.local.room.StateEntity
import uz.gita.rounded_internship.data.model.Type
import uz.gita.rounded_internship.databinding.ScreenHomeBinding
import uz.gita.rounded_internship.presentation.dialog.InitialDialog
import uz.gita.rounded_internship.presentation.viewModel.HomeViewModel
import uz.gita.rounded_internship.presentation.viewModel.impl.HomeViewModelImpl
import uz.gita.rounded_internship.utils.VIDEO_URL
import java.io.File

@AndroidEntryPoint
class HomeScreen : Fragment(R.layout.screen_home) {
    private val viewModel: HomeViewModel by viewModels<HomeViewModelImpl>()
    private val viewBinding: ScreenHomeBinding by viewBinding(ScreenHomeBinding::bind)
    private lateinit var initialDialog: InitialDialog

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var isPlaying = false

        viewModel.stateEntities.onEach { states ->
            states.onEach { state ->
                when (state.id) {
                    1 -> {
                        if (state.state) {
                            viewBinding.apply { makeViewVisible(overlayG, ivCrownG) }
                        } else {
                            viewBinding.apply { makeViewGone(overlayG, ivCrownG) }
                        }
                    }
                    2 -> {
                        if (state.state) {
                            viewBinding.apply { makeViewVisible(overlayV, ivCrownV) }
                        } else {
                            viewBinding.apply { makeViewGone(overlayV, ivCrownV) }
                        }
                    }
                    3 -> {
                        if (state.state) {
                            viewBinding.apply { makeViewVisible(overlayS, ivCrownS) }
                        } else {
                            viewBinding.apply { makeViewGone(overlayS, ivCrownS) }
                        }
                    }
                    4 -> {
                        if (state.state) {
                            viewBinding.apply { makeViewVisible(overlayL, ivCrownL) }
                        } else {
                            viewBinding.apply { makeViewGone(overlayL, ivCrownL) }
                        }
                    }
                    5 -> {
                        if (state.state) {
                            viewBinding.apply { makeViewVisible(overlayH, ivCrownH) }
                        } else {
                            viewBinding.apply { makeViewGone(overlayH, ivCrownH) }
                        }
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        viewModel.duration.onEach { duration ->
            if (duration == 5000L) {
                viewModel.type.onEach { type ->
                    viewModel.refreshTime()
                    when (type) {
                        Type.GRAMMAR -> {
                            viewModel.update(StateEntity(1, false))
                        }
                        Type.VOCABULARY -> {
                            viewModel.update(StateEntity(2, false))
                        }
                        Type.SPEAKING -> {
                            viewModel.update(StateEntity(3, false))
                        }
                        Type.LISTENING -> {
                            viewModel.update(StateEntity(4, false))
                        }
                        Type.HOMEWORK -> {
                            viewModel.update(StateEntity(5, false))
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.clickCount.onEach { count ->
            if (count == 3) {
                viewModel.type.onEach { type ->
                    viewModel.refreshShortTime()
                    when (type) {
                        Type.GRAMMAR -> {
                            viewModel.update(StateEntity(1, true))
                        }
                        Type.VOCABULARY -> {
                            viewModel.update(StateEntity(2, true))
                        }
                        Type.SPEAKING -> {
                            viewModel.update(StateEntity(3, true))
                        }
                        Type.LISTENING -> {
                            viewModel.update(StateEntity(4, true))
                        }
                        Type.HOMEWORK -> {
                            viewModel.update(StateEntity(5, true))
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        //play video
        viewBinding.videoView2.setVideoURI(Uri.parse(VIDEO_URL))

        viewBinding.ivPlay.setOnClickListener {
            viewBinding.videoView2.isPlaying
            if (isPlaying) {
                viewBinding.ivPlay.setImageResource(R.drawable.play1)
                viewBinding.videoView2.pause()
                isPlaying = !isPlaying
            } else {
                viewBinding.ivPlay.setImageResource(R.drawable.pause)
                viewBinding.videoView2.start()
                isPlaying = !isPlaying
            }
        }
        viewBinding.linearDownload.setOnClickListener { downloadFromUrl() }

        //dialog
        viewModel.imgUrl.onEach {
            initialDialog = InitialDialog(requireContext(), it)
            initialDialog.show()
            initialDialog.setStopListener {
                viewModel.setDialogState(true)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //type unLock & lock
        viewBinding.overlayV.setOnTouchListener { _, event ->
            viewModel.unlock(event)
            viewModel.setType(Type.VOCABULARY)
            true
        }
        viewBinding.vocabulary.setOnTouchListener { _, event ->
            viewModel.lock(event)
            viewModel.setType(Type.VOCABULARY)
            true
        }

        viewBinding.overlayG.setOnTouchListener { _, event ->
            viewModel.unlock(event)
            viewModel.setType(Type.GRAMMAR)
            true
        }
        viewBinding.grammar.setOnTouchListener { _, event ->
            viewModel.lock(event)
            viewModel.setType(Type.GRAMMAR)
            true
        }

        viewBinding.overlayS.setOnTouchListener { _, event ->
            viewModel.unlock(event)
            viewModel.setType(Type.SPEAKING)
            true
        }
        viewBinding.speaking.setOnTouchListener { _, event ->
            viewModel.lock(event)
            viewModel.setType(Type.SPEAKING)
            true
        }

        viewBinding.overlayL.setOnTouchListener { _, event ->
            viewModel.unlock(event)
            viewModel.setType(Type.LISTENING)
            true
        }
        viewBinding.listening.setOnTouchListener { _, event ->
            viewModel.lock(event)
            viewModel.setType(Type.LISTENING)
            true
        }

        viewBinding.overlayH.setOnTouchListener { _, event ->
            viewModel.unlock(event)
            viewModel.setType(Type.HOMEWORK)
            true
        }
        viewBinding.homework.setOnTouchListener { _, event ->
            viewModel.lock(event)
            viewModel.setType(Type.HOMEWORK)
            true
        }
    }
    //to follow dry
    private fun makeViewGone(overlay: View, crown: ImageView) {
        overlay.visibility = View.GONE
        crown.visibility = View.GONE
    }

    private fun makeViewVisible(overlay: View, crown: ImageView) {
        overlay.visibility = View.VISIBLE
        crown.visibility = View.VISIBLE
    }

    //video downloader
    @RequiresApi(Build.VERSION_CODES.R)
    private fun downloadFromUrl() {
        try {
            val request = DownloadManager.Request(Uri.parse(VIDEO_URL))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle("newVideo.mp4")
            request.setDescription("Downloading Your File")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                System.currentTimeMillis().toString()
            )
            val downloadManager =
                requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        } catch (exception: Exception) {
            Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}




