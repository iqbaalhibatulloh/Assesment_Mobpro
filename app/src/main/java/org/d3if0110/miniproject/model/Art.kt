package org.d3if0110.miniproject.model

import com.squareup.moshi.Json

data class Art(
    val id: String,
    val judul: String,
    val artis: String,
    @Json(name = "jenis_karya") val jenisKarya: String,
    @Json(name = "image_id") val imageId: String,
)