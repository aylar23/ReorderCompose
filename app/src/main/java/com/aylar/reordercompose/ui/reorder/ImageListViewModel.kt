package com.aylar.reordercompose.ui.reorder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aylar.reordercompose.reorderable.ItemPosition
import kotlin.random.Random


class ImageListViewModel : ViewModel() {
    val headerImage = "https://picsum.photos/seed/compose${Random.nextInt(Int.MAX_VALUE)}/400/200"
    val footerImage = "https://picsum.photos/seed/compose${Random.nextInt(Int.MAX_VALUE)}/400/200"
    var images by mutableStateOf(List(20) { "https://picsum.photos/seed/compose$it/200/300" })
    fun onMove(from: ItemPosition, to: ItemPosition) {
        images = images.toMutableList().apply {
            add(images.indexOfFirst { it == to.key }, removeAt(images.indexOfFirst { it == from.key }))
        }
    }

    fun canDragOver(draggedOver: ItemPosition, dragging: ItemPosition) = images.any { it == draggedOver.key }
}