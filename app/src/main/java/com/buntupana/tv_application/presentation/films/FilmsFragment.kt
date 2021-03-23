package com.buntupana.tv_application.presentation.films

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
class FilmsFragment : Fragment() {

    private val viewModel: FilmsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_films, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filmListLiveData.observe(viewLifecycleOwner) { resource ->
            when(resource){
                is Resource.Error -> Timber.e(resource.exception)
                is Resource.Loading -> Timber.d("LOADING")
                is Resource.Success -> Timber.d("SUCCESS with ${resource.data?.size} films")
            }
        }
    }
}