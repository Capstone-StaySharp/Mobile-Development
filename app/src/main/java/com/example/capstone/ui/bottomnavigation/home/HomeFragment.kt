package com.example.capstone.ui.bottomnavigation.home

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.capstone.R
import com.example.capstone.databinding.FragmentHomeBinding

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
        binding.fabMain.setOnClickListener { showAnnouncementDialog() }
        binding.btnDetect.setOnClickListener { startCamera() }

        return binding.root
    }

    private fun startCamera() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        startActivity(intent)
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

data class CapturedFace(val bitmap: Bitmap, val timestamp: Long)