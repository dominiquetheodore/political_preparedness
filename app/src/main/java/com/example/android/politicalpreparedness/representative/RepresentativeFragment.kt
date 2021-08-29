package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.representative.model.Representative
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*


class DetailFragment : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        //TODO: Add Constant for Location request
        private val REQUEST_LOCATION_PERMISSION = 1
    }

    //TODO: Declare ViewModel
    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(this).get(RepresentativeViewModel::class.java)
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Establish bindings
        var binding = FragmentRepresentativeBinding.inflate(inflater)

        val states = resources.getStringArray(R.array.states)

        //TODO: Define and assign Representative adapter
        val clickListener = {representative : Representative -> Unit}
        val representativesAdapter = RepresentativeListAdapter(RepresentativeListener(clickListener)) // { election -> adapterOnClick(election) }
        binding.representativesRecycler.adapter = representativesAdapter

        //TODO: Populate Representative adapter
        viewModel.representatives.observe(viewLifecycleOwner, Observer<List<Representative>> {
            it?.let {
                Timber.i("list has changed")
                representativesAdapter.submitList(it)
            }
        })

        viewModel.address.observe(viewLifecycleOwner, {
            Timber.i("address has changed")
            it?.let {
                binding.addressLine1.setText(it.line1)
                binding.addressLine2.setText(it.line2)
                binding.city.setText(it.city)
                binding.zip.setText(it.zip)
                binding.state.setSelection(states.indexOf(it.state));
            }
        })

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        /*LocationServices.getFusedLocationProviderClient(requireContext()!!).lastLocation.addOnSuccessListener {
            //TODO: UI updates.
            Timber.i("update last location")
        }*/

        //TODO: Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            getLocation()
            viewModel.getRepresentatives()
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.states,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.state.adapter = adapter
            }

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            Timber.i("search button clicked")
            val line1 = binding.addressLine1.text.toString()
            val line2 = binding.addressLine2.text.toString()
            val city = binding.city.text.toString()
            val zip = binding.zip.text.toString()
            val state = binding.state.getSelectedItem().toString()
            viewModel.getAddressFromSearch(line1, line2, city, zip, state)
            viewModel.getRepresentatives()
        }
        binding.lifecycleOwner = this

        return binding.root

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        if (requestCode == REQUEST_LOCATION_PERMISSION &&
            grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
        }
        else {
            Timber.i("Permissions not granted")
            val permissionSnackbar = Snackbar.make(
                requireView(),
                "Location permissions must be enabled to use this feature.",
                Snackbar.LENGTH_INDEFINITE
            )
            permissionSnackbar.setAction("Retry", View.OnClickListener {
                getLocation()
            })
            permissionSnackbar.show()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        if (isPermissionGranted()) {
            return true;
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            return false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        try {
            if (checkLocationPermissions()) {
                val lastLocation = fusedLocationProviderClient.lastLocation

                lastLocation.addOnSuccessListener(requireActivity()) { location: Location? ->
                        if (location !== null) {
                            viewModel.getAddressFromLocation(geoCodeLocation(location))
                        } else {
                            Timber.i("Last location unknown. Fallback to a default location.")
                            val loc = Location("dummyprovider")
                            loc.setLatitude(38.8860);
                            loc.setLongitude(-76.9995);
                            viewModel.getAddressFromLocation(geoCodeLocation(loc))
                        }
                    }
                    .addOnFailureListener {
                        Timber.i("Could not get device location.")
                    }
            }
            else {
                Timber.i("permissions not granted")
            }
        } catch (e: SecurityException) {
            Timber.e(e, e.message)
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())

        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}