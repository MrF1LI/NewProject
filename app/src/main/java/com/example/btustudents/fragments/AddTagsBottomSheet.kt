package com.example.btustudents.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.btustudents.AddPostActivity
import com.example.btustudents.R
import com.example.btustudents.databinding.BottomSheetAddTagsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class AddTagsBottomSheet: BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: BottomSheetAddTagsBinding? = null
    private val binding get() = _binding!!

    lateinit var dataPasser: OnDataPass

    private lateinit var arrayListTags: ArrayList<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddTagsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        arrayListTags = arrayListOf()

        binding.buttonSaveTags.setOnClickListener {
            dataPasser.onDataPass(arrayListTags)
            dismiss()
        }

        binding.chip.setOnClickListener(this)
        binding.chip1.setOnClickListener(this)
        binding.chip2.setOnClickListener(this)
        binding.chip3.setOnClickListener(this)
        binding.chip4.setOnClickListener(this)
        binding.chip5.setOnClickListener(this)
        binding.chip6.setOnClickListener(this)
        binding.chip7.setOnClickListener(this)
        binding.chip8.setOnClickListener(this)
        binding.chip9.setOnClickListener(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(chip: View?) {
        if (chip is Chip) {
            if (chip.isChecked) {
                arrayListTags.add(chip.text.toString())
                chip.isChecked = true
            } else {
                arrayListTags.remove(chip.text.toString())
                chip.isChecked = false
            }
        }
        Log.e("SHOW", arrayListTags.toString())
    }

}

interface OnDataPass {
    fun onDataPass(data: ArrayList<String>)
}