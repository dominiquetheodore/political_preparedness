package com.example.android.politicalpreparedness.election.adapter

import android.view.ViewGroup
import com.example.android.politicalpreparedness.databinding.FragmentElectionDetailBinding
import com.example.android.politicalpreparedness.network.models.Election
import androidx.recyclerview.widget.ListAdapter

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position) as Election
        holder.bind(election, clickListener)
    }

    //TODO: Add companion object to inflate ViewHolder (from)

}

//TODO: Create ElectionViewHolder
class ElectionViewHolder(private var binding: ListItemElectionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Election, clickListener: ElectionListener) {
        binding.election = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ElectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentElectionDetailBinding.inflate(layoutInflater, parent, false)
            return ElectionViewHolder(binding)
        }
    }

}

//TODO: Create ElectionDiffCallback
class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }
}

//TODO: Create ElectionListener
class ElectionListener(val clickListener: (election: Election) -> Unit) {
    fun onClick(election: Election) = clickListener(election)
}