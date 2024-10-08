package com.stefanenko.coinbase.ui.fragment.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentFavoritesBinding
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.base.decorators.VerticalItemDecoration
import com.stefanenko.coinbase.ui.fragment.favorites.recycler.AdapterFavorites
import com.stefanenko.coinbase.ui.fragment.guestMode.FavoritesGuestModeFragment
import com.stefanenko.coinbase.util.toDp
import javax.inject.Inject

class FavoritesFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: FavoritesViewModel

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding!!

    private lateinit var recyclerAdapter: AdapterFavorites

    private lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configSnackBar()
        viewModel.getFavorites()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoritesViewModel::class.java]
    }

    override fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is StateFavorites.ShowFavoritesRecycler -> {
                    initFavoritesRecycler(state.itemList)
                }

                is StateFavorites.ItemRemoveIntent -> {
                    recyclerAdapter.onDeleteItem(state.position)
                }

                is StateFavorites.CancelItemDelete -> {
                    recyclerAdapter.onInsertItem(state.position, state.item)
                }

                is StateFavorites.ShowErrorMessage -> {
                    showInfoDialog(resources.getString(R.string.alert_dialog_title_error), state.error)
                }

                StateFavorites.GuestMode -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FavoritesGuestModeFragment()).commit()
                }

                StateFavorites.StartLoading -> binding.progressBar.visibility = View.VISIBLE
                StateFavorites.StopLoading -> binding.progressBar.visibility = View.GONE

                StateFavorites.ShowRetrieveItemSnackBar -> {
                    snackbar.show()
                }
            }
        })
    }

    private fun configSnackBar() {
        snackbar = Snackbar.make(
            requireView(),
            resources.getString(R.string.snackBar_text_exchange_rate_removed),
            Snackbar.LENGTH_SHORT
        )
            .apply {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.main_green))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setActionTextColor(ContextCompat.getColor(context, R.color.color_secondary))

                setAction(resources.getString(R.string.snackBar_text_undo_remove)) {
                    viewModel.cancelItemDelete()
                }
                addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        showDebugLog("SnackBar dissmissed")
                        viewModel.performDelete()
                    }
                })
            }
    }

    private fun initFavoritesRecycler(itemList: List<ExchangeRate>) {
        with(binding.favoritesRecycler) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerAdapter = AdapterFavorites(itemList) {}
            adapter = recyclerAdapter
            addItemDecoration(VerticalItemDecoration(14.toDp()))
        }
        ItemTouchHelper(setItemTouchHelper()).attachToRecyclerView(binding.favoritesRecycler)
    }

    private fun setItemTouchHelper(): ItemTouchHelper.SimpleCallback {
        return object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.adapterPosition
                viewModel.itemDeleteIntent(
                    itemPosition,
                    recyclerAdapter.getItemByPosition(itemPosition)
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //snackbar.dismiss()
        viewModel.setBlankState()
    }
}