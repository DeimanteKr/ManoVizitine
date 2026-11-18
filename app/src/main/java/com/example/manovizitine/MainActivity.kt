package com.example.manovizitine

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manovizitine.ui.theme.ManoVizitineTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ManoVizitineTheme {
                VizitinesProgramele()
            }
        }
    }
}

// isimena koks ekranas šiuo metu rodomas
const val PRADZIA = "pradzia"
const val CV = "cv"
const val PORTFOLIO = "portfolio"
const val KONTAKTAI = "kontaktai"

@Composable
fun VizitinesProgramele(pradinisEkranas: String = PRADZIA) {
    // rememberSaveable – rodomas ekranas išlieka pasukus telefoną
    var ekranas by rememberSaveable { mutableStateOf(pradinisEkranas) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // safeDrawing – turinys nelenda po sistemos juostomis ir kameros išpjova
        contentWindowInsets = WindowInsets.safeDrawing
    ) { vidinesParastes ->

        // BoxWithConstraints leidžia sužinoti, kiek vietos turime ekrane
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(vidinesParastes)
        ) {
            if (maxWidth < 600.dp) {
                TelefonoIsdestymas(
                    ekranas = ekranas,
                    keistiEkrana = { naujas -> ekranas = naujas }
                )
            } else {
                PlatausEkranoIsdestymas(
                    ekranas = ekranas,
                    keistiEkrana = { naujas -> ekranas = naujas }
                )
            }
        }
    }
}

@Composable
fun TelefonoIsdestymas(ekranas: String, keistiEkrana: (String) -> Unit) {
    BackHandler(enabled = ekranas != PRADZIA) {
        keistiEkrana(PRADZIA)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (ekranas == PRADZIA) {
            KalbosPerjungiklis()
            Profilis()
            MeniuMygtukai(pazymetas = null, keistiEkrana = keistiEkrana)
        } else {
            PasirinktasTurinys(ekranas = ekranas)
            OutlinedButton(
                onClick = { keistiEkrana(PRADZIA) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.back))
            }
        }
    }
}

@Composable
fun PlatausEkranoIsdestymas(ekranas: String, keistiEkrana: (String) -> Unit) {
    var rodomas = ekranas
    if (rodomas == PRADZIA) {
        rodomas = CV
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Kairysis stulpelis
        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Profilis()
            MeniuMygtukai(pazymetas = rodomas, keistiEkrana = keistiEkrana)
            KalbosPerjungiklis()
        }

        VerticalDivider()

        // Dešinysis stulpelis
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PasirinktasTurinys(ekranas = rodomas)
            }
        }
    }
}

@Composable
fun PasirinktasTurinys(ekranas: String) {
    val duomenys: VizitinesViewModel = viewModel()

    if (ekranas == CV) {
        Antraste(stringResource(R.string.cv_title))
        PagrindinisTekstas(stringResource(R.string.cv_content))
        PapildomiIrasai(
            saugojimoRaktas = "cv_irasai",
            naujasIrasas = duomenys.naujasCvIrasas,
            keistiNaujaIrasa = { tekstas -> duomenys.naujasCvIrasas = tekstas }
        )
    }

    if (ekranas == PORTFOLIO) {
        Antraste(stringResource(R.string.portfolio_title))
        PagrindinisTekstas(stringResource(R.string.portfolio_content))
        PortfolioProjektai()
        PapildomiIrasai(
            saugojimoRaktas = "portfolio_irasai",
            naujasIrasas = duomenys.naujasPortfolioIrasas,
            keistiNaujaIrasa = { tekstas -> duomenys.naujasPortfolioIrasas = tekstas }
        )
    }

    if (ekranas == KONTAKTAI) {
        Antraste(stringResource(R.string.contact_title))
        KontaktuForma(duomenys)
    }
}

@Composable
fun Antraste(tekstas: String) {
    Text(
        text = tekstas,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun PagrindinisTekstas(tekstas: String) {
    Text(
        text = tekstas,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun MeniuMygtukai(pazymetas: String?, keistiEkrana: (String) -> Unit) {
    MeniuMygtukas(
        tekstas = stringResource(R.string.show_cv),
        uzpildytas = pazymetas == null || pazymetas == CV,
        paspaudus = { keistiEkrana(CV) }
    )

    MeniuMygtukas(
        tekstas = stringResource(R.string.show_portfolio),
        uzpildytas = pazymetas == null || pazymetas == PORTFOLIO,
        paspaudus = { keistiEkrana(PORTFOLIO) }
    )

    MeniuMygtukas(
        tekstas = stringResource(R.string.contact_me),
        uzpildytas = pazymetas == null || pazymetas == KONTAKTAI,
        paspaudus = { keistiEkrana(KONTAKTAI) }
    )
}

@Composable
fun MeniuMygtukas(tekstas: String, uzpildytas: Boolean, paspaudus: () -> Unit) {
    if (uzpildytas) {
        Button(onClick = paspaudus, modifier = Modifier.fillMaxWidth()) {
            Text(tekstas)
        }
    } else {
        OutlinedButton(onClick = paspaudus, modifier = Modifier.fillMaxWidth()) {
            Text(tekstas)
        }
    }
}

@Composable
fun KalbosPerjungiklis() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("lt"))
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.language_lt))
        }

        OutlinedButton(
            onClick = {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.language_en))
        }
    }
}
