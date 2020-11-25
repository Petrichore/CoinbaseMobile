package com.stefanenko.coinbase.ui.fragment.favorites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.base.decorators.VerticalItemDecoration
import com.stefanenko.coinbase.ui.fragment.favorites.recycler.AdapterFavorites
import com.stefanenko.coinbase.util.toDp
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_favorites.progressBar
import javax.inject.Inject

class FavoritesFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FavoritesViewModel

    private lateinit var recyclerAdapter: AdapterFavorites

    override fun getLayoutId(): Int = R.layout.fragment_favorites

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).toolbar.title = resources.getString(R.string.title_favorites)
        viewModel.getFavorites()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoritesViewModel::class.java]
    }

    override fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is StateFavorites.ShowErrorMessage -> {
                    showInfoDialog("Error", state.error)
                }
                is StateFavorites.ShowFavoritesRecycler -> {
                    initFavoritesRecycler(state.itemList)
                }
            }
        })

        viewModel.interruptibleState.observe(viewLifecycleOwner, { interruptibleState ->
            when (interruptibleState) {
                InterruptibleState.StartLoading -> progressBar.visibility = View.VISIBLE
                InterruptibleState.StopLoading -> progressBar.visibility = View.GONE
            }
        })
    }

    private fun initFavoritesRecycler(itemList: List<ExchangeRate>) {
        with(favoritesRecycler) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerAdapter = AdapterFavorites(itemList) {

            }

            adapter = recyclerAdapter
            addItemDecoration(VerticalItemDecoration(14.toDp()))
        }
    }
}