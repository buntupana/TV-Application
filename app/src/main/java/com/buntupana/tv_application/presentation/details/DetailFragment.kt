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
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

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

        binding.buttonRetry.setOnClickListener {
            viewModel.retry()
        }

        binding.buttonRecommendationRetry.setOnClickListener {
            viewModel.retryRecommendations()
        }

        binding.recyclerRecommendations.isNestedScrollingEnabled = false

        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).let {
            binding.recyclerRecommendations.layoutManager = it
        }

        recommendationsAdapter.listener = this
        binding.recyclerRecommendations.adapter = recommendationsAdapter

        enableCollapsingBehaviour()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.filmResourceState.observe(viewLifecycleOwner) { resource ->

            when (resource) {
                is Resource.Error -> {
                    Timber.e(resource.exception)
                    // if there is an error with no data we'll show the button retry
                    if (viewModel.filmViewEntity.value == null) {
                        binding.layoutLoading.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.textError.visibility = View.VISIBLE
                        binding.buttonRetry.visibility = View.VISIBLE
                    }
                }
                is Resource.Loading -> {
                    // when loading and no data we'll show loading
                    if (viewModel.filmViewEntity.value == null) {
                        binding.layoutLoading.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.textError.visibility = View.GONE
                        binding.buttonRetry.visibility = View.GONE
                    }
                }
                is Resource.Success -> {
                    // when result is success and there is data we'll show the info
                    if (viewModel.filmViewEntity.value != null) {
                        binding.layoutLoading.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.textError.visibility = View.GONE
                        binding.buttonRetry.visibility = View.GONE
                    }
                }
            }
        }
        viewModel.recommendationResource.observe(viewLifecycleOwner) { resource ->
            Timber.d("setupObservers: recommendations result ${resource.getStatusString()}")
            when (resource) {
                is Resource.Error -> {
                    binding.progressRecommendations.visibility = View.GONE
                    if (viewModel.isRecommendationsListEmpty()) {
                        binding.buttonRecommendationRetry.visibility = View.VISIBLE
                        binding.textRecommendationError.visibility = View.VISIBLE
                    }
                }
                is Resource.Loading -> {
                    binding.progressRecommendations.visibility = View.VISIBLE
                    binding.buttonRecommendationRetry.visibility = View.GONE
                    binding.textRecommendationError.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.buttonRecommendationRetry.visibility = View.GONE
                    binding.textRecommendationError.visibility = View.GONE
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

    /**
     * When [AppBarLayout] is collapsed the title will be visible and the favourite will be gone
     * vice versa happens when is expanded
     */
    private fun enableCollapsingBehaviour() {
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val percentage =
                (binding.appBar.totalScrollRange - abs(verticalOffset).toFloat()) / binding.appBar.totalScrollRange

            when {
                percentage > 0.5f -> binding.imageFavourite.visibility = View.VISIBLE
                percentage < 0.1f -> binding.toolbarDetail.title =
                    viewModel.filmViewEntity.value?.title ?: ""
                percentage < 0.3f -> binding.imageFavourite.visibility = View.GONE
                percentage > 0.3f -> binding.toolbarDetail.title = ""
            }
        })
    }
}