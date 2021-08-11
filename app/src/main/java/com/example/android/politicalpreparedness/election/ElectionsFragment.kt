package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel
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

        // binding.representativeButton.setOnClickListener { navToRepresentatives() }
        // binding.upcomingButton.setOnClickListener { navToElections() }

        //TODO: Add ViewModel values and create ViewModel


        //TODO: Add binding values
        binding.viewModel = viewModel

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters
        // val clickListener = {election : Election -> Unit}

        // binding.upcomingElectionsRecycler.adapter = upcomingElectionsAdapter

        //TODO: Populate recycler adapters
        /*
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                upcomingElectionsAdapter.submitList(it)
            }
        })*/




        // Create the observer which updates the UI.
        val nameObserver = Observer<String> { newName ->
            // Update the UI, in this case, a TextView.
            binding.nameTextView.text = newName
        }

        binding.button.setOnClickListener {
            val anotherName = "John Doe"
            Log.i("electionsFragment", "last name " + viewModel.currentName.value)
            viewModel.currentName.setValue(anotherName)
        }

        /*
        binding.electionsView.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = ElectionListAdapter()
        }*/

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.currentName.observe(viewLifecycleOwner, nameObserver)

        Log.i("ElectionsFragment", "inside Elections Fragment")

        //TODO: Populate recycler adapters

        return binding.root

    }

    //TODO: Refresh adapters when fragment loads

}