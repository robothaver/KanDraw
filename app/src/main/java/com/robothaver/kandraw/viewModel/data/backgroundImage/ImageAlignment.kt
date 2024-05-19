package com.robothaver.kandraw.viewModel.data.backgroundImage

class ImageAlignments {
    companion object {
        val TopStart: ImageAlignment = ImageAlignment(HorizontalAlignment.Start, VerticalAlignment.Top)
        val TopCenter: ImageAlignment = ImageAlignment(
            HorizontalAlignment.Center,
            VerticalAlignment.Top
        )
        val TopEnd: ImageAlignment = ImageAlignment(HorizontalAlignment.End, VerticalAlignment.Top)

        val CenterStart: ImageAlignment = ImageAlignment(
            HorizontalAlignment.Start,
            VerticalAlignment.Center
        )
        val Center: ImageAlignment = ImageAlignment(
            HorizontalAlignment.Center,
            VerticalAlignment.Center
        )
        val CenterEnd: ImageAlignment = ImageAlignment(
            HorizontalAlignment.End,
            VerticalAlignment.Center
        )

        val BottomStart: ImageAlignment = ImageAlignment(
            HorizontalAlignment.Start,
            VerticalAlignment.Bottom
        )
        val BottomCenter: ImageAlignment = ImageAlignment(
            HorizontalAlignment.Center,
            VerticalAlignment.Bottom
        )
        val BottomEnd: ImageAlignment = ImageAlignment(
            HorizontalAlignment.End,
            VerticalAlignment.Bottom
        )
    }
}

data class ImageAlignment(
    val horizontal: HorizontalAlignment,
    val vertical: VerticalAlignment
)

enum class VerticalAlignment {
    Top, Center, Bottom
}

enum class HorizontalAlignment {
    Start, Center, End
}