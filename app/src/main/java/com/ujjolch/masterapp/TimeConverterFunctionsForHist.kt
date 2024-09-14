package com.ujjolch.masterapp

import java.text.SimpleDateFormat
import java.util.Locale

fun MonthWeightConverter(histList: List<hist>): List<co.yml.charts.common.model.Point> {
    // Date formatter to extract month and year from date string
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val monthYearFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault())

    // Group hist objects by month-year
    val groupedByMonth = histList.groupBy { hist ->
        monthYearFormat.format(dateFormat.parse(hist.date))
    }

    // Calculate average weight for each month
    val pointsList = mutableListOf<co.yml.charts.common.model.Point>()

    groupedByMonth.entries.forEachIndexed { index, entry ->
        val averageWeight = entry.value.map {
            it.weight
        }.average()
        pointsList.add(
            co.yml.charts.common.model.Point(
                x = index.toFloat(),
                y = averageWeight.toFloat()
            )
        )
    }

    return pointsList
}
fun MonthImpedancetConverter(histList: List<hist>): List<co.yml.charts.common.model.Point> {
    // Date formatter to extract month and year from date string
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val monthYearFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault())

    // Group hist objects by month-year
    val groupedByMonth = histList.groupBy { hist ->
        monthYearFormat.format(dateFormat.parse(hist.date))
    }

    // Calculate average impedance for each month
    val pointsList = mutableListOf<co.yml.charts.common.model.Point>()

    groupedByMonth.entries.forEachIndexed { index, entry ->
        val averageImpedance = entry.value.map { it.impedance }.average()
        pointsList.add(
            co.yml.charts.common.model.Point(
                x = index.toFloat(),
                y = averageImpedance.toFloat()
            )
        )
    }

    return pointsList
}

fun getMonthsAndYears(histList: List<hist>): List<String> {                    // returns in the form of mm-yy
    // Date formatter to parse the original date format and extract month-year
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val monthYearFormat = SimpleDateFormat("MM-yy", Locale.getDefault())

    // Create a mutable set to store unique month-year combinations
    val monthsSet = mutableSetOf<String>()

    // Iterate through the list and extract month-year from each date
    for (hist in histList) {
        val date = dateFormat.parse(hist.date)
        val monthYear = monthYearFormat.format(date)
        monthsSet.add(monthYear)
    }

    // Convert the set to a list and return
    return monthsSet.toList()
}
fun convert4digYearToNoYear(dateString: String): String {
    // Date format for input (dd-MM-yyyy)
    val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    // Date format for output (dd-MM-yy)
    val outputFormat = SimpleDateFormat("dd-MM", Locale.getDefault())

    // Parse the input date string
    val date = inputFormat.parse(dateString)

    // Format the date to the new format and return it
    return outputFormat.format(date)
}

fun YearWeightConverter(histList: List<hist>): List<co.yml.charts.common.model.Point> {
    // SimpleDateFormat to parse the date string and extract the year
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    // Group the hist objects by year
    val groupedByYear = histList.groupBy { hist ->
        val date = dateFormat.parse(hist.date)
        val calendar = java.util.Calendar.getInstance()
        calendar.time = date
        calendar.get(java.util.Calendar.YEAR)
    }

    // Calculate the average weight for each year
    val averageWeightsByYear = groupedByYear.map { (year, histGroup) ->
        year to histGroup.map { it.weight }.average()
    }

    // Convert the results to a list of Point objects
    return averageWeightsByYear.mapIndexed { index, (_, avgWeight) ->
        co.yml.charts.common.model.Point(x = index.toFloat(), y = avgWeight.toFloat())
    }
}
fun YearImpedanceConverter(histList: List<hist>): List<co.yml.charts.common.model.Point> {
    // SimpleDateFormat to parse the date string and extract the year
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    // Group the hist objects by year
    val groupedByYear = histList.groupBy { hist ->
        val date = dateFormat.parse(hist.date)
        val calendar = java.util.Calendar.getInstance()
        calendar.time = date
        calendar.get(java.util.Calendar.YEAR)
    }

    // Calculate the average weight for each year
    val averageImpedanceByYear = groupedByYear.map { (year, histGroup) ->
        year to histGroup.map { it.impedance }.average()
    }

    // Convert the results to a list of Point objects
    return averageImpedanceByYear.mapIndexed { index, (_, avgImpedance) ->
        co.yml.charts.common.model.Point(x = index.toFloat(), y = avgImpedance.toFloat())
    }
}
fun getYears(histList: List<hist>): List<String> {
    // Date formatter to parse the original date format and extract the year
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    // Create a mutable set to store unique years
    val yearsSet = mutableSetOf<String>()

    // Iterate through the list and extract the year from each date
    for (hist in histList) {
        val date = dateFormat.parse(hist.date)
        val year = yearFormat.format(date)
        yearsSet.add(year)
    }

    // Convert the set to a list and return
    return yearsSet.toList()
}
