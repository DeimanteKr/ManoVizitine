package com.example.manovizitine
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.platform.LocalContext

import android.content.Context
import androidx.compose.runtime.remember
import org.json.JSONArray

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

@Composable
fun VizitinesProgramele() {
    var ekranas by rememberSaveable { mutableStateOf("pradzia") }

    BackHandler(enabled = ekranas != "pradzia") {
        ekranas = "pradzia"
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { vidinesParastes ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vidinesParastes),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (ekranas) {
                    "pradzia" -> {
                        OutlinedButton(
                            onClick = {
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags("lt")
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.language_lt))
                        }

                        OutlinedButton(
                            onClick = {
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags("en")
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.language_en))
                        }
                        Image(
                            painter = painterResource(R.drawable.profilio_nuotrauka),
                            contentDescription = stringResource(
                                R.string.profile_photo_description
                            ),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                        )

                        Text(
                            text = stringResource(R.string.profile_name),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = stringResource(R.string.profile_description),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = stringResource(R.string.profile_email),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { ekranas = "cv" },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.show_cv))
                        }

                        Button(
                            onClick = { ekranas = "portfolio" },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.show_portfolio))
                        }

                        OutlinedButton(
                            onClick = { ekranas = "kontaktai" },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.contact_me))
                        }
                    }

                    "cv" -> {
                        IrasuEkranas(
                            saugojimoRaktas = "cv_irasai",
                            pavadinimas = stringResource(R.string.cv_title),
                            pradinisTurinys = stringResource(R.string.cv_content),
                            grizti = { ekranas = "pradzia" }
                        )
                    }

                    "portfolio" -> {
                        IrasuEkranas(
                            saugojimoRaktas = "portfolio_irasai",
                            pavadinimas = stringResource(R.string.portfolio_title),
                            pradinisTurinys = stringResource(R.string.portfolio_content),
                            grizti = { ekranas = "pradzia" }
                        )
                    }

                    "kontaktai" -> {
                        KontaktuForma(
                            grizti = { ekranas = "pradzia" }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InformacijosEkranas(
    pavadinimas: String,
    turinys: String,
    grizti: () -> Unit
) {
    Text(
        text = pavadinimas,
        style = MaterialTheme.typography.headlineMedium
    )

    Text(
        text = turinys,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge
    )

    OutlinedButton(
        onClick = grizti,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.back))
    }
}
@Composable
fun KontaktuForma(grizti: () -> Unit) {
    val context = LocalContext.current

    var kontaktai by rememberSaveable { mutableStateOf("") }
    var klausimas by rememberSaveable { mutableStateOf("") }

    val gavejas = stringResource(R.string.profile_email)
    val tema = stringResource(R.string.contact_email_subject)
    val klaidosTekstas = stringResource(R.string.contact_no_email_app)
    val laiskoTekstas = stringResource(
        R.string.contact_email_body,
        kontaktai.trim(),
        klausimas.trim()
    )

    Text(
        text = stringResource(R.string.contact_title),
        style = MaterialTheme.typography.headlineMedium
    )

    OutlinedTextField(
        value = kontaktai,
        onValueChange = { kontaktai = it },
        label = {
            Text(stringResource(R.string.contact_details_label))
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = klausimas,
        onValueChange = { klausimas = it },
        label = {
            Text(stringResource(R.string.contact_question_label))
        },
        minLines = 4,
        modifier = Modifier.fillMaxWidth()
    )

    Button(
        onClick = {
            val adresas = Uri.parse(
                "mailto:${Uri.encode(gavejas)}" +
                        "?subject=${Uri.encode(tema)}" +
                        "&body=${Uri.encode(laiskoTekstas)}"
            )

            val intent = Intent(Intent.ACTION_SENDTO, adresas)

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    klaidosTekstas,
                    Toast.LENGTH_LONG
                ).show()
            }
        },
        enabled = kontaktai.isNotBlank() && klausimas.isNotBlank(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.contact_send))
    }

    OutlinedButton(
        onClick = grizti,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.back))
    }
}
@Composable
fun IrasuEkranas(
    saugojimoRaktas: String,
    pavadinimas: String,
    pradinisTurinys: String,
    grizti: () -> Unit
) {
    val context = LocalContext.current

    val atmintis = remember(context) {
        context.getSharedPreferences(
            "vizitines_duomenys",
            Context.MODE_PRIVATE
        )
    }

    var irasai by remember(saugojimoRaktas) {
        val issaugoti = atmintis.getString(
            saugojimoRaktas,
            "[]"
        ) ?: "[]"

        val masyvas = JSONArray(issaugoti)

        mutableStateOf(
            List(masyvas.length()) { indeksas ->
                masyvas.getString(indeksas)
            }
        )
    }

    var naujasIrasas by rememberSaveable(saugojimoRaktas) {
        mutableStateOf("")
    }

    Text(
        text = pavadinimas,
        style = MaterialTheme.typography.headlineMedium
    )

    Text(
        text = pradinisTurinys,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge
    )

    Text(
        text = stringResource(R.string.added_entries_title),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleMedium
    )

    if (irasai.isEmpty()) {
        Text(
            text = stringResource(R.string.no_added_entries),
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        irasai.forEachIndexed { indeksas, irasas ->
            Text(
                text = "${indeksas + 1}. $irasas",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    OutlinedTextField(
        value = naujasIrasas,
        onValueChange = { naujasIrasas = it },
        label = {
            Text(stringResource(R.string.new_entry_label))
        },
        minLines = 3,
        modifier = Modifier.fillMaxWidth()
    )

    Button(
        onClick = {
            val atnaujintiIrasai = irasai + naujasIrasas.trim()

            val masyvas = JSONArray()
            atnaujintiIrasai.forEach { irasas ->
                masyvas.put(irasas)
            }

            atmintis.edit()
                .putString(saugojimoRaktas, masyvas.toString())
                .apply()

            irasai = atnaujintiIrasai
            naujasIrasas = ""
        },
        enabled = naujasIrasas.isNotBlank(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.save_entry))
    }

    OutlinedButton(
        onClick = grizti,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.back))
    }
}