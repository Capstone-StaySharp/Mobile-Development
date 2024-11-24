package com.example.capstone.ui.bottomnavigation.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.databinding.FragmentEventDetailNewsBinding

class EventDetailNewsFragment : Fragment() {

    private var _binding: FragmentEventDetailNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventDetailViewModel: EventDetailNewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailNewsBinding.inflate(inflater, container, false)

        // Setup EventDetailViewModel
        eventDetailViewModel = ViewModelProvider(this).get(EventDetailNewsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // untuk set tombol back
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        val event = EventDetailNewsFragmentArgs.fromBundle(requireArguments()).event
        if (event == null) {
            Log.e("EventDetailFragment", "Event ID is null")
        }

        binding.tvEventDetailName.text = event.title
        binding.tvEventTime.text = event.publishedat
        binding.tvEventDetailDesc.text = event.content
        binding.tvEventOwner.text = event.author
        binding.btnLinkPresent.setOnClickListener {
            val url = event.urltoimage
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        Glide.with(this).load(event.urltoimage).into(binding.imgEventDetail)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // untuk set tombol back
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }else->{
                super.onOptionsItemSelected(item)
            }
        }
    }
}