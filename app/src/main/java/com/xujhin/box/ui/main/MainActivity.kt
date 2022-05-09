package com.xujhin.box.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.gyf.immersionbar.ImmersionBar
import com.xujhin.box.databinding.ActivityMainBinding
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.entity.GroupEntity
import com.xujhin.box.ext.navigateTo
import com.xujhin.box.ui.dialog.DialogType
import com.xujhin.box.ui.dialog.InputDialog
import com.xujhin.box.ui.dialog.InputType
import com.xujhin.box.ui.dialog.ShowDialog
import com.xujhin.box.ui.result.ResultActivity
import java.util.Base64

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var dataAdapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .fitsSystemWindows(true)
            .init()
        setContentView(binding.root)
        initViewAndEvent()
        initDataChange()
    }

    private fun initDataChange() {
        viewModel.resultLivedata.observe(this, Observer {
            dataAdapter.updateData(it)
            dataAdapter.notifyDataChanged()
        })
    }

    private fun initViewAndEvent() {
        binding.btnAddRepository.setOnClickListener {
            showAddDialog(DialogType.AddRepository)
        }
        binding.btnAddBox.setOnClickListener {
            showAddDialog(DialogType.AddBox)
        }

        dataAdapter = GroupAdapter(this)
        dataAdapter.listener = object : ActionListener {
            override fun delete(data: BaseEntity, pos: Int) {
                when (data) {
                    is BaseEntity.Repository -> {
                        viewModel.deleteRepository(data)
                    }
                    is BaseEntity.Box -> {
                        viewModel.deleteBox(data)
                    }
                    else -> {
                    }
                }

            }

            override fun modify(data: BaseEntity, pos: Int) {
                when (data) {
                    is BaseEntity.Repository -> {
                        showAddDialog(DialogType.ModifyRepository, data)
                    }
                    is BaseEntity.Box -> {
                        showAddDialog(DialogType.ModifyBox, null, box = data)
                    }
                    else -> {
                    }
                }
            }

            override fun deleteAll(groupEntity: GroupEntity) {
                when (groupEntity.inputType) {
                    InputType.Box -> {
                        viewModel.deleteAllBox()
                    }
                    InputType.Repository -> {
                        viewModel.deleteAllRepository()
                    }
                }
            }

            override fun clickItem(data: BaseEntity, adapterPosition: Int) {
                ShowDialog().apply {
                    if (data is BaseEntity.Box) {
                        this.modifyBox = data
                    } else if (data is BaseEntity.Repository) {
                        this.modifyRepository = data
                    }
                }.show(supportFragmentManager, "show")
            }
        }

        binding.rv.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        binding.stickyLayout.isSticky = true


        binding.btnProduct.setOnClickListener {
            navigateTo<ResultActivity>()
        }
    }

    private fun showAddDialog(
        dialogType: DialogType,
        repository: BaseEntity.Repository? = null,
        box: BaseEntity.Box? = null
    ) {
        val inputDialog = InputDialog()
        inputDialog.dialogType = dialogType
        inputDialog.modifyRepository = repository
        inputDialog.modifyBox = box
        inputDialog.show(supportFragmentManager, "type")
    }
}

