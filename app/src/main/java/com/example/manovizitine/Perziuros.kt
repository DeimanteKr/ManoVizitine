package com.example.manovizitine

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.manovizitine.ui.theme.ManoVizitineTheme

@Preview(name = "Telefonas", device = "spec:width=411dp,height=891dp", showSystemUi = true)
@Preview(name = "Telefonas (gulsčias)", device = "spec:width=891dp,height=411dp", showSystemUi = true)
@Preview(name = "Mažas telefonas", device = "spec:width=320dp,height=640dp", showSystemUi = true)
@Preview(name = "Planšetė", device = "spec:width=1280dp,height=800dp,dpi=240", showSystemUi = true)
@Preview(name = "Planšetė (stačia)", device = "spec:width=800dp,height=1280dp,dpi=240", showSystemUi = true)
annotation class VisuEkranuPerziura

@VisuEkranuPerziura
@Composable
fun PradziosPerziura() {
    ManoVizitineTheme {
        VizitinesProgramele()
    }
}

@VisuEkranuPerziura
@Composable
fun CvPerziura() {
    ManoVizitineTheme {
        VizitinesProgramele(pradinisEkranas = CV)
    }
}

@VisuEkranuPerziura
@Composable
fun PortfolioPerziura() {
    ManoVizitineTheme {
        VizitinesProgramele(pradinisEkranas = PORTFOLIO)
    }
}

@VisuEkranuPerziura
@Composable
fun KontaktuPerziura() {
    ManoVizitineTheme {
        VizitinesProgramele(pradinisEkranas = KONTAKTAI)
    }
}

@Preview(name = "Telefonas (English)", locale = "en", device = "spec:width=411dp,height=891dp", showSystemUi = true)
@Composable
fun AngliskaPerziura() {
    ManoVizitineTheme {
        VizitinesProgramele()
    }
}
