package com.xujhin.box.ui.login

import com.xujhin.box.utils.sp.SpHelper
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.gyf.immersionbar.ImmersionBar
import com.xujhin.box.databinding.ActivityLoginBinding
import com.xujhin.box.ext.navigateTo
import com.xujhin.box.ext.showToast
import com.xujhin.box.ui.main.MainActivity
import com.xujhin.box.utils.sp.SpKey

class LoginActivity : AppCompatActivity() {
    companion object {
        const val DEFAULT_ACCOUNT = "account"
        const val DEFAULT_PWD = "password"
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .fitsSystemWindows(true)
            .init()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewAndEvent()
    }

    private fun initViewAndEvent() {

        if (!SpHelper.getString(SpKey.ACCOUNT).isNullOrEmpty()) {
            binding.edtAccount.setText(SpHelper.getString(SpKey.ACCOUNT))
        }

//        binding.edtPwd.setOnEditorActionListener { v, actionId, event ->
//            return@setOnEditorActionListener executeLogin()
//        }
        binding.btnLogin.setOnClickListener {
            if (executeLogin()) {
                return@setOnClickListener
            }
        }
    }

    private fun executeLogin(): Boolean {
        if (!validateInfo()) {
            return true
        }
        SpHelper.save(SpKey.ACCOUNT, binding.edtAccount.text.toString().trim())
        navigateTo<MainActivity>()
        showToast("登录成功")
        this.finish()
        return false
    }

    private fun validateInfo(): Boolean {
        if (binding.edtAccount.text.isNullOrEmpty()) {
            showToast("请输入账号")
            return false
        }
        if (binding.edtPwd.text.isNullOrEmpty()) {
            showToast("请输入密码")
            return false
        }
        if (!binding.edtAccount.contentEqual(DEFAULT_ACCOUNT) || !binding.edtPwd.contentEqual(DEFAULT_PWD)) {
            showToast("账号或密码错误")
            return false
        }
        return true
    }
}

fun EditText.contentEqual(string: String): Boolean {
    if (this.text.toString().trim() == string) {
        return true
    }
    return false
}