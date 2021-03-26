package com.buntupana.tv_application.presentation.films

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.buntupana.tv_application.R
import com.buntupana.tv_application.databinding.FragmentFilmsBinding
import com.buntupana.tv_application.databinding.ItemFilmBinding
import com.buntupana.tv_application.databinding.ItemFilmFavoriteBinding
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.presentation.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FilmsFragment : Fragment(), FilmListBindingAdapter.OnFilmItemClickListener {

    companion object {
        const val TYPE_SCREEN_KEY = "TYPE_SCREEN_KEY"
        fun getInstance(typeScreen: FilmsViewModel.TypeScreen): FilmsFragment {
            Timber.d("getInstance: $typeScreen")
            val args = Bundle().apply {
                putString(TYPE_SCREEN_KEY, typeScreen.toString())
            }
            return FilmsFragment().apply {
                arguments = args
            }
        }
    }

    @Inject
    lateinit var filmsViewModelAssistedFactory: FilmsViewModel.AssistedFactory
    private val viewModel: FilmsViewModel by viewModels {
        FilmsViewModel.provideFactory(filmsViewModelAssistedFactory, typeScreen)
    }

    private lateinit var binding: FragmentFilmsBinding

    private val adapterFilms = FilmListBindingAdapter<ItemFilmBinding>(R.layout.item_film)
    private val adapterFavourites =
        FilmListBindingAdapter<ItemFilmFavoriteBinding>(R.layout.item_film_favorite)

    private val args: FilmsFragmentArgs by navArgs()

    private val navController by lazy {
        val navView = requireActivity().findViewById<View>(R.id.nav_host_fragment)
        Navigation.findNavController(navView)
    }

    private var typeScreen = FilmsViewModel.TypeScreen.FILMS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            arguments?.getString(TYPE_SCREEN_KEY)?.let {
                typeScreen = FilmsViewModel.TypeScreen.valueOf(it)
            }
        } else {
            typeScreen = args.typeScreen
        }
        Timber.d("onCreate: $typeScreen")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmsBinding.inflate(inflater, container, false)

        // Depending on the type of screen we'll set differents adapters
        when (typeScreen) {
            FilmsViewModel.TypeScreen.FILMS -> {
                binding.recyclerFilm.adapter = adapterFilms
                adapterFilms.listenerFilm = this
                // Adding divisors
                binding.recyclerFilm.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayout.VERTICAL
                    )
                )
            }
            FilmsViewModel.TypeScreen.FAVOURITES -> {
                binding.recyclerFilm.adapter = adapterFavourites
                adapterFavourites.listenerFilm = this
                binding.swipeRefresh.isEnabled = false
            }
        }

        binding.searchBox.editTextSearchKey.doOnTextChanged { searchKey, _, _, _ ->
            browse(searchKey.toString())
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.retry()
        }

        return binding.root
    }

    fun browse(searchKey: String) {
        viewModel.browse(searchKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.orientation = resources.configuration.orientation
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        setupObservers()
    }

    private fun setupObservers() {

        viewModel.filmViewEntityList.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Timber.e(resource.exception)// searchBox enabled again
                    binding.searchBox.editSearchKey.isEnabled = true
                    // if there is an error we'll hide loading
                    binding.progressBarFilms.visibility = View.GONE
                    // if the list is empty well show an error message in the screen
                    if (adapterFilms.currentList.isEmpty()) {
                        viewModel.setInfoNetWorkProblem()
                        binding.textInfoMessage.visibility = View.VISIBLE
                    } else {
                        // if the list is not empty we'll show a toast
                        Toast.makeText(
                            requireContext(),
                            R.string.message_error_connection,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    // showing loading
                    Timber.d("setupObservers: Loading")
                    if (viewModel.isListEmpty()) {
                        binding.progressBarFilms.visibility = View.VISIBLE
                        binding.searchBox.editSearchKey.isEnabled = false
                    }
                }
                is Resource.Success -> {
                    // searchBox enabled again
                    binding.searchBox.editSearchKey.isEnabled = true
                    // hiding loading
                    binding.progressBarFilms.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    resource.data?.let { filmEntityViewList ->
                        Timber.d("Success response with ${filmEntityViewList.size} films")
                        // if the list is empty we'll show a message
                        if (filmEntityViewList.isEmpty()) {
                            if (viewModel.searchKey.isBlank()) {
                                viewModel.setInfoNoData()
                            } else {
                                viewModel.setInfoAsNoMatchesFound()
                            }
                            binding.textInfoMessage.visibility = View.VISIBLE
                        } else {
                            binding.textInfoMessage.visibility = View.GONE
                        }
                        when (typeScreen) {
                            FilmsViewModel.TypeScreen.FILMS -> adapterFilms.submitList(
                                filmEntityViewList
                            )
                            FilmsViewModel.TypeScreen.FAVOURITES -> adapterFavourites.submitList(
                                filmEntityViewList
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onItemClick(filmId: String) {
        Timber.d("onItemClick() called with: flimId = [$filmId]")
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(filmId))
    }

    override fun onFavouriteClick(favorite: Boolean, position: Int) {
        Timber.d("onFavouriteClick() called with: favorite = [$favorite], position = [$position]")
        viewModel.setFavourite(favorite, position)
    }
}