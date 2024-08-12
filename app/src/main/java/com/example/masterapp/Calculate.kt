package com.example.masterapp

import android.util.Log


fun Double.format(digits: Int) = "%.${digits}f".format(this)

object Calculate {

    fun convertFeetAndInchesToCm(feet: Int, inches: Double): Double {
        val totalInches = feet * 12 + inches
        val cmPerInch = 2.54
        return (totalInches * cmPerInch).format(2).toDouble()
    }

    fun convertCmToFeetAndInches(cm: Double): Pair<Int, Double> {
        val totalInches = cm / 2.54
        val feet = (totalInches / 12).toInt()
        val inches = totalInches % 12
        return Pair(feet, inches)
    }
    fun convertpoundsToKilograms(pounds: Double): Double {
        return (pounds / 2.20462).format(2).toDouble()
    }
    fun convertKgToPounds(kg: Double): Double {
        val conversionFactor = 2.20462
        return (kg * conversionFactor).format(2).toDouble()
    }

    fun BMI(HeightInCM:Double,WeightInKG:Double): Double{
        return (WeightInKG/((HeightInCM/100.0)*(HeightInCM/100.0))).format(2).toDouble()
    }

    fun BodyWaterForMale(Age:Int,HeightInCM:Double,WeightInKG: Double):Double{
        return ((2.447 - (0.09156*Age)) + (0.1074*HeightInCM) + (0.3362*WeightInKG)).format(2).toDouble()
    }
    fun BodyWaterForFemale(Age:Int,HeightInCM:Double,WeightInKG: Double):Double{
        return (-2.097 + (0.1069*HeightInCM) + (0.2466*WeightInKG))
    }
    fun BodyWaterPercent(BodyWater:Double,BodyWeight:Double):Double{
        return ((BodyWater/BodyWeight)*100).format(2).toDouble()
    }
    fun BodyFatPercentforMale(Age:Int,BMI:Double):Double{
        return (1.20*BMI + 0.23*Age-16.2).format(2).toDouble()
    }
    fun BodyFat(weight:Double,BodyFatPercent:Double):Double{
            return (weight*(BodyFatPercent/100)).format(2).toDouble()
    }
    fun BodyFatPercentforFemale(Age:Int,BMI:Double):Double{
        return (1.20*BMI + 0.23*Age-5.4).format(2).toDouble()
    }
    fun LeanBodyMass(Weight:Double,BodyFatPercent: Double):Double{
        val BodyFat = BodyFat(Weight,BodyFatPercent)
        return (Weight-BodyFat).format(2).toDouble()
    }

    fun LeanBodyMassPercent(LeanBodyMass:Double,WeightInKG: Double):Double{
        return ((LeanBodyMass/WeightInKG)*100).format(2).toDouble()
    }

    fun BMRforMale(WeightInKG: Double,HeightInCM: Double,Age: Int):Double{
        return (10 * WeightInKG + 6.25 * HeightInCM - 5*Age+ 5).format(2).toDouble()
    }
    fun BMRforFemale(WeightInKG: Double,HeightInCM: Double,Age: Int):Double{
        return (10*WeightInKG + 6.25*HeightInCM- 5*Age-161).format(2).toDouble()
    }
    fun SkeletalMusscleMassforMale(Impedence:Double,HeightInCM: Double,Age: Int):Double{
        return (((((HeightInCM*HeightInCM)/Impedence)*0.401) + (1*3.825) + (Age* (-0.071)))+5.102).format(2).toDouble()
    }
    fun SkeletalMusscleMassforFemale(Impedence:Double,HeightInCM: Double,Age: Int):Double{
        return (((((HeightInCM*HeightInCM)/Impedence)*0.401) + (0*3.825) + (Age* (-0.071)))+5.102).format(2).toDouble()
    }
    fun BoneWeight(WeightInKG: Double,HeightInCM: Double,Age: Int):Double{
        return (-0.25+((0.046*HeightInCM)+(0.036*WeightInKG)-(0.012*Age))).format(2).toDouble()
    }
    fun BoneWeightPercent(WeightInKG: Double,HeightInCM: Double,Age: Int):Double{
        val BoneWeight = BoneWeight(WeightInKG,HeightInCM,Age)
        return ((BoneWeight/WeightInKG)*100).format(2).toDouble()
    }
    fun InorganicSalt(WeightInKG: Double):Double{
        return (0.04*WeightInKG).format(2).toDouble()
    }
    fun Subcalc(HeightInCM: Double):Double{
        return (((HeightInCM * 0.4) - (HeightInCM * (HeightInCM * 0.0826))) * (-1)).format(2).toDouble()
    }
    fun VisceralFatIndex(HeightInCM:Double,WeightInKG:Double,Age:Int):Double{
        val subcalc = Subcalc(HeightInCM)
        return (((WeightInKG * 305) / (subcalc + 48)) - 2.9 + (Age * 0.15)).format(2).toDouble()
    }
    fun SubcutaneousFatForMale(WeightInKG: Double,HeightInCM: Double,Age: Int):Double{
        val bfatpercent = BodyFatPercentforMale(Age, BMI(HeightInCM,WeightInKG))
        val bfat = BodyFat(WeightInKG,bfatpercent)
        val vfatindex = VisceralFatIndex(HeightInCM,WeightInKG,Age)
        return  (bfat-(bfat*vfatindex/100)).format(2).toDouble()
    }
    fun SubcutaneousFatForFemale(WeightInKG: Double,HeightInCM: Double,Age: Int):Double{
        val bfatpercent = BodyFatPercentforFemale(Age, BMI(HeightInCM,WeightInKG))
        val bfat = BodyFat(WeightInKG,bfatpercent)
        val vfatindex = VisceralFatIndex(HeightInCM,WeightInKG,Age)
        return  (bfat-(bfat*vfatindex/100)).format(2).toDouble()
    }

    fun AMRforMale(WeightInKG: Double,HeightInCM: Double,Age: Int,Activity:String = "e"):Double{
        val multiplyingFactor = if(Activity == "la") 1.375
            else if(Activity == "ma") 1.55
            else if(Activity == "a") 1.725
            else if(Activity == "ea") 1.9
            else 1.2
        val BMR = BMRforMale(WeightInKG,HeightInCM,Age)
        return (BMR*multiplyingFactor).format(2).toDouble()
    }
    fun AMRforFemale(WeightInKG: Double,HeightInCM: Double,Age: Int,Activity:String = "e"):Double{
        val multiplyingFactor = if(Activity == "la") 1.375
        else if(Activity == "ma") 1.55
        else if(Activity == "a") 1.725
        else if(Activity == "ea") 1.9
        else 1.2
        val BMR = BMRforFemale(WeightInKG,HeightInCM,Age)
        return (BMR*multiplyingFactor).format(2).toDouble()
    }

    fun Protien(Weight:Double,BodyFatPercent: Double):Double{
        val lbm = LeanBodyMass(Weight,BodyFatPercent)
        return (0.2*lbm).format(2).toDouble()
    }
    fun ProtienPercentage(Weight:Double,BodyFatPercent: Double):Double{
        val lbm = LeanBodyMass(Weight,BodyFatPercent)
        return ((0.2*lbm)/Weight *100).format(2).toDouble()
    }

    fun MusclePercent(BodyWaterPercent:Double,Weight:Double,BodyFatPercent: Double):Double{
        val Protien = Protien(Weight,BodyFatPercent)
        return (Protien+BodyWaterPercent).format(2).toDouble()
    }

}


fun isInteger(input: String): Boolean {
    return try {
        input.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

fun isDouble(input: String): Boolean {
    return try {
        input.toDouble()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

fun SignAdder(input: String): String {
    return if (input.startsWith("-")) {
        input
    } else {
        "+$input"
    }
}


