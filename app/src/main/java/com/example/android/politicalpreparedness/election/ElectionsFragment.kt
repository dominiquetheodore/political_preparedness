package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*

class ElectionsFragment: Fragment() {

    //DONE: Declare ViewModel
    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, ElectionsViewModelFactory(activity.application)).get(ElectionsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO: Add ViewModel values and create ViewModel


        //TODO: Add binding values
        binding.viewModel = viewModel

        //TODO: Link elections to voter info
        viewModel.navigateToElection.observe(viewLifecycleOwner, Observer { election ->
            election?.let {
                this.findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionFragmentToVoterInfoFragment(
                        it.id, it.division))
                viewModel.onElectionNavigated()
            }
        })

        //DONE: Initiate recycler adapters

        val clickListener = {election : Election -> viewModel.onElectionClicked(election)}
        val upcomingElectionsAdapter = ElectionListAdapter(ElectionListener(clickListener)) // { election -> adapterOnClick(election) }
        val savedElectionsAdapter = ElectionListAdapter(ElectionListener(clickListener))

        binding.upcomingElectionsRecycler.adapter = upcomingElectionsAdapter
        binding.savedElectionsRecycler.adapter = savedElectionsAdapter

        //DONE: Populate recycler adapters
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer<List<Election>> {
            it?.let {
                upcomingElectionsAdapter.submitList(it)
            }
        })

        viewModel.savedElections.observe(viewLifecycleOwner, Observer<List<Election>> {
            it?.let {
                savedElectionsAdapter.submitList(it)
            }
        })

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}