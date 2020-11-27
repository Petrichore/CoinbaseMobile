package com.stefanenko.coinbase.ui.fragment.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.exception.ERROR_UNAUTHORIZED
import com.stefanenko.coinbase.domain.repository.DataRepository
import com.stefanenko.coinbase.domain.useCase.ProfileUseCases
import com.stefanenko.coinbase.ui.base.BaseViewModel
import com.stefanenko.coinbase.ui.fragment.favorites.StateFavorites
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("MoveVariableDeclarationIntoWhen")
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val connectivityManager: NetworkConnectivityManager,
    private val authPreferences: AuthPreferences
) : BaseViewModel() {

    val state = MutableLiveData<StateProfile>()
    val stateScattering = MutableLiveData<StateScattering>()

    fun getProfile() {
        if(authPreferences.isUserAuth()){
            if (connectivityManager.isConnected()) {
                state.value = StateProfile.StartLoading
                viewModelScope.launch {
                    val responseState = profileUseCases.getProfile(authPreferences.getAccessToken())
                    when (responseState) {
                        is ResponseState.Data -> state.value =
                            StateProfile.ShowProfileData(responseState.data)
                        is ResponseState.Error -> {
                            when (responseState.error) {
                                ERROR_UNAUTHORIZED -> {
                                    handleTokenRefresh(authPreferences.getRefreshToken(), {
                                        authPreferences.saveAccessToken(it.first)
                                        authPreferences.saveRefreshToken(it.second)
                                        state.value = StateProfile.ReAuthPerformed
                                    }, {
                                        stateScattering.value = StateScattering.ShowErrorMessage(it)
                                    })
                                }
                                else -> stateScattering.value =
                                    StateScattering.ShowErrorMessage(responseState.error)
                            }
                        }
                    }
                    state.value = StateProfile.StopLoading
                }
            } else {
                stateScattering.value = StateScattering.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
            }
        }else{
            state.value = StateProfile.GuestMode
        }
    }

    fun performLogout() {
        authPreferences.clearLoginSate()
        state.value = StateProfile.LogOut
    }

    fun scatterStates(){
        stateScattering.value = StateScattering.ScatterLastState
    }
}