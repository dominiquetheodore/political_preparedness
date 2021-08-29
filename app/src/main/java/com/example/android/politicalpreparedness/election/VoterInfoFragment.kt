package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import timber.log.Timber

class VoterInfoFragment : Fragment() {
    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.i("inside voter info fragment")
        //TODO: Add ViewModel values and create ViewModel


        //TODO: Add binding values
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */
        val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = bundle.argElectionId
        val division = bundle.argDivision
        val application = requireNotNull(this.activity).application

        val dataSource = ElectionDatabase.getInstance(application)
        val viewModelFactory = VoterInfoViewModelFactory(electionId, division, application, dataSource.electionDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel

        //TODO: Handle loading of URLs
        viewModel.votingLocationsUrl.observe(viewLifecycleOwner, {
            it?.let {
                setIntent(it)
                viewModel.onVotingLocationNavigated()
            }

        })

        viewModel.ballotInformationUrl.observe(viewLifecycleOwner, {
            it?.let {
                setIntent(it)
                viewModel.onBallotInformationNavigated()
            }
        })

        viewModel.isElectionFollowed.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    binding.followElectionBtn.visibility = View.GONE
                    binding.unfollowElectionBtn.visibility = View.VISIBLE
                }
                else {
                    binding.followElectionBtn.visibility = View.VISIBLE
                    binding.unfollowElectionBtn.visibility = View.GONE
                }
            }
        })


        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
        return binding.root
    }

    //TODO: Create method to load URL intents
    private fun enableLink(view: TextView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}