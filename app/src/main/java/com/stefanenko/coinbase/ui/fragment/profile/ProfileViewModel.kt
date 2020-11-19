package com.stefanenko.coinbase.ui.fragment.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.exception.ERROR_UNAUTHORIZED
import com.stefanenko.coinbase.domain.repository.DataRepository
import com.stefanenko.coinbase.ui.base.BaseViewModel
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("MoveVariableDeclarationIntoWhen")
class ProfileViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val connectivityManager: NetworkConnectivityManager,
    private val authPreferences: AuthPreferences
) : BaseViewModel() {

    val state = MutableLiveData<StateProfile>()
    val interruptibleState = MutableLiveData<InterruptibleState>()

    fun getProfile() {
        if (connectivityManager.isConnected()) {
            interruptibleState.value = InterruptibleState.StartLoading

            viewModelScope.launch {
                val responseState = dataRepository.getProfile(authPreferences.getAccessToken())

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
                                    state.value = StateProfile.ShowErrorMessage(it)
                                })
                            }
                            else -> state.value =
                                StateProfile.ShowErrorMessage(responseState.error)
                        }
                    }
                }

                interruptibleState.value = InterruptibleState.StopLoading
            }
        } else {
            state.value = StateProfile.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
        }
    }

    fun performLogout(){
        authPreferences.clearLoginSate()
        state.value = StateProfile.LogOut
    }
}