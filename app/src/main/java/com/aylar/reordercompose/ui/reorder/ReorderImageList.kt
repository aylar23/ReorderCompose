package com.aylar.reordercompose.ui.reorder

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.aylar.reordercompose.reorderable.ReorderableItem
import com.aylar.reordercompose.reorderable.detectReorder
import com.aylar.reordercompose.reorderable.rememberReorderableLazyListState
import com.aylar.reordercompose.reorderable.reorderable

@Composable
fun ReorderImageList(
    modifier: Modifier = Modifier,
    vm: ImageListViewModel = viewModel(),
) {
    val state = rememberReorderableLazyListState(onMove = vm::onMove, canDragOver = vm::canDragOver)
    LazyColumn(
        state = state.listState,
        modifier = modifier
            .then(Modifier.reorderable(state))
    ) {
        item {
            HeaderFooter("Header", vm.headerImage)
        }
        items(vm.images, { it }) { item ->
            ReorderableItem(state, item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 8.dp else 0.dp)
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)

                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            Icons.Default.List,
                            "",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
                            modifier = Modifier.detectReorder(state)
                        )
                        Image(
                            painter = rememberAsyncImagePainter(item),
                            contentDescription = null,
                            modifier = Modifier.size(128.dp)
                        )
                        Text(
                            text = item,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Divider()
                }
            }
        }
        item {
            HeaderFooter("Footer", vm.footerImage)
        }
    }
}

@Composable
private fun HeaderFooter(title: String, url: String) {
    Box(modifier = Modifier.height(128.dp).fillMaxWidth()) {
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            title,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}