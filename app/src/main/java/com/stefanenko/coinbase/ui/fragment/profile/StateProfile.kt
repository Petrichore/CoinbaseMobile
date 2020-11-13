package com.stefanenko.coinbase.ui.fragment.profile

import com.stefanenko.coinbase.domain.entity.Profile

sealed class StateProfile {
    data class ShowErrorMessage(val error: String) : StateProfile()
    data class ShowProfileData(val profile: Profile) : StateProfile()
    object ReAuthPerformed : StateProfile()
}

sealed class InterruptibleState {
    object StartLoading : InterruptibleState()
    object StopLoading : InterruptibleState()
}