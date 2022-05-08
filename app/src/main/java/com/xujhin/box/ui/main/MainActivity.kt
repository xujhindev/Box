package com.xujhin.box.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseBinderAdapter
import com.xujhin.box.R
import com.xujhin.box.databinding.ActivityMainBinding
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.ui.dialog.InputDialog
import com.xujhin.box.ui.dialog.InputType
import com.xujhin.box.ui.main.item.ActionListener
import com.xujhin.box.ui.main.item.ItemCell
import java.util.Objects

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var dataAdapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
            showAddDialog(InputType.Repository)
        }
        binding.btnAddBox.setOnClickListener {
            showAddDialog(InputType.Box)
        }

        dataAdapter = GroupAdapter(this)
        dataAdapter.listener = object : ActionListener {
            override fun delete(data: BaseEntity, pos: Int) {

            }

            override fun modify(data: BaseEntity, pos: Int) {

            }

        }

        binding.rv.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        binding.stickyLayout.isSticky = true
    }

    private fun showAddDialog(repository: InputType) {
        val inputDialog = InputDialog()
        inputDialog.inputType = repository
        inputDialog.show(supportFragmentManager, "type")
    }
}

