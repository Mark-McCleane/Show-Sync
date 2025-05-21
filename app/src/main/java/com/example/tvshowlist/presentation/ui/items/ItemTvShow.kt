package com.example.tvshowlist.presentation.ui.items

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.tvshowlist.R
import com.example.tvshowlist.data.remote.RetrofitInterface
import com.example.tvshowlist.domain.model.TvShow

@Composable
fun ItemTvShow(
    tvShow: TvShow,
    modifier: Modifier = Modifier,
    navigateTo: (tvShow: TvShow) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navigateTo(tvShow) }
    ) {
        val clickableModifier = Modifier.clickable { isExpanded = !isExpanded }
        var clickableHeight by remember {
            mutableIntStateOf(IntSize.Zero.height)
        }

        ListItem(
            modifier = Modifier.padding(0.dp),
            leadingContent = {
                AsyncImage(
                    model = RetrofitInterface.IMAGE_BASE_URL + tvShow.posterPath,
                    contentDescription = stringResource(
                        R.string.tvShowTitle_cover_image,
                        tvShow.title
                    ),
                    error = painterResource(id = android.R.drawable.stat_notify_error),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.width(110.dp)
                )
            },
            headlineContent = {
                Text(
                    text = tvShow.title,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            supportingContent = {
                Text(
                    text = tvShow.description?.ifEmpty { stringResource(R.string.no_description_found) }
                        ?: stringResource(R.string.no_description_found),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    modifier = Modifier
                        .onSizeChanged {
                            clickableHeight = it.height / 2
                        }
                        .fillMaxWidth(0.9f),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = stringResource(
                        R.string.aired_tvShowAirDate,
                        tvShow.airDate ?: stringResource(R.string.no_air_date_found)
                    ),
                    modifier = Modifier.fillMaxWidth(0.9f),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                val expandableArrow =
                    if (isExpanded) painterResource(android.R.drawable.arrow_up_float) else painterResource(
                        android.R.drawable.arrow_down_float
                    )
                val topPadding = if (isExpanded) 101.dp else clickableHeight.dp / 2
                Column(
                    modifier = clickableModifier
                        .height(clickableHeight.dp)
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = topPadding / 2,
                            bottom = topPadding / 2
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = expandableArrow,
                        contentDescription = null,
                        modifier = clickableModifier
                    )
                }
            },
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewItemTvShow2() {
    val tvShow =
        TvShow(0, "This is a very looooooooong Test Description", "Test Title", "2022-02-20")
    ItemTvShow(
        tvShow = tvShow,
        navigateTo = {}
    )
}