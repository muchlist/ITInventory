package com.meretas.itinventory.utils

class Translasi {
    fun osTranslation(os: String): String {
        return when (os) {
            "1064" -> "Windows 10"
            "732" -> "Windows 7 32 bit"
            "764" -> "Windows 7 64 bit"
            "864" -> "Windows 8 64 bit"
            "832" -> "Windows 8 32 bit"
            "XP" -> "Windows XP"
            "SRV" -> "Windows Server"
            "L" -> "Linux"
            else -> "Windows 10"
        }
    }

    fun osTranslationReverse(os: String): String {
        return when (os) {
            "Windows 10" -> "1064"
            "Windows 7 32 bit" -> "732"
            "Windows 7 64 bit" -> "764"
            "Windows 8 64 bit" -> "864"
            "Windows 8 32 bit" -> "832"
            "Windows XP" -> "XP"
            "Windows Server" -> "SRV"
            "Linux" -> "L"
            else -> "1064"
        }
    }

    fun processorTranslation(processor: Double): String {
        return when (processor) {
            2.0 -> "Kurang dari i3"
            3.0 -> "Setara i3"
            5.0 -> "Setara i5"
            7.0 -> "Setara i7"
            else -> "Setara i3"
        }
    }

    fun processorTranslationReverse(processor: String): Double {
        return when (processor) {
            "Kurang dari i3" -> 2.0
            "Setara i3" -> 3.0
            "Setara i5" -> 5.0
            "Setara i7" -> 7.0
            else -> 3.0
        }
    }

    fun ramTranslationReverse(ram: String): Double {
        return when (ram) {
            "2 GB" -> 2.0
            "4 GB DDR3" -> 4.0
            "4 GB DDR4" -> 4.1
            "8 GB DDR3" -> 8.0
            "8 GB DDR4" -> 8.1
            "16 GB" -> 16.0
            else -> 4.0
        }
    }

    fun ramTranslation(ram: Double): String {
        return when (ram) {
            2.0 -> "2 GB"
            4.0 -> "4 GB DDR3"
            4.1 -> "4 GB DDR4"
            8.0 -> "8 GB DDR3"
            8.1 -> "8 GB DDR4"
            16.0 -> "16 GB"
            else -> "4 GB DDR3"
        }
    }

    fun hardiskTranslationReverse(ram: String): Int {
        return when (ram) {
            "256 GB" -> 256
            "512 GB" -> 512
            "1 TB" -> 1000
            "2 TB" -> 2000
            "Lebih dari 2 TB" -> 4000
            else -> 1000
        }
    }

    fun hardiskTranslation(ram: Int): String {
        return when (ram) {
            256 -> "256 GB"
            512 -> "512 GB"
            1000 -> "1 TB"
            2000 -> "2 TB"
            4000 -> "Lebih dari 2 TB"
            else -> "1 TB"
        }
    }
}