package com.example.capstone.ui.bottomnavigation.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.fabMain.imageTintList = null

        binding.btnDetect.imageTintList = null

        binding.fabMain.setOnClickListener {
            showAnnouncementDialog()
        }

        binding.btnDetect.setOnClickListener { startCameraX() }
        return binding.root

    }

    private fun startCameraX() {

    }

    private fun showAnnouncementDialog() {
        val dialog = AnnouncementDialogFragment.newInstance()
        dialog.show(parentFragmentManager, AnnouncementDialogFragment.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}