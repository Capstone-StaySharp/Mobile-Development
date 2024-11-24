package com.example.capstone.ui.bottomnavigation.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private lateinit var viewModel: NewsViewModel
    private lateinit var eventAdapter: ListEventAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        eventAdapter = ListEventAdapter{ event->
            val action = NewsFragmentDirections.actionNavigationNewsToEventDetailNewsFragment(event)
            findNavController().navigate(action)
        }
        binding.rvNews.layoutManager = LinearLayoutManager(context)
        binding.rvNews.adapter = eventAdapter

        viewModel.events.observe(viewLifecycleOwner){events->
            eventAdapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
            binding.progressBar.visibility = if (isLoading)View.VISIBLE else View.GONE
        }

        viewModel.displayEvents()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}