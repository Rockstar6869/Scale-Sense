package com.example.masterapp

data class ThemistoScalesList(
    val scales: MutableList<themistoscale> = mutableListOf()
)

data class themistoscale(
    val name:String,
    val address:String
)
