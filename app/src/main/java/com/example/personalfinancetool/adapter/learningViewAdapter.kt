package com.example.personalfinancetool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancetool.databinding.ListItemLayoutBinding
import com.example.personalfinancetool.databinding.VideoItemLayoutBinding
import com.example.personalfinancetool.model.IncomeExpenseItem
import com.example.personalfinancetool.model.videoItem
import kotlinx.coroutines.NonDisposableHandle.parent

class learningViewAdapter(val listItems : MutableList<videoItem>) : RecyclerView.Adapter<learningViewAdapter.ViewHolder>() {
    val adapterData = listItems


    public class ViewHolder(private var binding: VideoItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bidnings(video : videoItem) {
            binding.videoTitle.text = video.title
            binding.video.settings.mediaPlaybackRequiresUserGesture = false
            binding.video.settings.domStorageEnabled = true
            binding.video.webViewClient = WebViewClient()
            binding.video.settings.javaScriptEnabled = true
            binding.video.webChromeClient = WebChromeClient()
            binding.video.loadUrl(video.url)


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): learningViewAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VideoItemLayoutBinding.inflate(layoutInflater, parent, false)
        return learningViewAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return adapterData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bidnings(adapterData.get(position)) // Gets the corresponding income expense Item from the list
    }
}