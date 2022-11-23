package uz.gita.rounded_internship.presentation.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.bumptech.glide.Glide
import uz.gita.rounded_internship.R
import uz.gita.rounded_internship.databinding.DialogInitialBinding
import uz.gita.rounded_internship.utils.config

class InitialDialog(context: Context, private val url: String) : Dialog(context) {
    private lateinit var binding: DialogInitialBinding
    private var stopListener: (() -> Unit)? = null
    fun setStopListener(block: () -> Unit) {
        stopListener = block
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DialogInitialBinding.inflate(layoutInflater)
        config(binding)
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        Glide
            .with(binding.root)
            .load(url)
            .centerCrop()
            .placeholder(context.getDrawable(R.drawable.placeholder))
            .into(binding.shapeableImageView)

        binding.btnStop.setOnClickListener {
            stopListener?.invoke()
            dismiss()
        }
    }
}