package com.buntupana.tv_application.presentation.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.buntupana.tv_application.R
import com.buntupana.tv_application.databinding.FragmentHomeBinding
import com.buntupana.tv_application.presentation.films.FilmsFragment
import com.buntupana.tv_application.presentation.films.FilmsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private var filmsFragment: FilmsFragment? = null
    private var favouritesFragment: FilmsFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        // setting up the bottom nav controller
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val bottomNavController =
                childFragmentManager.findFragmentById(R.id.nav_home_menu_host_fragment)!!
                    .findNavController()

            binding.bottomNavigationView?.setupWithNavController(bottomNavController)
        } else {
            if (filmsFragment == null || favouritesFragment == null) {
                filmsFragment = FilmsFragment.getInstance(FilmsViewModel.TypeScreen.FILMS)
                favouritesFragment =
                    FilmsFragment.getInstance(FilmsViewModel.TypeScreen.FAVOURITES)
                childFragmentManager.beginTransaction().replace(
                    R.id.layout_films, filmsFragment!!
                ).commit()
                childFragmentManager.beginTransaction().replace(
                    R.id.layout_favourites, favouritesFragment!!
                ).commit()
            }

            binding.searchBoxHome?.editTextSearchKey?.doOnTextChanged { searchKey, _, _, _ ->
                filmsFragment?.browse(searchKey.toString())
                favouritesFragment?.browse(searchKey.toString())
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favouritesCountObservable.observe(viewLifecycleOwner) { favouritesCount ->
            setFavouriteCountBadge(favouritesCount)
        }
    }

    private fun setFavouriteCountBadge(count: Int) {
        if (count == 0) {
            binding.bottomNavigationView?.removeBadge(R.id.action_favourites)
        } else {
            binding.bottomNavigationView?.getOrCreateBadge(R.id.action_favourites)
                .apply {
//                    backgroundColor =
//                        ContextCompat.getColor(requireContext(), R.color.forest_green)
//                    if (number != count) {
//                        number = count
//                    }
                }
        }
    }
}