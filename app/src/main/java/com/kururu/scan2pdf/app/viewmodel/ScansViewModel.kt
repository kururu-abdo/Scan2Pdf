package com.kururu.scan2pdf.app.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.lifecycle.*
import com.kururu.scan2pdf.app.data.model.FileDetails
import com.kururu.scan2pdf.app.data.repositories.AppRepository
import com.pspdfkit.document.PdfDocumentLoader
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.nio.file.Files.copy
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel

class ScansViewModel   @Inject constructor(private val repository: AppRepository):ViewModel() {
    val allScans: LiveData<List<FileDetails>> = repository.allScans.asLiveData()


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: FileDetails) = viewModelScope.launch {
        repository.insert(word)
    }

    fun remove(fileId: Int) = viewModelScope.launch {
        repository.remove(fileId)
    }

//    private val mutableState = MutableStateFlow(State())
//    val state: StateFlow<State> = mutableState
//
//    private fun <T> MutableStateFlow<T>.mutate(mutateFn: T.() -> T) {
//        value = value.mutateFn()
//    }
}
//    private val assetsToLoad = listOf(
//        "Annotations.pdf",
//        "Aviation.pdf",
//        "Calculator.pdf",
//        "Classbook.pdf",
//        "Construction.pdf",
//        "Student.pdf",
//        "Teacher.pdf",
//        "The-Cosmic-Context-for-Life.pdf"
//    )
//    fun loadPdfs() = viewModelScope.launch(Dispatchers.IO) {
//
//        // Mutate the state to indicate that we're now loading.
//        mutableState.mutate { copy(loading = true) }
//
//        val context = getApplication<Application>().applicationContext
//
//        // Each map here is running a suspended function.
//        val pdfDocuments = assetsToLoad
//            .map { extractPdf(context, it) }
//            .map { it.toUri to loadPdf(context, it.toUri()) }
//            .toMap()
//
//        // Stop loading and add the PDFs to the state.
//        mutableState.mutate {
//            copy(
//                loading = false,
//                documents = pdfDocuments
//            )
//        }
//    }
//
//    @SuppressLint("CheckResult")
//    private suspend fun loadPdf(context: Context, uri: Uri) = suspendCoroutine<PdfDocument> { continuation ->
//        PdfDocumentLoader
//            .openDocumentAsync(context, uri)
//            .subscribe(continuation::resume, continuation::resumeWithException)
//    }
//}
//
//data class State(
//    val loading: Boolean = false,
//    val documents: Map<Uri, PdfDocument> = emptyMap(),
//    val selectedDocumentUri: Uri? = null
//)
//
