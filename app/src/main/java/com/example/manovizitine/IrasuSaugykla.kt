package com.example.manovizitine

import android.content.Context
import org.json.JSONArray

// Įrašų saugojimas telefono pastoviojoje atmintyje naudojant SharedPreferences.
// SharedPreferences saugo tik paprastas reikšmes (tekstą, skaičius),
// todėl įrašų sąrašą paverčiame JSON tekstu, pvz.: ["Pirmas įrašas","Antras įrašas"]
class IrasuSaugykla(context: Context) {

    private val atmintis = context.getSharedPreferences(
        "vizitines_duomenys",
        Context.MODE_PRIVATE
    )

    fun gautiIrasus(raktas: String): List<String> {
        val jsonTekstas = atmintis.getString(raktas, "[]") ?: "[]"
        val masyvas = JSONArray(jsonTekstas)

        val irasai = mutableListOf<String>()
        for (i in 0 until masyvas.length()) {
            irasai.add(masyvas.getString(i))
        }
        return irasai
    }

    fun issaugotiIrasus(raktas: String, irasai: List<String>) {
        val masyvas = JSONArray()
        for (irasas in irasai) {
            masyvas.put(irasas)
        }

        atmintis.edit()
            .putString(raktas, masyvas.toString())
            .apply()
    }
}
