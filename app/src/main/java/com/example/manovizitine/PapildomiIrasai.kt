package com.example.manovizitine

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

// saugojimoRaktas – pagal jį atskiriami CV ("cv_irasai") ir portfolio ("portfolio_irasai") įrašai.
@Composable
fun PapildomiIrasai(
    saugojimoRaktas: String,
    naujasIrasas: String,
    keistiNaujaIrasa: (String) -> Unit
) {
    val context = LocalContext.current
    val saugykla = remember { IrasuSaugykla(context) }

    // Atidarius ekraną įrašai nuskaitomi iš atminties
    var irasai by remember(saugojimoRaktas) {
        mutableStateOf(saugykla.gautiIrasus(saugojimoRaktas))
    }

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
    }

    for (i in irasai.indices) {
        Text(
            text = "${i + 1}. ${irasai[i]}",
            modifier = Modifier.fillMaxWidth()
        )
    }

    OutlinedTextField(
        value = naujasIrasas,
        onValueChange = keistiNaujaIrasa,
        label = { Text(stringResource(R.string.new_entry_label)) },
        minLines = 3,
        modifier = Modifier.fillMaxWidth()
    )

    Button(
        onClick = {
            irasai = irasai + naujasIrasas.trim()
            saugykla.issaugotiIrasus(saugojimoRaktas, irasai)

            keistiNaujaIrasa("")
        },
        enabled = naujasIrasas.isNotBlank(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.save_entry))
    }
}
