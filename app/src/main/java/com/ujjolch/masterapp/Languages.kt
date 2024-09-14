package com.ujjolch.masterapp

sealed class Language(val Language:String,
                     val LanguageCode: String){
    object English:Language("English","en")
    object Hindi:Language("Hindi","hi")
    object Spanish:Language("Spanish","es")
    object German:Language("German","de")
    object Bengali:Language("Bengali","bn")
    object Punjabi:Language("Punjabi","pa")
    object Gujarati:Language("Gujarati","gu")
    object French:Language("French","fr")
    object Japanese:Language("Japanese","ja")
    object Chinese:Language("Chinese","zh")
    object Marathi:Language("Marathi","mr")
    object Kannada:Language("Kannada","kn")
}

val listoflanguages = listOf<Language>(Language.English,
    Language.Hindi,
    Language.Bengali,
    Language.Punjabi,
    Language.Gujarati,
    Language.Marathi,
    Language.Kannada,
    Language.Spanish,
    Language.German,
    Language.French,
    Language.Japanese,
    Language.Chinese)