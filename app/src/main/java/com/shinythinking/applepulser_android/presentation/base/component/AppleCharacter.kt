package com.shinythinking.applepulser_android.presentation.base.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerStatus
import com.shinythinking.applepulser_android.domain.model.PlayerType

@Composable
fun AppleCharacter(
    player: Player,
    modifier: Modifier = Modifier,
    showHeartRate: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = player.colorType.getCharacterImage()),
            contentDescription = player.colorType.toColorName(),
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = player.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        if (showHeartRate && player.bpm != null) {
            Text(
                text = "${player.bpm}bpm",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppleCharacterPreview() {
    APBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppleCharacter(
                    player = Player(
                        id = "1",
                        name = "홍사인",
                        status = PlayerStatus.PLAYING,
                        isHost = true,
                        colorType = PlayerType.YELLOW,
                        bpm = 120
                    )
                )
            }
        }
    }
}