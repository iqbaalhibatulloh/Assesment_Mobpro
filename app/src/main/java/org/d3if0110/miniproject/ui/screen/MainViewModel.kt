package org.d3if0110.miniproject.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if0110.miniproject.model.Art
import org.d3if0110.miniproject.network.ApiStatus
import org.d3if0110.miniproject.network.ArtApi
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Art>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = ArtApi.service.getArt(userId).data
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure retrieve data: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(email: String, judul: String, artis: String, jenisKarya: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ArtApi.service.postArt(
                    email,
                    judul.toRequestBody("text/plain".toMediaTypeOrNull()),
                    artis.toRequestBody("text/plain".toMediaTypeOrNull()),
                    jenisKarya.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody(),
                )

                if (result.status == "success")
                    retrieveData(email)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun deleteData(email: String, hewanId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ArtApi.service.deleteArt(
                    email,
                    hewanId
                )

                if (result.status == "success")
                    retrieveData(email)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun clearMessage() { errorMessage.value = null }
}
