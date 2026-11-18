package com.example.manovizitine

import android.content.ActivityNotFoundException
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.io.File

// Failas, į kurį išsaugoma kamera padaryta nuotrauka (programėlės vidinėje atmintyje)
const val NUOTRAUKOS_FAILAS = "mano_nuotrauka.jpg"

@Composable
fun Profilis() {
    val context = LocalContext.current

    val perziura = LocalInspectionMode.current
    var nuotrauka by remember {
        mutableStateOf(if (perziura) null else nuskaitytiNuotrauka(context))
    }

    // Kameros programėlės iškvietimas (Intent MediaStore.ACTION_IMAGE_CAPTURE).
    // TakePicturePreview grąžina nedidelę nuotrauką (Bitmap) – profilio nuotraukai jos pakanka.
    val kamera = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { nufotografuota ->
        if (nufotografuota != null) {
            issaugotiNuotrauka(context, nufotografuota)
            nuotrauka = nufotografuota
        }
    }

    val dabartineNuotrauka = nuotrauka
    if (dabartineNuotrauka != null) {
        Image(
            bitmap = dabartineNuotrauka.asImageBitmap(),
            contentDescription = stringResource(R.string.profile_photo_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )
    } else {
        Image(
            painter = painterResource(R.drawable.profilio_nuotrauka),
            contentDescription = stringResource(R.string.profile_photo_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )
    }

    OutlinedButton(
        onClick = {
            try {
                kamera.launch(null)
            } catch (e: ActivityNotFoundException) {
                // Telefone nėra kameros programėlės
                Toast.makeText(context, R.string.no_camera_app, Toast.LENGTH_LONG).show()
            }
        }
    ) {
        Text(stringResource(R.string.take_photo))
    }

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
}

fun issaugotiNuotrauka(context: Context, nuotrauka: Bitmap) {
    val failas = File(context.filesDir, NUOTRAUKOS_FAILAS)
    failas.outputStream().use { srautas ->
        nuotrauka.compress(Bitmap.CompressFormat.JPEG, 95, srautas)
    }
}

fun nuskaitytiNuotrauka(context: Context): Bitmap? {
    val failas = File(context.filesDir, NUOTRAUKOS_FAILAS)
    if (!failas.exists()) {
        return null
    }
    return BitmapFactory.decodeFile(failas.absolutePath)
}
