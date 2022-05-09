package com.xujhin.box.ui.result

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xujhin.box.R
import com.xujhin.box.databinding.ActivityResultBinding
import com.xujhin.box.opengl.view.MyGLSurfaceView

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    private val glSurfaceView: MyGLSurfaceView by lazy {
        MyGLSurfaceView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        glSurfaceView.apply {
            renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY;
        }
        binding.renderLayer.addView(glSurfaceView)
        binding.btnBack.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun finish() {
        super.finish()
        setupOutAnim()
    }

    private fun setupOutAnim() {
        overridePendingTransition(R.anim.on_activity_close_enter, R.anim.on_activity_close_exit);
    }


}
