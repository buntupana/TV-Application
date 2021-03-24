package com.buntupana.tv_application.presentation.films

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.buntupana.tv_application.R
import com.buntupana.tv_application.databinding.FragmentFilmsBinding
import com.buntupana.tv_application.databinding.ItemFilmBinding
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.presentation.common.FilmListBindingAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FilmsFragment : Fragment(), FilmListBindingAdapter.OnFilmItemClickListener {

    private val viewModel: FilmsViewModel by viewModels()

    private lateinit var binding: FragmentFilmsBinding

    private val adapter = FilmListBindingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmsBinding.inflate(inflater, container, false)

        binding.recyclerFilm.adapter = adapter
        adapter.listener = this

        binding.recyclerFilm.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )

        binding.searchBox.editTextSearchKey.doOnTextChanged { searchKey, _, _, _ ->
            Timber.d("onCreateView() called with: searchKey = [$searchKey]")
            viewModel.browse(searchKey.toString())
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.browse(binding.searchBox.editTextSearchKey.text.toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupObservers()
    }

    private fun setupObservers() {

        viewModel.filmViewEntityList.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Timber.e(resource.exception)
                    if (adapter.currentList.isEmpty()) {
                        viewModel.setInfoNetWorkProblem()
                        binding.textInfoMessage.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(
                            requireContext(),
                            R.string.message_error_connection,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> Timber.d("LOADING")
                is Resource.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    resource.data?.let { filmEntityViewList ->
                        if (filmEntityViewList.isEmpty()) {
                            viewModel.setInfoAsNoMatchesFound()
                            binding.textInfoMessage.visibility = View.VISIBLE
                        } else {
                            binding.textInfoMessage.visibility = View.GONE
                        }
                        adapter.submitList(filmEntityViewList)
                    }
                }
            }
        }
    }

    override fun onItemClick(binding: ItemFilmBinding, position: Int) {
        Timber.d("onItemClick() called with: binding = [$binding], position = [$position]")
        Toast.makeText(requireContext(), "Item $position clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onFavouriteClick(favorite: Boolean, position: Int) {
        Timber.d("onFavouriteClick() called with: favorite = [$favorite], position = [$position]")
        viewModel.setFavourite(favorite, position)
    }
}