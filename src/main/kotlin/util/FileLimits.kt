package util

import java.io.File

/** Maximum source/data file size accepted by the CLI and import paths (16 MiB). */
const val MAX_SOURCE_FILE_BYTES: Long = 16L * 1024 * 1024

fun File.readTextLimited(maxBytes: Long = MAX_SOURCE_FILE_BYTES): String {
    val len = length()
    require(len <= maxBytes) {
        "Refusing to read $absolutePath: size $len bytes exceeds limit of $maxBytes bytes"
    }
    return readText()
}
