package com.aylar.reordercompose.ui.reorder

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aylar.reordercompose.reorderable.ReorderableItem
import com.aylar.reordercompose.reorderable.detectReorderAfterLongPress
import com.aylar.reordercompose.reorderable.rememberReorderableLazyListState
import com.aylar.reordercompose.reorderable.reorderable

@Composable
fun ReorderList(vm: ReorderListViewModel = viewModel()) {
    Column {
        HorizontalReorderList(
            vm = vm,
            modifier = Modifier.padding(vertical = 16.dp),
        )
        VerticalReorderList(vm = vm)
    }
}

@Composable
private fun VerticalReorderList(
    modifier: Modifier = Modifier,
    vm: ReorderListViewModel,
) {
    val state = rememberReorderableLazyListState(onMove = vm::moveDog, canDragOver = vm::isDogDragEnabled)
    LazyColumn(
        state = state.listState,
        modifier = modifier.reorderable(state)
    ) {
        items(vm.dogs, { item -> item.key }) { item ->
            ReorderableItem(state, item.key) { dragging ->
                val elevation = animateDpAsState(if (dragging) 8.dp else 0.dp)
                if (item.isLocked) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    ) {
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .detectReorderAfterLongPress(state)
                            .shadow(elevation.value)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(16.dp)
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
@Composable
private fun HorizontalReorderList(
    vm: ReorderListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = rememberReorderableLazyListState(onMove = vm::moveCat)
    LazyRow(
        state = state.listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .then(Modifier.reorderable(state))
            .detectReorderAfterLongPress(state),
    ) {
        items(vm.cats, { item -> item.key }) { item ->
            ReorderableItem(state, item.key) { dragging ->
                val scale = animateFloatAsState(if (dragging) 1.1f else 1.0f)
                val elevation = if (dragging) 8.dp else 0.dp
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .scale(scale.value)
                        .shadow(elevation, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.Red)
                ) {
                    Text(item.title)
                }
            }
        }
    }
}