package com.meretas.itinventory.edit_computer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.add_computer.AddComputerViewModel
import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.utils.DATA_INTENT_COMPUTER_LIST_DETAIL
import com.meretas.itinventory.utils.INTENT_DETAIL_EDIT_COMPUTER
import com.meretas.itinventory.utils.Translasi
import kotlinx.android.synthetic.main.activity_add_computer.*
import org.jetbrains.anko.toast

class EditComputerActivity : AppCompatActivity() {

    //CHOICES
    private var divisi: String = ""
    private var lokasi: String = "None"
    private var jenisPerangkat: String? = "Desktop"
    private var seatManajement: Boolean = false
    private var sistemOperasi: String? = "1064"
    private var processor: Double = 3.0
    private var ram: Double = 4.0
    private var hardisk: Int = 1000
    private var statusPC: String? = "Baik"

    private lateinit var viewModel: EditComputerViewModel
    lateinit var translasi: Translasi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_computer)

        val intent = intent.getParcelableExtra<ComputerListData.Result>(INTENT_DETAIL_EDIT_COMPUTER)
        viewModel = ViewModelProviders.of(this).get(EditComputerViewModel::class.java)
        translasi = Translasi()




    }

    fun choiceDivisi() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Umum",
            "Operasional",
            "Komersial",
            "Teknik",
            "Keuangan",
            "TIK",
            "GM",
            "Server",
            "Lainnya"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Divisi")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                et_add_divisi.text = status
                divisi = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun choiceLokasi() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Bjm_Reg",
            "Bjm_Trisakti",
            "Bjm_TPKB",
            "Bjm_Pulpis",
            "None"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Lokasi")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                et_add_lokasi.text = status
                lokasi = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun choiceJenisPC() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Desktop",
            "Laptop",
            "AllInOne",
            "Server"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Jenis PC")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                et_add_jenis_pc.text = status
                jenisPerangkat = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun choiceSeatManajement() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Ya",
            "Tidak"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seat Management ?")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                et_add_seat_manajemen_pc.text = status
                seatManajement = status == "Ya"
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun choiceSistemOperasi() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Windows 10",
            "Windows 7 32 bit",
            "Windows 7 64 bit",
            "Windows 8 64 bit",
            "Windows 8 32 bit",
            "Windows XP",
            "Windows Server",
            "Linux"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Sistem Operasi")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                tv_add_os.text = status
                sistemOperasi = translasi.osTranslationReverse(status)
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun choiceProcessor() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Kurang dari i3",
            "Setara i3",
            "Setara i5",
            "Setara i7"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Processor")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                tv_add_processor.text = status
                processor = translasi.processorTranslationReverse(status)
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun choiceRam() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "2 GB",
            "4 GB DDR3",
            "4 GB DDR4",
            "8 GB DDR3",
            "8 GB DDR4",
            "16 GB"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih RAM")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                tv_add_ram.text = status
                ram = translasi.ramTranslationReverse(status)
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun choicehardisk() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "256 GB",
            "512 GB",
            "1 TB",
            "2 TB",
            "Lebih dari 2 TB"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Hardisk")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                tv_add_hardisk.text = status
                hardisk = translasi.hardiskTranslationReverse(status)

            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun choiceStatus() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Baik",
            "Backup",
            "Rusak",
            "Hilang"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Jenis PC")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                tv_add_status.text = status
                statusPC = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}
