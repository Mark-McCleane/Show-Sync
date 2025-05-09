package com.example.tvshowlist.presentation.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
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
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.size(110.dp)) {
                AsyncImage(
                    model = RetrofitInterface.IMAGE_BASE_URL + tvShow.posterPath,
                    contentDescription = stringResource(
                        com.example.tvshowlist.R.string.tvShowTitle_cover_image,
                        tvShow.title
                    ),
                    error = painterResource(id = android.R.drawable.stat_notify_error),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            val clickableModifier = Modifier.clickable { isExpanded = !isExpanded }

            Column(
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Text(
                    text = tvShow.title,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(5.dp),
                )

                Text(
                    text = tvShow.description?.ifEmpty { stringResource(com.example.tvshowlist.R.string.no_description_found) } ?: stringResource(com.example.tvshowlist.R.string.no_description_found),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(
                        com.example.tvshowlist.R.string.aired_tvShowAirDate,
                        tvShow.airDate ?: "No Air Date Found"
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                modifier = clickableModifier.weight(1f).defaultMinSize(minHeight = 110.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val expandableArrow = if (isExpanded) painterResource(android.R.drawable.arrow_up_float) else painterResource(android.R.drawable.arrow_down_float)
                Icon(
                    painter = expandableArrow,
                    contentDescription = null,
                    modifier = clickableModifier
                )
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
        navigateTo = {}
    )
}