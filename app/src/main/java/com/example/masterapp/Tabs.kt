package com.example.masterapp

import androidx.annotation.DrawableRes

sealed class Tab(val title:String,@DrawableRes val icon:Int){
    object Weight:Tab("Weight",R.drawable.baseline_monitor_weight_24)
    object BMI:Tab("BMI",R.drawable.calculator)
    object BodyWaterPercent:Tab("Body Water%",R.drawable.baseline_water_drop_24)
    object BodyFatPercent:Tab("Body Fat%",R.drawable.google_fit)
    object SkeletalMuscleMass:Tab("Skeletal Muscle Mass",R.drawable.dumbbell)
//    object BoneWeightPercent:Tab("Bone Weight%",R.drawable.bone)
    object LeanBodyMass:Tab("Lean Body Mass",R.drawable.baseline_monitor_weight_24)
    object BMR:Tab("BMR",R.drawable.food_variant)
}

val listOfTabs = listOf(
    Tab.Weight,
    Tab.BMI,
    Tab.BodyWaterPercent,
    Tab.BodyFatPercent,
    Tab.SkeletalMuscleMass,
//    Tab.BoneWeightPercent,
    Tab.LeanBodyMass,
    Tab.BMR
)

