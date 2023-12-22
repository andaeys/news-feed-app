package andaeys.io.newsapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

const val EXTRA_URL = "URL"
class NewsDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(EXTRA_URL)
        setContent {
            WebViewScreen(url = url?:"")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(url: String) {
    var progress by remember { mutableStateOf(0) }
    var isProgressBarVisible by remember { mutableStateOf(true) }

    DisposableEffect(progress) {
        if (progress == 100) {
            isProgressBarVisible = false
        }
        onDispose { /* cleanup logic */ }
    }

    Scaffold{
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val webView = WebView(LocalContext.current).apply {
                settings.javaScriptEnabled = true
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        progress = newProgress
                    }
                }
            }

            if (isProgressBarVisible) {
                LinearProgressIndicator(
                    progress = if (progress in 1 until 100) {
                        progress / 100f
                    } else {
                        1f
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AndroidView(
                factory = { webView },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) { wv ->
                wv.loadUrl(url)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}