package com.example.foodnote.ui.antother_fragment

import android.animation.ObjectAnimator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Property
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ViewSwitcher
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.foodnote.R
import com.example.foodnote.databinding.ProfileFragmentBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.settings_fragment.SettingsFragment
import com.example.foodnote.utils.hide
import com.example.foodnote.utils.show
import java.io.File


class ProfileFragment : BaseViewBindingFragment<ProfileFragmentBinding>(ProfileFragmentBinding::inflate) , ViewSwitcher.ViewFactory {

    companion object {
        const val DAY = "day"
        const val NIGHT = "night"
        const val STACK = "STACK_NEW"
        const val DURATION_ANIMATION_ALPHA = 1000L
        const val DURATION_ANIMATION_AVATAR = 1200L
    }

    private val fragment = SettingsFragment()
    private var theme = DAY
    private var saveH = 0
    private var flagBlockBekArrow = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImageSwitcher()
        loadTheme()
        chekSwitch()
        editProfile()
        backArrow()
        editAvatar()

        Handler(Looper.myLooper()!!).postDelayed({
            animation(binding.root)
            animationExpanded(binding.viewSun)
        }, 0)
    }

    private fun editAvatar() {
        binding.cardAvatar.setOnClickListener {
            if (flagBlockBekArrow) {
                val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, 1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> { if (resultCode == RESULT_OK) {
                val chosenImageUri: Uri = (data?.data ?: "") as Uri

                try {
                    val wholeID = DocumentsContract.getDocumentId(chosenImageUri)
                    val id = wholeID.split(":").toTypedArray()[1]

                    val column = arrayOf(MediaStore.Images.Media.DATA)

                    val sel = MediaStore.Images.Media._ID + "=?"

                    val cursor = requireActivity().contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, arrayOf(id), null
                    )

                    val filePath: String

                    if (cursor != null) {
                        val columnIndex: Int = cursor.getColumnIndex(column[0])

                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex)
                            loadPhotoAvatar(filePath)
                        }
                        cursor.close()
                    }
                } catch (e : Exception) {}
            } }
        }
    }

    private fun loadPhotoAvatar(filePath : String) {
        val bitmap = BitmapFactory.decodeFile(filePath)
        binding.photoProfile.setImageBitmap( bitmap )
    }

    private fun backArrow() = with(binding) {
        binding.back.hide()
        flagBlockBekArrow = false
        binding.back.setOnClickListener {

            if(flagBlockBekArrow) {
                animation(binding.root)

                childFragmentManager.popBackStack()

                objectAnimation(convertDpToPixels(12).toFloat(), View.X)
                animationExpandedAvatar(cardAvatar, 0.5f)
                animationExpandedEditProfile(materialCardView2, saveH)
                flagBlockBekArrow = false
            }
        }
    }

    private fun editProfile() = with(binding) {
        editProfile.setOnClickListener {
            saveH = binding.materialCardView2.height

            animation(root)
            animationExpandedEditProfile(materialCardView2,((root.height - materialCardView2.y - convertDpToPixels(12))).toInt())

            showHideView(View.GONE)
            binding.back.show()

            animationExpandedAvatar(cardAvatar,2f)
            objectAnimation(materialCardView2.width/2f - cardAvatar.width, View.X)
        }
    }

    private fun showHideView(invisible: Int) = with(binding) {
        editProfile.visibility = invisible
        editText.visibility = invisible
        editText2.visibility = invisible
        textYouProfile.visibility = invisible
        editImage.visibility = invisible
    }

    private fun setFragmentConstructor() {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.containerEditProfile, fragment)
            .addToBackStack(STACK)
            .commit()
    }

    private fun objectAnimation(value: Float, anim: Property<View, Float>) {
        ObjectAnimator.ofFloat(binding.cardAvatar, anim, value).apply {
            duration = DURATION_ANIMATION_AVATAR
            interpolator = AnticipateOvershootInterpolator(0f)
            start()
        }.doOnEnd {
            if (value == convertDpToPixels(12).toFloat()) {
                binding.back.hide()
                showHideView(View.VISIBLE)
            } else {
                setFragmentConstructor()
                flagBlockBekArrow = true
            }
        }
    }

    private fun initImageSwitcher() {
        val inAnimation: Animation = AlphaAnimation(0f, 1f)
        inAnimation.duration = DURATION_ANIMATION_ALPHA
        val outAnimation: Animation = AlphaAnimation(1f, 0f)
        outAnimation.duration = DURATION_ANIMATION_ALPHA

        binding.mImageSwitcher.setFactory(this)
        binding.mImageSwitcher.inAnimation = inAnimation
        binding.mImageSwitcher.outAnimation = outAnimation
    }

    private fun loadTheme() {
        if(theme == DAY) {
            loadImage(R.drawable.sun)
            binding.mImageSwitcher.setImageResource(R.drawable.day_image_low)
        } else {
            loadImage(R.drawable.moon)
            binding.mImageSwitcher.setImageResource(R.drawable.night_image_low)
        }
    }

    private fun loadImage(drawable: Int) { Glide.with(this).load(drawable).transition(DrawableTransitionOptions.withCrossFade()).transform(CircleCrop()).into(binding.imageViewSun) }

    private fun convertDpToPixels(dp: Int) = (dp * requireContext().resources.displayMetrics.density).toInt()

    private fun chekSwitch() {
        binding.switchTheme.setOnClickListener {
            animation(binding.root)
            animationClose(binding.viewSun)

            Handler(Looper.myLooper()!!).postDelayed({
                theme = if(theme == DAY) { NIGHT } else { DAY }
                loadTheme()

                animation(binding.root)
                animationExpanded(binding.viewSun)
            }, DURATION_ANIMATION_AVATAR)
        }
    }

    private fun animation(root: ConstraintLayout, timeAnimation: Long = DURATION_ANIMATION_AVATAR, startDelay: Long = 0) {
        val transition = ChangeBounds()
        transition.duration     = timeAnimation
        transition.interpolator = AnticipateOvershootInterpolator(1f,0f)
        transition.startDelay   = startDelay

        TransitionManager.beginDelayedTransition(root, transition)
    }

    private fun animationExpanded(view: View) {
        view.layoutParams = view.layoutParams.apply {
            height = convertDpToPixels(220)
        }
        if(theme == NIGHT) view.translationX = -60f
        if(theme == DAY) view.translationX = 0f
    }

    private fun animationClose(view: View) {
        view.layoutParams = view.layoutParams.apply { height = convertDpToPixels(80) }
    }

    private fun animationExpandedEditProfile(view: View, newHeight : Int) {
        view.layoutParams = view.layoutParams.apply { height = newHeight }
    }

    private fun animationExpandedAvatar(view: View, scale : Float) {
        view.layoutParams = view.layoutParams.apply {
            height = (view.height * scale).toInt()
            width = (view.width * scale).toInt()
        }
    }

    override fun makeView(): View {
        val imageView = ImageView(context)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )

        imageView.setBackgroundColor(-0x1000000)
        return imageView
    }

}


