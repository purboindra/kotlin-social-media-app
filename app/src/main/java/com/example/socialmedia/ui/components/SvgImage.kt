package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import coil3.svg.SvgImage
import com.example.socialmedia.R
import java.io.File

@Composable
fun SvgImage(
    svgResource: Int,
    modifier: Modifier? = null,
    contentDescription: String? = null
) {
    
    val context = LocalContext.current
    val svgUri = remember {
        val file = File(context.cacheDir, "svg_image_${svgResource}.svg")
        if (!file.exists()) {
            context.resources.openRawResource(svgResource).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
        file.toURI()
    }
    
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(svgUri.toString())
            .decoderFactory(SvgDecoder.Factory())
            .build(),
        contentDescription = contentDescription,
        modifier = modifier ?: Modifier.size(32.dp)
    )
    
}