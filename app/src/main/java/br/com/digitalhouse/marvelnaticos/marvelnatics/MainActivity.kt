package br.com.digitalhouse.marvelnaticos.marvelnatics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.CustomToolbar
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.MarvelnaticsTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelnaticsTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {CustomToolbar()}, bodyContent = {})
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MarvelnaticsTheme {
        CustomToolbar()
    }
}