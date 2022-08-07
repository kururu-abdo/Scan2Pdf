package com.kururu.scan2pdf.app.viewmodel




import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kururu.scan2pdf.app.data.model.FileDetails
import com.kururu.scan2pdf.app.data.repositories.AppRepository
import com.pspdfkit.document.PdfDocument
import com.pspdfkit.document.PdfDocumentLoader
import com.pspdfkit.document.download.DownloadJob
import com.pspdfkit.document.download.DownloadRequest
import com.pspdfkit.document.download.source.AssetDownloadSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class PdfViewModel  @Inject constructor (application: Application , repository: AppRepository) : AndroidViewModel(application) {

    // The list of PDFs in our assets folder
    private val assetsToLoad = listOf(
        "Annotations.pdf",
        "Aviation.pdf",
        "Calculator.pdf",
        "Classbook.pdf",
        "Construction.pdf",
        "Student.pdf",
        "Teacher.pdf",
        "The-Cosmic-Context-for-Life.pdf"
    )
    private val _assetsToLoad2 =repository.allScans.asLiveData()
    val assetsToLoad2:  List<FileDetails> = _assetsToLoad2.value!!
    private val mutableState = MutableStateFlow(PdfState())
    val state: StateFlow<PdfState> = mutableState

    fun loadPdfs() = viewModelScope.launch(Dispatchers.IO) {

        // Indicate that we are now loading
        mutableState.mutate { copy(loading = true) }

        val context = getApplication<Application>().applicationContext

        val pdfDocuments = assetsToLoad2
            .map { extractPdf(context, it.fileRef) }
            .map { it.toUri() to loadPdf(context, it.toUri()) }
            .toMap()

        // Stop loading and add the PDFs to our state
        mutableState.mutate {
            copy(
                loading = false,
                documents = pdfDocuments
            )
        }
    }

    fun openDocument(uri: Uri) {
        mutableState.mutate {
            copy(selectedDocumentUri = uri)
        }
    }

    fun closeDocument() {
        mutableState.mutate {
            copy(selectedDocumentUri = null)
        }
    }

    private suspend fun loadPdf(context: Context, uri: Uri) = suspendCoroutine<PdfDocument> { continuation ->
        PdfDocumentLoader
            .openDocumentAsync(context, uri)
            .subscribe(continuation::resume, continuation::resumeWithException)
    }

    private suspend fun extractPdf(context: Context, assetPath: String) = suspendCoroutine<File> { continuation ->
        val outputFile = File(context.filesDir, assetPath)
        val request = DownloadRequest.Builder(context)
            .source(AssetDownloadSource(context, assetPath))
            .outputFile(outputFile)
            .overwriteExisting(true)
            .build()

        val job = DownloadJob.startDownload(request)
        job.setProgressListener(
            object : DownloadJob.ProgressListenerAdapter() {
                override fun onComplete(output: File) {
                    continuation.resume(output)
                }

                override fun onError(exception: Throwable) {
                    super.onError(exception)
                    continuation.resumeWithException(exception)
                }
            }
        )
    }

    private fun <T> MutableStateFlow<T>.mutate(mutateFn: T.() -> T) {
        value = value.mutateFn()
    }
}

data class PdfState(
    val loading: Boolean = false,
    val documents: Map<Uri, PdfDocument> = emptyMap(),
    val selectedDocumentUri: Uri? = null
)