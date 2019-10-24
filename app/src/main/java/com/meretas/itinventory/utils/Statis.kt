package com.meretas.itinventory.utils

class Statis {
    companion object {
        var isComputerUpdate = false  //mengetahui apakah komputer update jika iya list akan direset
        var isHistoryUpdate = false  //apakah history update jika iya list history akan di reset
        var isStockUpdate = false
        var isCctvUpdate = false //false di CctvListActivity , true di AddCctvActivity
        var isPrinterUpdate = false //false di PrinterListActivity , true di AddPrinterActivity
        var isServerUpdate = false //false di PrinterListActivity , true di AddPrinterActivity

        //apabila stok berubah (menambahkan, mengurangkan, mengedit)
        // maka ketiga fragment stock_detail fragment di reload di onresume
        var isStockChangePlus = false
        var isStockChangeMinus = false

        //ketika mengedit stock use detail maka di set true
        var isStockUseDetailUpdate = false

        //Stock List Filter
        var whatStockCategory = ""
        var whatStockActiveStatus = true
        var whatStockBranch = ""

        //Computer List Filter
        var whatComputerBranch = ""
        var whatComputerLocation = ""
        var whatComputerDivision = ""
        var whatComputerSeatManajemen = ""
        var whatComputerStatus = "Baik"

        //Cctv List Filter
        var whatCctvBranch = ""
        var whatCctvStatus = "Aktif"

        //Cctv List Filter
        var whatPrinterBranch = ""
        var whatPrinterStatus = "Baik"
        var whatPrinterDivision = "Umum"

        //Server List Filter
        //Cctv List Filter
        var whatServerBranch = ""
        var whatServerStatus = "Aktif"
        var whatServerCategory = ""

    }
}