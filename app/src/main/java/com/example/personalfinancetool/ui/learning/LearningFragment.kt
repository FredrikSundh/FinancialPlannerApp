package com.example.personalfinancetool.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalfinancetool.adapter.learningViewAdapter
import com.example.personalfinancetool.databinding.FragmentLearningBinding
import com.example.personalfinancetool.model.videoItem

class LearningFragment : Fragment() {

    private var _binding: FragmentLearningBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentLearningBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val youtubeUrlList = listOf<String>(
            "https://www.youtube.com/embed/jleOJdjVZJs", // Starting budgeting and traps
            "https://www.youtube.com/embed/watch?v=Rm6UdfRs3gw&list=PL83DF21B47327EDFE&index=10", // Basic idea of compounding interest
            "https://www.youtube.com/embed/watch?v=98qfFzqDKR8&list=PL83DF21B47327EDFE", // what is stocks
            "https://www.youtube.com/embed/watch?v=mec-QpjQMXY&list=PL83DF21B47327EDFE&index=4", // Thinking about compounding
            "https://www.youtube.com/embed/watch?v=IDHJCEKeHLc&list=PL83DF21B47327EDFE&index=9", // Mortgage Loans

            "https://www.youtube.com/embed/watch?v=Qh-M3_L4xYk&list=PL83DF21B47327EDFE&index=11", // Intro to stocks and bonds
            "https://www.youtube.com/embed/watch?v=733mgqrzNKs&list=PL83DF21B47327EDFE&index=17", // Value of money over time
            "https://www.youtube.com/embed/watch?v=-Z5kkfrEc8I&list=PL83DF21B47327EDFE&index=19", // Talking about inflation
        )

        val youtubeVideoTitle = listOf<String>(
            "Creating a Budget",
            "Investing and Compounding interest",
            "What is a stock and why own one?",
            "reasoning about compounding",
            "Mortgages and home ownership",
            "talking about stocks and bonds",
            "how money creates value over time",
            "Inflation"
        )

        val videoList = mutableListOf<videoItem>()

        for(i in 0..youtubeUrlList.size-1) {
            val title = youtubeVideoTitle.get(i)
            val url = youtubeUrlList.get(i)
            videoList.add(videoItem(title,url))
        }

        val myAdapter = learningViewAdapter(videoList)
        binding.learningRv.adapter = myAdapter

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.learningRv.layoutManager = layoutManager

        binding.rvLeftButton.setOnClickListener{
            val layoutManager = binding.learningRv.layoutManager as LinearLayoutManager
            val previousItemPosition = layoutManager.findFirstVisibleItemPosition() -1
            if (previousItemPosition >=0) {
                binding.learningRv.smoothScrollToPosition(previousItemPosition)
            }
        }
        binding.rvRightButton.setOnClickListener{
            val layoutManager = binding.learningRv.layoutManager as LinearLayoutManager
            val nextItemPosition = layoutManager.findFirstVisibleItemPosition() + 1
            if(nextItemPosition <= 7) {
                binding.learningRv.smoothScrollToPosition(nextItemPosition)
            }

        }















        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}