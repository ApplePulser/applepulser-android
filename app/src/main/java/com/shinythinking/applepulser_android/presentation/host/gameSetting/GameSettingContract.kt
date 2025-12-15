package com.shinythinking.applepulser_android.presentation.host.gameSetting

import com.shinythinking.applepulser_android.domain.model.GameMode
import com.shinythinking.applepulser_android.domain.model.RoomInfo

object GameSettingContract {

    sealed interface State {
        val roomInfo: RoomInfo?
        data class ModeSelection(
            override val roomInfo: RoomInfo? = null,
            val selectedMode: GameMode? = null
        ) : State {
            val canProceed: Boolean get() = selectedMode != null
        }

        data class SettingsInput(
            override val roomInfo: RoomInfo? = null,
            val selectedMode: GameMode,
            val minHeartRate: Int = 120,
            val maxHeartRate: Int = 140,
            val duration: Int = 5,
        ) : State {
            val canProceed: Boolean get() = isValidSettings()

            private fun isValidSettings(): Boolean {
                return minHeartRate in 60..180 && maxHeartRate in 80..200 && maxHeartRate > minHeartRate && duration in 1..60
            }
        }

        data class SettingsCheck(
            override val roomInfo: RoomInfo? = null,
            val selectedMode: GameMode,
            val minHeartRate: Int,
            val maxHeartRate: Int,
            val duration: Int,
        ) : State

        data object Launching : State {
            override val roomInfo: RoomInfo? = null
        }

        data class Error(
            val message: String, val previousState: State?
        ) : State {
            override val roomInfo: RoomInfo? = null
        }
    }

    sealed interface Intent {
        data class ModeSelected(val mode: GameMode) : Intent
        object ModeNextClicked : Intent

        data class MinHeartRateChanged(val value: Int) : Intent
        data class MaxHeartRateChanged(val value: Int) : Intent
        data class DurationChanged(val value: Int) : Intent
        object SettingsNextClicked : Intent

        object LaunchClicked : Intent

        object BackClicked : Intent
        object ErrorDismissed : Intent
    }

    sealed interface SideEffect {
        data class NavigateToGamePlay(
            val playerId: String,
            val roomId: String,
            val settingJson: String,
            val deviceAddress: String
        ) : SideEffect

        object NavigateBack : SideEffect
        data class ShowToast(val message: String) : SideEffect
    }
}