package com.robothaver.kandraw.viewModel.data

data class ImageSaveOptions(
    val createAlbum: Boolean = false,
    val includeBackgroundImage: Boolean = true,
    val includeGrid: Boolean = false,
    val transparentBackground: Boolean = false,
)
