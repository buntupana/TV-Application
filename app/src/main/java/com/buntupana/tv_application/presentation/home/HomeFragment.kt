package com.buntupana.tv_application.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.buntupana.tv_application.R
import com.buntupana.tv_application.domain.entities.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favouritesCountObservable.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> Timber.d("Error")
                is Resource.Loading -> Timber.d("Loading")
                is Resource.Success -> Timber.d("Success: ${resource.data}")
            }
        }
    }
}