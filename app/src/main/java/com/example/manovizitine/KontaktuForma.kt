package com.example.manovizitine

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

// išsiunčiami el. paštu per kitą programėlę (Intent).
@Composable
fun KontaktuForma(duomenys: VizitinesViewModel) {
    val context = LocalContext.current

    OutlinedTextField(
        value = duomenys.kontaktai,
        onValueChange = { tekstas -> duomenys.kontaktai = tekstas },
        label = { Text(stringResource(R.string.contact_details_label)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = duomenys.klausimas,
        onValueChange = { tekstas -> duomenys.klausimas = tekstas },
        label = { Text(stringResource(R.string.contact_question_label)) },
        minLines = 4,
        modifier = Modifier.fillMaxWidth()
    )

    Button(
        onClick = {
            siustiLaiska(context, duomenys.kontaktai, duomenys.klausimas)
        },
        enabled = duomenys.kontaktai.isNotBlank() && duomenys.klausimas.isNotBlank(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.contact_send))
    }
}

fun siustiLaiska(context: Context, kontaktai: String, klausimas: String) {
    val gavejas = context.getString(R.string.profile_email)
    val tema = context.getString(R.string.contact_email_subject)
    val tekstas = context.getString(R.string.contact_email_body, kontaktai.trim(), klausimas.trim())

    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(gavejas))
    intent.putExtra(Intent.EXTRA_SUBJECT, tema)
    intent.putExtra(Intent.EXTRA_TEXT, tekstas)

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, R.string.contact_no_email_app, Toast.LENGTH_LONG).show()
    }
}
