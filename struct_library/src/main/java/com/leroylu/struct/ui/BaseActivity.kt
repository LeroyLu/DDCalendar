package com.leroylu.struct.ui

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.leroylu.struct.BaseApplication
import com.leroylu.struct.util.*


/**
 * @author jiaj.lu
 * @date 2020/8/17
 * @description
 */
abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity() {

    val permissionHelper = PermissionHelper()
    val activityHelper = ActivityHelper()

    private lateinit var binding: Binding
    private lateinit var loadingDialog: LoadingDialog
    private var mFactory: ViewModelProvider.Factory? = null

    fun getLoadingDialog() = loadingDialog

    protected fun getBinding() = binding

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityManager.addActivity(this)

        permissionHelper.init(this)
        activityHelper.init(this)

        super.onCreate(savedInstanceState)
        initLoadingDialog()
        initViewModel()
        initDataBinding()
        initView()
        if (setTranslucentStatus()) {
            StatusBarHelper.setTranslucentStatus(this)
        }
        initObserver()
        init()
    }

    open fun setTranslucentStatus() = true

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.removeActivity(this)
    }

    protected fun initToolbar(id: Int) {
        val toolbar = binding.root.findViewById<Toolbar>(id)
        toolbar.elevation = 1f
        setSupportActionBar(toolbar)
    }

    override fun dispatchTouchEvent(me: MotionEvent): Boolean {
        KeyboardHelper.checkAndHideKeyboard(this, me)
        return super.dispatchTouchEvent(me)
    }

    private fun initLoadingDialog() {
        loadingDialog = LoadingDialog(this)
        lifecycle.addObserver(loadingDialog)
    }

    abstract fun setBindingConfig(): BindingConfig

    private fun initDataBinding() {
        val config = setBindingConfig()
        binding = DataBindingUtil.setContentView(this, config.layoutId)
        binding.lifecycleOwner = this
        config.getParams().entries.forEach {
            binding.setVariable(it.key, it.value)
        }
    }

    protected open fun <T : ViewModel?> getActivityViewModel(modelClass: Class<T>): T {
        return ViewModelProviders.of(this).get(modelClass)
    }

    protected open fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(application as BaseApplication, getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        val application: Application = application
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory!!
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityHelper.dispatchActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    protected open fun initView() {

    }

    protected open fun initViewModel() {

    }

    protected open fun initObserver() {

    }

    protected open fun init() {

    }

}