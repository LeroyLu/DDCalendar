package com.leroylu.calendar.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.leroylu.calendar.R
import com.leroylu.calendar.databinding.FragmentFallowAddBinding
import com.leroylu.calendar.model.FallowingAddViewModel
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.ui.BaseFragment
import com.leroylu.struct.util.FileOperator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
class FallowingAddFragment : BaseFragment<FragmentFallowAddBinding>() {

    private lateinit var mode: Mode

    enum class Mode {
        Add, Modify
    }

    private lateinit var viewModel: FallowingAddViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_fallow_add
    }

    override fun initViewModel() {
        viewModel = getViewModel(FallowingAddViewModel::class.java)
    }

    override fun bindingData(binding: FragmentFallowAddBinding) {
        binding.vm = viewModel
        binding.operation = Operation()
    }

    override fun init() {
        mode = (arguments?.getSerializable("mode") ?: Mode.Add) as Mode

        if (Mode.Modify == mode) {
            val vtuber = Gson().fromJson(arguments?.getString("data"), Vtuber::class.java)

            viewModel.id.postValue(vtuber.vid)
            viewModel.name.postValue(vtuber.name)
            viewModel.icon.postValue(vtuber.icon)
            viewModel.description.postValue(vtuber.description)
            viewModel.stream.postValue(vtuber.streamRoomId)

            Glide.with(requireContext())
                .load(vtuber.icon)
                .transform(CircleCrop())
                .into(getBinding().icon)

            getBinding().commit.text = "保存"
        }

    }

    inner class Operation {

        fun setFallowingIcon() {
            val albumIntent = Intent(Intent.ACTION_PICK)
            albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

            getBaseActivity()?.activityHelper?.startActivityWithResult(
                albumIntent,
                1
            ) { data: Intent?, resultCode: Int ->
                if (RESULT_OK == resultCode && data != null) {
                    val uri = data.data

                    Glide.with(requireContext())
                        .asBitmap()
                        .load(uri)
                        .transform(CircleCrop())
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                getBinding().icon.setImageBitmap(resource)
                                GlobalScope.launch {
                                    val file = FileOperator.saveUserIcon(requireContext(), resource)
                                    viewModel.icon.postValue(file.absolutePath)
                                }
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })
                }
            }
        }

        fun fallow() {
            viewModel.addOrUpdateFallowing {
                findNavController().popBackStack()
            }
        }
    }

}