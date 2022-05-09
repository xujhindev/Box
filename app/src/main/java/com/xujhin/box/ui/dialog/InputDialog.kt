package com.xujhin.box.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.xujhin.box.databinding.DialogInputBinding
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.ui.main.MainViewModel
import com.xujhin.box.utils.IdGenerator

class InputDialog : DialogFragment() {

    private var _binding: DialogInputBinding? = null
    private val binding: DialogInputBinding get() = _binding!!
    var dialogType: DialogType = DialogType.AddRepository

    var modifyRepository: BaseEntity.Repository? = null
    var modifyBox: BaseEntity.Box? = null
    private val viewModel: MainViewModel by activityViewModels()

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
        when (dialogType) {
            DialogType.ModifyBox -> {
                binding.tvTitle.text = "修改盒子"
                binding.btnAdd.setText("修改盒子")
            }
            DialogType.ModifyRepository -> {
                binding.tvTitle.text = "修改仓库"
                binding.btnAdd.setText("修改仓库")
            }
            DialogType.AddRepository -> {
                binding.tvTitle.text = "添加仓库"
                binding.btnAdd.text = "添加仓库"
            }
            DialogType.AddBox -> {
                binding.tvTitle.text = "添加盒子"
                binding.btnAdd.setText("添加盒子")
            }
        }
        modifyRepository?.let {
            binding.edtName.setText(it.name)
            binding.edtHeight.setText(it.height)
            binding.edtLength.setText(it.length)
            binding.edtWidth.setText(it.width)
            binding.edtWeight.setText(it.weight)
        }
        modifyBox?.let {
            binding.edtName.setText(it.name)
            binding.edtHeight.setText(it.height)
            binding.edtLength.setText(it.length)
            binding.edtWidth.setText(it.width)
            binding.edtWeight.setText(it.weight)
        }
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

        if (name.isNullOrEmpty()) {
            showToast("请输入正确的名字")
            return
        }

        if (length.toFloat() <= 0) {
            showToast("请输入正确的长度")
            return
        }

        if (width.toFloat() <= 0) {
            showToast("请输入正确的宽度")
            return
        }

        if (height.toFloat() <= 0) {
            showToast("请输入正确的高度")
            return
        }

        if (weight.toFloat() <= 0) {
            showToast("请输入正确的重量")
            return
        }
        when (dialogType) {
            DialogType.ModifyBox -> {
                modifyBox?.let {
                    it.name = name
                    it.length = length
                    it.height = height
                    it.width = width
                    it.weight = weight
                    viewModel.modifyBox(it)
                }

            }
            DialogType.ModifyRepository -> {
                modifyRepository?.let {
                    it.name = name
                    it.length = length
                    it.height = height
                    it.width = width
                    it.weight = weight
                    viewModel.modifyRepository(it)
                }
            }
            DialogType.AddRepository -> {
                val data =
                    BaseEntity.Repository(name = name, weight = weight, height = height, width = width, length = length)

                viewModel.update(data)
            }
            DialogType.AddBox -> {
                val data = BaseEntity.Box(name = name, weight = weight, height = height, width = width, length = length)
                viewModel.update(data)
            }
        }
        this.dismiss()
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
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

enum class DialogType {
    ModifyBox, ModifyRepository, AddRepository, AddBox
}

enum class InputType {
    Repository, Box
}