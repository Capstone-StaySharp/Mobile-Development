package com.example.capstone.ui.bottomnavigation.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.capstone.databinding.FragmentEventDetailNewsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class EventDetailNewsFragment : Fragment() {

    private var _binding: FragmentEventDetailNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailNewsBinding.inflate(inflater, container, false)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = EventDetailNewsFragmentArgs.fromBundle(requireArguments()).event

        val content = event.content?.replace("\\n", " ")

        val publishedAtDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(event.publishedat)
        val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(publishedAtDate)

        binding.tvEventDetailName.text = event.title
        binding.tvEventTime.text = formattedDate
        binding.tvEventDetailDesc.text = content
        binding.tvEventOwner.text = event.author
        Glide.with(this).load(event.urltoimage).into(binding.imgEventDetail)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}