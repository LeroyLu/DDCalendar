package com.leroylu.struct.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.leroylu.struct.BaseApplication
import com.leroylu.struct.util.LoadingDialog

/**
 * @author jiaj.lu
 * @date 2020/8/27
 * @description
 */
abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {

    private var mFactory: ViewModelProvider.Factory? = null

    private lateinit var binding: Binding

    protected fun getBinding() = binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initDataBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        bindingData(binding)
        initObserver()
        initView()
        init()
    }

    abstract fun getLayoutId(): Int

    private fun initDataBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    protected open fun <T : ViewModel?> getViewModel(modelClass: Class<T>): T {
        return ViewModelProviders.of(this).get(modelClass)
    }

    protected open fun <T : ViewModel?> getActivityViewModel(modelClass: Class<T>): T {
        return ViewModelProviders.of(requireActivity()).get(modelClass)
    }

    protected open fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(requireActivity().application as BaseApplication, getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        val application: Application = requireActivity().application
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory!!
    }

    protected fun getLoadingDialog(): LoadingDialog? {
        return if (requireActivity() is BaseActivity<*>)
            (requireActivity() as BaseActivity<*>).getLoadingDialog()
        else
            null
    }

    protected fun getBaseActivity(): BaseActivity<*>? {
        val activity = requireActivity()
        return if (activity is BaseActivity<*>) {
            activity
        } else {
            null
        }
    }

    protected open fun init() {

    }

    protected open fun initView() {

    }

    protected open fun bindingData(binding: Binding) {

    }

    protected open fun initViewModel() {

    }

    protected open fun initObserver() {

    }
}