package com.example.manovizitine

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

// Portfolio vaizdai kraunami iš interneto su Coil.
// GitHub https://github.com/gMieFE/portfolio-images
const val VAIZDU_ADRESAS = "https://raw.githubusercontent.com/gMieFE/portfolio-images/main/"

// Vienas portfolio projektas.
// telefonoEkranai = true, kai vaizdai yra stačios telefono ekrano nuotraukos.
data class Projektas(
    val pavadinimas: Int,
    val aprasymas: Int,
    val vaizdai: List<String>,
    val telefonoEkranai: Boolean = false
)

val projektai = listOf(
    Projektas(
        pavadinimas = R.string.portfolio1_title,
        aprasymas = R.string.portfolio1_description,
        vaizdai = listOf("portfolio1_home.png", "portfolio1_about.png", "portfolio1_work.png")
    ),
    Projektas(
        pavadinimas = R.string.portfolio2_title,
        aprasymas = R.string.portfolio2_description,
        vaizdai = listOf(
            "portfolio2_home.jpg",
            "portfolio2_product.jpg",
            "portfolio2_cart.png",
            "portfolio2_admin.png"
        )
    ),
    Projektas(
        pavadinimas = R.string.portfolio3_title,
        aprasymas = R.string.portfolio3_description,
        vaizdai = listOf("portfolio3_gallery.webp", "portfolio3_detail.webp")
    ),
    Projektas(
        pavadinimas = R.string.portfolio4_title,
        aprasymas = R.string.portfolio4_description,
        vaizdai = listOf(
            "portfolio4_home.jpg",
            "portfolio4_list.jpg",
            "portfolio4_create.jpg",
            "portfolio4_author.jpg"
        ),
        telefonoEkranai = true
    )
)

@Composable
fun PortfolioProjektai() {
    for (projektas in projektai) {
        Text(
            text = stringResource(projektas.pavadinimas),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = stringResource(projektas.aprasymas),
            modifier = Modifier.fillMaxWidth()
        )

        val aukstis = if (projektas.telefonoEkranai) 400.dp else 220.dp

        for (failas in projektas.vaizdai) {
            InternetoVaizdas(
                adresas = VAIZDU_ADRESAS + failas,
                aprasymas = stringResource(projektas.pavadinimas),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(aukstis)
            )
        }
    }
}

@Composable
fun InternetoVaizdas(adresas: String, aprasymas: String, modifier: Modifier) {
    // Android Studio peržiūra (Preview) neturi interneto – ten rodome tik pilką vaizdą
    if (LocalInspectionMode.current) {
        Image(
            painter = painterResource(R.drawable.vaizdas_kraunamas),
            contentDescription = aprasymas,
            modifier = modifier
        )
        return
    }

    var nepavyko by remember { mutableStateOf(false) }
    var bandymas by remember { mutableIntStateOf(0) }

    key(bandymas) {
        AsyncImage(
            model = adresas,
            contentDescription = aprasymas,
            placeholder = painterResource(R.drawable.vaizdas_kraunamas),
            error = painterResource(R.drawable.vaizdas_klaida),
            onError = { nepavyko = true },
            contentScale = ContentScale.Fit,
            modifier = modifier
        )
    }

    if (nepavyko) {
        Text(
            text = stringResource(R.string.image_load_failed),
            color = MaterialTheme.colorScheme.error
        )

        OutlinedButton(
            onClick = {
                nepavyko = false
                bandymas = bandymas + 1
            }
        ) {
            Text(stringResource(R.string.retry))
        }
    }
}
