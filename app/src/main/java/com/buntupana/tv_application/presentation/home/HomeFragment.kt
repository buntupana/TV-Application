package com.buntupana.tv_application.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.buntupana.tv_application.R
import com.buntupana.tv_application.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        // setting up the bottom nav controller
        val bottomNavController =
            childFragmentManager.findFragmentById(R.id.nav_home_menu_host_fragment)!!
                .findNavController()

        binding.bottomNavigationView.setupWithNavController(bottomNavController)

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
            binding.bottomNavigationView.removeBadge(R.id.action_favourites)
        } else {
            binding.bottomNavigationView.getOrCreateBadge(R.id.action_favourites)
                .apply {
                    backgroundColor = ContextCompat.getColor(requireContext(), R.color.purple_500)
                    if (number != count) {
                        number = count
                    }
                }
        }
    }
}