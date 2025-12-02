package com.shinythinking.applepulser_android.presentation.base.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme

@Composable
fun TextWithSideBar(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onlyTitle: Boolean = false,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.5f
                    ),
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Column(
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
            if (!onlyTitle) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun TextWithSideBarPreview() {
    ApplepulserTheme {
        APBackground(
            modifier = Modifier.fillMaxSize()
        )
        {
            TextWithSideBar(
                title = "Title",
                description = "Description",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}