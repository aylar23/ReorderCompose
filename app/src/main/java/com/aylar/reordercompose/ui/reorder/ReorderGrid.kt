package com.aylar.reordercompose.ui.reorder

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aylar.reordercompose.reorderable.ReorderableItem
import com.aylar.reordercompose.reorderable.detectReorderAfterLongPress
import com.aylar.reordercompose.reorderable.rememberReorderableLazyGridState
import com.aylar.reordercompose.reorderable.reorderable

@Composable
fun ReorderGrid(vm: ReorderListViewModel = viewModel()) {
    Column {
        HorizontalGrid(
            vm = vm,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        VerticalGrid(vm = vm)
    }
}

@Composable
private fun HorizontalGrid(
    vm: ReorderListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = rememberReorderableLazyGridState(onMove = vm::moveCat)
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        state = state.gridState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .reorderable(state)
            .height(200.dp)
            .detectReorderAfterLongPress(state)
    ) {
        items(vm.cats, { it.key }) { item ->
            ReorderableItem(state, item.key) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .shadow(elevation.value)
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(item.title)
                }
            }
        }
    }
}

@Composable
private fun VerticalGrid(
    vm: ReorderListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = rememberReorderableLazyGridState(onMove = vm::moveDog, canDragOver = vm::isDogDragEnabled)
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        state = state.gridState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.reorderable(state)
    ) {
        items(vm.dogs, { it.key }) { item ->
            if (item.isLocked) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Text(item.title)
                }
            } else {
                ReorderableItem(state, item.key) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 8.dp else 0.dp)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .detectReorderAfterLongPress(state)
                            .shadow(elevation.value)
                            .aspectRatio(1f)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(item.title)
                    }
                }
            }
        }
    }
}