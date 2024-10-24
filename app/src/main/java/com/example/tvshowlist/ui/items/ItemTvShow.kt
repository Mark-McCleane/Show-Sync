package com.example.tvshowlist.ui.items

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.tvshowlist.domain.model.TvShow

@Composable
fun ItemTvShow(
    tvShow: TvShow,
    modifier: Modifier = Modifier,
    tvShowTitle: String,
    tvShowAirDate: String = "",
    tvShowTagLine: String = "",
    imageUrl: String,
    navigateTo: (tvShow: TvShow) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navigateTo(tvShow) }
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ) {
            Column(modifier = Modifier) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "$tvShowTitle Cover Image",
                    error = painterResource(id = android.R.drawable.stat_notify_error),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(110.dp, 110.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = tvShowTitle,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Divider(thickness = 2.dp)
                val clickableModifier = Modifier.clickable { isExpanded = !isExpanded }
                Text(
                    text = tvShowTagLine.ifEmpty { "No description found" },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    modifier = clickableModifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Aired: $tvShowAirDate",
                    modifier = clickableModifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }

        }
    }
}