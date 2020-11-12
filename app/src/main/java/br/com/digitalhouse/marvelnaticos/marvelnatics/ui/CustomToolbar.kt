package br.com.digitalhouse.marvelnaticos.marvelnatics.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.ui.MarvelnaticsTheme

class CustomToolbar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TopAppBar(
                backgroundColor = Color.Transparent,
                title = {
                    Row(
                        Image(asset = )
                    )
                })
        }
    }
}
