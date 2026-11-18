package com.example.manovizitine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// ViewModel neišsitrina pasukus ekraną (kai Android iš naujo sukuria Activity),
// todėl vartotojas nepraranda to, ką buvo įrašęs.
class VizitinesViewModel : ViewModel() {
    // Kontaktų forma
    var kontaktai by mutableStateOf("")
    var klausimas by mutableStateOf("")

    // Nauji CV ir portfolio įrašai
    var naujasCvIrasas by mutableStateOf("")
    var naujasPortfolioIrasas by mutableStateOf("")
}
