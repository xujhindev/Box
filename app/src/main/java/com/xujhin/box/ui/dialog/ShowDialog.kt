package com.xujhin.box.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.xujhin.box.databinding.DialogInputBinding
import com.xujhin.box.databinding.DialogShowBinding
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.ui.main.MainViewModel

class ShowDialog : DialogFragment() {

    private var _binding: DialogShowBinding? = null
    private val binding: DialogShowBinding get() = _binding!!

    var modifyRepository: BaseEntity.Repository? = null
    var modifyBox: BaseEntity.Box? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndEvent()
    }

    private fun initViewAndEvent() {

        modifyRepository?.let {
            binding.tvShow.setText(
                """
                      名称：${it.name}
                      长度：${it.length}(mm)
                      宽度：${it.width}(mm)
                      高度：${it.height}(mm)
                      重量：${it.weight}(kg)
                      
                """.trimIndent()
            )
        }
        modifyBox?.let {
            binding.tvShow.setText(
                """
                      名称：${it.name}
                      长度：${it.length}(mm)
                      宽度：${it.width}(mm)
                      高度：${it.height}(mm)
                      重量：${it.weight}(kg)
                      
                """.trimIndent()
            )
        }
    }


    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
