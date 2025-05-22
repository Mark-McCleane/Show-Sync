package com.example.tvshowlist.presentation.ui.items

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
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
    val clickableModifier = Modifier.clickable(
        indication = rememberRipple(bounded = true, radius = 32.dp),
        interactionSource = remember { MutableInteractionSource() }
    ) { isExpanded = !isExpanded }

    var clickableHeight by remember {
        mutableIntStateOf(IntSize.Zero.height)
    }

    ListItem(
        modifier = Modifier
            .padding(0.dp)
            .clickable { navigateTo(tvShow) },
        leadingContent = {
            AsyncImage(
                model = RetrofitInterface.IMAGE_BASE_URL + tvShow.posterPath,
                contentDescription = stringResource(
                    R.string.tvShowTitle_cover_image,
                    tvShow.title
                ),
                error = painterResource(id = android.R.drawable.stat_notify_error),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .onSizeChanged {
                        if (clickableHeight < it.height / 2) {
                            clickableHeight = it.height / 2
                        }
                    }
                    .width(110.dp)
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged {
                        if (clickableHeight < it.height / 2) {
                            clickableHeight = it.height / 2
                        }
                    }
            ) {
                Text(
                    text = tvShow.description?.ifEmpty { stringResource(R.string.no_description_found) }
                        ?: stringResource(R.string.no_description_found),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(
                        R.string.aired_tvShowAirDate,
                        tvShow.airDate ?: stringResource(R.string.no_air_date_found)
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        trailingContent = {
            val expandableArrow = if (isExpanded) painterResource(android.R.drawable.arrow_up_float)
            else painterResource(
                android.R.drawable.arrow_down_float
            )

            Column(
                modifier = clickableModifier
                    .heightIn(min = 200.dp, max = clickableHeight.dp)
                    .padding(vertical = 16.dp, horizontal = 9.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = expandableArrow,
                    contentDescription = null,
                )
            }
        },
    )
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