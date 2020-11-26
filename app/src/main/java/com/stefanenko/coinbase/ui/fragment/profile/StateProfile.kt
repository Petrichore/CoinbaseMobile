package com.stefanenko.coinbase.ui.fragment.profile

import com.stefanenko.coinbase.domain.entity.Profile

sealed class StateProfile {
    object StartLoading : StateProfile()
    object StopLoading : StateProfile()
    object GuestMode: StateProfile()
    data class ShowProfileData(val profile: Profile) : StateProfile()
    object ReAuthPerformed : StateProfile()
    object LogOut : StateProfile()
}

sealed class StateScattering {
    data class ShowErrorMessage(val error: String) : StateScattering()
    object ScatterLastState : StateScattering()
}