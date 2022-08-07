package com.kururu.scan2pdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class PdfActivity : AppCompatActivity() {
    private lateinit var fileName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        val pdfLayout = findViewById<PDFView>(R.id.pdfViewLayout)
        val intent = intent

        if (intent.extras != null) {
            fileName = intent.extras?.getString("fileName").toString()
        }

        try {
            pdfLayout.fromUri(File("${applicationContext.getExternalFilesDir(null)}/$fileName.pdf").toUri())

                .load()
        } catch (e: Exception) {

        }
    }
}