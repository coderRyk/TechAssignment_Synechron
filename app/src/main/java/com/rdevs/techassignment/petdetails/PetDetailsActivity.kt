package com.rdevs.techassignment.petdetails

import Pet
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.rdevs.techassignment.R
import com.rdevs.techassignment.utils.UiUtils

class PetDetailsActivity : AppCompatActivity() {

    var pbContainer: LinearLayout? = null

    companion object {
        const val KEY_EXTRA_PET = "EXTRA_PET"
    }

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_details_activity);

        val pet = intent.extras?.getParcelable<Pet>(KEY_EXTRA_PET);
        if (pet != null) {
            title = pet.title
        }

        pbContainer = findViewById(R.id.pbContainer)
        webView = findViewById(R.id.wvPetDetails)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true;
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.isScrollbarFadingEnabled = false
        webView.webViewClient = MyWebViewClient()

        Log.d("PetDetailsActivity", " $pet.content_url")
        pet?.content_url?.let {
            webView.loadUrl(it) }
            ?: finish()
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            url?.let { view?.loadUrl(it) }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            Log.d("PetDetailsActivity", "onPageFinished ")
            UiUtils.toggleVisibility(webView, true)
            UiUtils.toggleVisibility(pbContainer, false)
            super.onPageFinished(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            Log.d("PetDetailsActivity", "onPageStarted ")
            UiUtils.toggleVisibility(webView, false)
            UiUtils.toggleVisibility(pbContainer, true)
            super.onPageStarted(view, url, favicon)
        }

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            Log.d("PetDetailsActivity", "onReceivedError $errorCode $description")
        }

    }

}