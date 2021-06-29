package com.stefanenko.coinbase.ui.fragment.profile

import com.stefanenko.coinbase.domain.entity.Profile

sealed class StateProfile {
    object BlankState: StateProfile()
    object ReAuthPerformed : StateProfile()
    object LogOut : StateProfile()
    object StartLoading : StateProfile()
    object StopLoading : StateProfile()
    object GuestMode: StateProfile()
    data class ShowProfileData(val profile: Profile) : StateProfile()
    data class ShowErrorMessage(val error: String) : StateProfile()
}