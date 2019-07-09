package com.meretas.itinventory.data

data class ComputerPostData(val lokasi: String,
                            val divisi: String,
                            val jenisPerangkat: String?,
                            val seatManajement: Boolean,
                            val sistemOperasi: String?,
                            val processor: Double,
                            val ram: Double,
                            val hardisk: Int,
                            val statusPC: String?,
                            val namaUser: String,
                            val hostKomputer: String?,
                            val alamatIp: String?,
                            val nomerInventaris: String?,
                            val merkPerangkat: String?,
                            val tahun: String?,
                            val vga: String? ,
                            val note: String?
                            )