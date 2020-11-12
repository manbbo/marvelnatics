package br.com.digitalhouse.marvelnaticos.marvelnatics.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ImagePainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.digitalhouse.marvelnaticos.marvelnatics.R


@Composable
@Preview
fun CustomToolbar (){
    TopAppBar(
        backgroundColor = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                ) {
                    Surface(color = primaryColor, modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxSize().align(Alignment.Center),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ORGANIZAR COLEÇÃO",
                                style = TextStyle(color = white, fontSize = TextUnit(10)),
                                modifier = Modifier.clickable(
                                    onClick = {}
                                ).padding(end = 20.dp)
                            )
                            Text(text = "|", style = TextStyle(color = white, fontSize = TextUnit(10)), modifier = Modifier.padding(end = 20.dp))
                            Text(text = "FAVORITOS",
                                style = TextStyle(color = white, fontSize = TextUnit(10)),
                                modifier = Modifier.clickable(
                                    onClick = {}
                                ).padding(end = 20.dp))
                        }
                    }
                }

                Image(
                    modifier = Modifier.padding(3.dp),
                    asset = imageResource(id = R.drawable.logoshort),
                )
            }
        },
    )
}
