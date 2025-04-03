package com.example.tvshowlist.presentation.ui.items

import android.R
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
                    contentDescription = stringResource(
                        com.example.tvshowlist.R.string.tvShowTitle_cover_image,
                        tvShowTitle
                    ),
                    error = painterResource(id = R.drawable.stat_notify_error),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(110.dp, 110.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            val clickableModifier = Modifier.clickable { isExpanded = !isExpanded }
            Column(
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Text(
                    text = tvShowTitle,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Divider(thickness = 2.dp)
                Text(
                    text = tvShowTagLine.ifEmpty { stringResource(com.example.tvshowlist.R.string.no_description_found) },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        com.example.tvshowlist.R.string.aired_tvShowAirDate,
                        tvShowAirDate
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = clickableModifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 110.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = if (isExpanded) painterResource(android.R.drawable.arrow_up_float)
                    else painterResource(android.R.drawable.arrow_down_float),
                    contentDescription = null,
                    modifier = clickableModifier)
            }
        }
    }
}

@Preview
@Composable
fun PreviewItemTvShow() {
    val tvShow =
        TvShow(0, "This is a very looooooooong Test Description", "Test Title", "2022-02-20")
    ItemTvShow(
        tvShow = tvShow,
        tvShowTitle = tvShow.title,
        tvShowAirDate = tvShow.airDate ?: "",
        tvShowTagLine = tvShow.description ?: "",
        imageUrl = "",
        navigateTo = {}
    )
}