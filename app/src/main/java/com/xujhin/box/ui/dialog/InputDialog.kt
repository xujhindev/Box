package com.xujhin.box.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.xujhin.box.databinding.DialogInputBinding
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.ui.main.MainViewModel

class InputDialog : DialogFragment() {

    private var _binding: DialogInputBinding? = null
    private val binding: DialogInputBinding get() = _binding!!
    var inputType: InputType = InputType.Box

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndEvent()
    }

    private fun initViewAndEvent() {
        binding.btnAdd.setOnClickListener {
            addData()
        }
    }

    private fun addData() {
        val name = binding.edtName.text.toString().trim()
        val length = binding.edtLength.text.toString().trim()
        val width = binding.edtWidth.text.toString().trim()
        val height = binding.edtHeight.text.toString().trim()
        val weight = binding.edtWeight.text.toString().trim()
        when (inputType) {
            InputType.Repository -> {
                val data = BaseEntity.Repository(name, weight, height, width, length = length)
                viewModel.update(data)
            }
            InputType.Box -> {
                val data = BaseEntity.Box(name, weight, height, width, length = length)
                viewModel.update(data)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

enum class InputType {
    Repository, Box
}