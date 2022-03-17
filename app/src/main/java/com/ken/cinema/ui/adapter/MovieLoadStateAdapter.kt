package com.ken.cinema.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ken.cinema.databinding.LoadStateViewBinding

/**
 * Adapter for displaying a RecyclerView item based on LoadState, such as a loading spinner, or a retry error button.
 */
class MovieLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<MovieLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(val binding: LoadStateViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        val progress = holder.binding.loadStateProgress
        val textErrorMessage = holder.binding.loadStateErrorMessage
        val btnRetry = holder.binding.loadStateRetry

        progress.isVisible = loadState is LoadState.Loading
        textErrorMessage.isVisible = loadState is LoadState.Error
        btnRetry.isVisible = loadState is LoadState.Error

        if (loadState is LoadState.Error) {
            textErrorMessage.text = loadState.error.localizedMessage
        }
        btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LoadStateViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}