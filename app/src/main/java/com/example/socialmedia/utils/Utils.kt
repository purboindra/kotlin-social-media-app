package com.example.socialmedia.utils

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache

fun imageLoader(context: Context): ImageLoader {
    val imageLoader = ImageLoader.Builder(context).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.25).build()
    }.diskCache {
        DiskCache.Builder().directory(context.cacheDir.resolve("image_cache"))
            .build()
    }.build()
    return imageLoader
}