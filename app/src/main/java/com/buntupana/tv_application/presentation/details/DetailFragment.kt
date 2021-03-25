package com.buntupana.tv_application.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.buntupana.tv_application.R
import com.buntupana.tv_application.core.setToolbar
import com.buntupana.tv_application.databinding.FragmentDetailBinding
import com.buntupana.tv_application.databinding.ItemRecommendationBinding
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.presentation.common.SimpleListBindingAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment(),
    SimpleListBindingAdapter.OnItemClickListener<ItemRecommendationBinding> {

    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var detailViewModelAssistedFactory: DetailViewModel.AssistedFactory
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModel.provideFactory(detailViewModelAssistedFactory, args.filmId)
    }

    private lateinit var binding: FragmentDetailBinding

    private val recommendationsAdapter =
        SimpleListBindingAdapter<DetailViewModel.RecommendationViewEntity, ItemRecommendationBinding>(
            R.layout.item_recommendation
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        setToolbar(binding.toolbarDetail)

        binding.imageFavourite.setOnClickListener {
            viewModel.changeFavourite()
        }

        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).let {
            binding.recyclerRecommendations.layoutManager = it
        }

        recommendationsAdapter.listener = this
        binding.recyclerRecommendations.adapter = recommendationsAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.filmResourceState.observe(viewLifecycleOwner) {}
        viewModel.recommendationResource.observe(viewLifecycleOwner) { resource ->
            Timber.d("setupObservers: recommendations result $resource")
            when (resource) {
                is Resource.Error -> {
                    Timber.d("ERROR")
                }
                is Resource.Loading -> Timber.d("LOADING")
                is Resource.Success -> {
                    binding.progressRecommendations.visibility = View.GONE
                    recommendationsAdapter.submitList(resource.data)
                }
            }
        }
    }

    override fun onItemClick(binding: ItemRecommendationBinding, position: Int) {
        val filmId = viewModel.getRecommendationId(position)
        findNavController().navigate(DetailFragmentDirections.actionDetailFragmentSelf(filmId))
    }
}