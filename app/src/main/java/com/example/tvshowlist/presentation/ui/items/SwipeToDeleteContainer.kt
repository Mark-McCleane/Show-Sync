package com.example.tvshowlist.presentation.ui.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.SwipeToDismissDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    onConfirmDeletion: () -> Unit,
    onDeletionTitle: String,
    onDeletionMessage: String,
    content: @Composable (T) -> Unit,
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }

    var showConfirmationDialog by remember {
        mutableStateOf(false)
    }

    var isConfirmed by remember { mutableStateOf(false) }

    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToEnd) {
                showConfirmationDialog = true
                isRemoved
            } else {
                isRemoved = false
                false
            }
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.5f }
    )

    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        title = onDeletionTitle,
        message = onDeletionMessage,
        onConfirm = {
            isConfirmed = true
            isRemoved = true
            onConfirmDeletion()
        },
        onDismiss = {
            isConfirmed = false
            showConfirmationDialog = false
        }
    )



    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            background = {
                DeleteBackground(state)
            },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.StartToEnd)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color =
        if (swipeDismissState.dismissDirection == DismissDirection.StartToEnd) {
            Color.Red
        } else Color.Transparent

    val alignment =
        if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) Alignment.CenterEnd
        else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = alignment
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}