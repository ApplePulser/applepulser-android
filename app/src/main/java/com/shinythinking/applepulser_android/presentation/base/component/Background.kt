package com.shinythinking.applepulser_android.presentation.base.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.R
import com.shinythinking.applepulser_android.ui.theme.Applepulser_androidTheme

@Composable
fun Background(
    modifier: Modifier = Modifier,
    appleExist: Boolean = false,
    backExist: Boolean = false,
    content: @Composable (modifier: Modifier) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF615AC7),
                            1.0f to Color(0xFFBC2225)
                        )
                    )
                )
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF0D00FF),
                            0.42f to Color(0xFF0C12FC),
                            1.0f to Color(0xFF00FFD9)
                        )
                    ),
                    alpha = 0.2f
                )
        ) {
            if (backExist) {
                Icon(
                    painterResource(R.drawable.ic_backward),
                    contentDescription = stringResource(R.string.backward),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(24.dp)
                        .size(48.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            if (appleExist) {
                Image(
                    painter = painterResource(R.drawable.ic_bg_big_apple),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd),
                    contentScale = ContentScale.Crop,
                )
            }
        }
        content(modifier)
    }
}

@Preview
@Composable
fun BaseScreenPreview() {
    Applepulser_androidTheme {
        Background(
            modifier = Modifier,
            backExist = true,
            appleExist = true,
        ) {}
    }
}