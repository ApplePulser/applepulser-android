package com.shinythinking.applepulser_android.presentation.host.hostRoom

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.applepulser_android.R
import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerStatus
import com.shinythinking.applepulser_android.domain.model.RoomInfo
import com.shinythinking.applepulser_android.domain.model.RoomStatus
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.presentation.base.component.APLabel
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HostRoomScreen(
    onNavigateToGameSetting: (String, String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HostRoomViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    BackHandler {
        viewModel.onIntent(HostRoomContract.Intent.BackClicked)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HostRoomContract.SideEffect.NavigateToGameSetting -> {
                onNavigateToGameSetting(sideEffect.playerId, sideEffect.roomId)
            }

            is HostRoomContract.SideEffect.NavigateBack -> {
                onNavigateBack()
            }

            is HostRoomContract.SideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    HostRoomContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
fun HostRoomContent(
    state: HostRoomContract.State,
    onIntent: (HostRoomContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    APBackground(
        modifier = modifier,
        appleExist = true,
        backExist = true,
        buttonExist = true,
        buttonText = "Start",
        buttonEnable = state.roomInfo?.canStart ?: false,
        isClicked = true,
        onBackButtonClicked = { onIntent(HostRoomContract.Intent.BackClicked) },
        onButtonClick = { onIntent(HostRoomContract.Intent.StartGameClicked) },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = stringResource(R.string.participation_status),
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.member),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground

            )
            Spacer(Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(state.roomInfo?.players ?: emptyList()) { player ->
                    APLabel(
                        text = player.name,
                        isReady = player.status == PlayerStatus.READY,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ClientRoomScreenPreview() {
    ApplepulserTheme {
        HostRoomContent(
            state = HostRoomContract.State(
                roomInfo = RoomInfo(
                    roomId = "1",
                    roomCode = "1",
//                    qrCode = "1",
                    status = RoomStatus.PLAYING,
                    maxPlayers = 3,
                    players = listOf(
                        Player(id = "12", name = "홍사인"),
                        Player(id = "1", name = "한예준"),
                        Player(id = "1", name = "신바다"),
                        Player(id = "1", name = "김나경")
                    )
                ),
                roomId = "1",
                myPlayerId = "12",
                isReady = true,
                isLoading = false
            ),
            onIntent = {},
        )
    }
}