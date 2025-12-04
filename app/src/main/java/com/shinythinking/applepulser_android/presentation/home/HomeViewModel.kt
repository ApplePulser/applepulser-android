package com.shinythinking.applepulser_android.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel(), ContainerHost<HomeContract.State, HomeContract.SideEffect> {

    override val container = container<HomeContract.State, HomeContract.SideEffect>(
        initialState = HomeContract.State()
    )

    fun onIntent(intent: HomeContract.Intent) = when (intent) {
        is HomeContract.Intent.LaunchRoomClick -> handleLaunchRoom()
        is HomeContract.Intent.EnterRoomClick -> handleEnterRoom()
    }

    private fun handleLaunchRoom() = intent {
        postSideEffect(HomeContract.SideEffect.NavigateToLaunchRoom)
    }

    private fun handleEnterRoom() = intent {
        postSideEffect(HomeContract.SideEffect.NavigateToEnterRoom)
    }
}