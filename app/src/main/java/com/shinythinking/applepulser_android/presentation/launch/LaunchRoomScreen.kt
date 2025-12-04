package com.shinythinking.applepulser_android.presentation.launch

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LaunchRoomScreen(
    onNavigateToHostRoom: (String, String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LaunchRoomViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LaunchRoomContract.SideEffect.NavigateToHostRoom -> {
                onNavigateToHostRoom(sideEffect.playerId, sideEffect.roomId)
            }

            is LaunchRoomContract.SideEffect.NavigateBack -> {
                onNavigateBack()
            }

            is LaunchRoomContract.SideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            is LaunchRoomContract.SideEffect.CopyToClipboard -> {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Room Code", sideEffect.code)
                clipboard.setPrimaryClip(clip)
            }

            is LaunchRoomContract.SideEffect.ShareRoomInfo -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Join my Heart Sync game! Code: ${sideEffect.code}")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }
        }
    }

    LaunchRoomContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
fun LaunchRoomContent(
    state: LaunchRoomContract.State,
    onIntent: (LaunchRoomContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    APBackground(
        modifier = modifier.fillMaxSize(),
        appleExist = false,
        buttonExist = true,
        buttonText = "Enter",
        buttonEnable = true,
        onButtonClick = {
            onIntent(LaunchRoomContract.Intent.OnEnterClicked)
        }
    ) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (state.error != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Failed to create room", color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                IconButton(onClick = { onIntent(LaunchRoomContract.Intent.OnRetryClicked) }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Retry", tint = Color.White)
                }
            }
        } else {
            state.roomInfo?.let { roomInfo ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 60.dp), // 하단 버튼 여백
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(80.dp))

                    Text(
                        text = "Successfully created",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "Room Code",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = roomInfo.roomCode,
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        fontSize = 48.sp
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
//                        if (roomInfo.qrCode != null) {
//                            AsyncImage(
//                                model = roomInfo.qrCode,
//                                contentDescription = "QR Code",
//                                modifier = Modifier.fillMaxSize(),
//                                contentScale = ContentScale.Fit
//                            )
//                        } else {
                        // QR 코드가 없을 때 Placeholder (혹은 로딩)
                        Text("QR Code Generating...", color = Color.Black)
//                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Copy & Share Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        IconAction(
                            icon = Icons.Outlined.Share,
                            label = "Copy",
                            onClick = { onIntent(LaunchRoomContract.Intent.OnCopyCodeClicked) }
                        )
                        IconAction(
                            icon = Icons.Outlined.Share,
                            label = "Share",
                            onClick = { onIntent(LaunchRoomContract.Intent.OnShareClicked) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IconAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview
@Composable
fun LaunchRoomScreenPreview() {
    ApplepulserTheme {
        LaunchRoomContent(
            state = LaunchRoomContract.State(
                isLoading = false,
                roomInfo = com.shinythinking.applepulser_android.domain.model.RoomInfo(
                    roomId = "1",
                    roomCode = "123456",
                    status = com.shinythinking.applepulser_android.domain.model.RoomStatus.WAITING,
//                    qrCode = null // 프리뷰에선 QR 없음
                )
            ),
            onIntent = {}
        )
    }
}