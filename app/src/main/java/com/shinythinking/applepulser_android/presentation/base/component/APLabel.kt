package com.shinythinking.applepulser_android.presentation.base.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.ui.theme.Applepulser_androidTheme

@Composable
fun APLabel(
    text: String,
    isReady: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isReady) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(percent = 50),
        color = backgroundColor,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview
@Composable
fun APLabelPreview() {
    Applepulser_androidTheme {
        Background() {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                APLabel(
                    text = "true Label",
                    isReady = true
                )
                Spacer(Modifier.height(32.dp))
                APLabel(
                    text = "false Label",
                    isReady = false
                )
            }
        }
    }
}