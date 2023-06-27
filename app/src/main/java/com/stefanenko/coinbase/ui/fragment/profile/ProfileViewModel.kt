package com.stefanenko.coinbase.ui.fragment.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.exception.ERROR_UNAUTHORIZED
import com.stefanenko.coinbase.domain.useCase.ProfileUseCases
import com.stefanenko.coinbase.util.errorHandler.ViewModelErrorHandler
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("MoveVariableDeclarationIntoWhen")
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val connectivityManager: NetworkConnectivityManager,
    val authPreferences: AuthPreferences,
    private val viewModelErrorHandler: ViewModelErrorHandler
) : ViewModel() {

    val state = MutableLiveData<StateProfile>()

    fun getProfile() {
        if (authPreferences.isUserAuth()) {
            if (connectivityManager.isConnected()) {
                state.value = StateProfile.StartLoading
                viewModelScope.launch {
                    val accessToken = authPreferences.getAccessToken()
                    val responseState = profileUseCases.getProfile(accessToken)

                    when (responseState) {
                        is ResponseState.Data -> state.value =
                            StateProfile.ShowProfileData(responseState.data)
                        is ResponseState.Error -> {
                            handleResponseError(responseState)
                        }
                        else -> Unit
                    }

                    state.value = StateProfile.StopLoading
                }
            } else {
                state.value = StateProfile.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
            }
        } else {
            state.value = StateProfile.GuestMode
        }
    }

    private suspend fun handleResponseError(responseState: ResponseState.Error<Profile>) {
        when (responseState.error) {
            ERROR_UNAUTHORIZED -> {
                viewModelErrorHandler.handleUnauthorizedError(
                    {
                        authPreferences.saveAccessToken(it.first)
                        authPreferences.saveRefreshToken(it.second)
                        state.value = StateProfile.ReAuthPerformed
                    }, {
                        state.value = StateProfile.ShowErrorMessage(it)
                    })
            }
            else -> state.value = StateProfile.ShowErrorMessage(responseState.error)
        }
    }

    fun performLogout() {
        authPreferences.clearLoginSate()
        state.value = StateProfile.LogOut
    }

    fun setBlankState() {
        state.value = StateProfile.BlankState
    }
}