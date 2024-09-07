package com.ujjolch.masterapp

fun hexToDecimal(hex: String): Int {
    return hex.toInt(16)
}


fun hexToBinary(hex: String): String {
    val hexToBinaryMap = mapOf(
        '0' to "0000", '1' to "0001", '2' to "0010", '3' to "0011",
        '4' to "0100", '5' to "0101", '6' to "0110", '7' to "0111",
        '8' to "1000", '9' to "1001", 'A' to "1010", 'B' to "1011",
        'C' to "1100", 'D' to "1101", 'E' to "1110", 'F' to "1111"
    )

    val binaryStringBuilder = StringBuilder()

    for (char in hex.toUpperCase()) {
        val binaryValue = hexToBinaryMap[char]
        if (binaryValue != null) {
            binaryStringBuilder.append(binaryValue)
        } else {
            throw IllegalArgumentException("Invalid hexadecimal character: $char")
        }
    }

    return binaryStringBuilder.toString()
}

fun getDataType(hex: String): Char {

    val binaryString = hexToBinary(hex)
    // Return the last character of the string
    return binaryString.last()
}
fun getDecimalPoint(hex: String): String {
    val binaryString = hexToBinary(hex)
    // Extract the 5th and 6th bits
    val fifthBit = binaryString[5]
    val sixthBit = binaryString[6]
    // Combine the bits into a single string
    return "$fifthBit$sixthBit"
}

fun getUnit(hex: String): String {
    val binaryString = hexToBinary(hex)
    // Extract the 5th and 6th bits
    val thirdBit = binaryString[3]
    val fourthBit = binaryString[4]
    // Combine the bits into a single string
    return "$thirdBit$fourthBit"
}

fun addDecimalPoint2(binarydecimalpoint: String, number: Int): Double {
    return when (binarydecimalpoint) {
        "00" -> {
            // Add a point from 1 place from the end
            val strNumber = number.toString()
            val result = strNumber.dropLast(1) + "." + strNumber.takeLast(1)
            result.toDouble()
        }
        "01" -> {
            // Convert the integer to double
            number.toDouble()
        }
        "10" -> {
            // Add a point after 2 places from the end
            val strNumber = number.toString()
            val result = strNumber.dropLast(2) + "." + strNumber.takeLast(2)
            result.toDouble()
        }
        else -> {
            number.toDouble()
        }
    }
}

