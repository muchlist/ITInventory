package com.meretas.itinventory.computer_inv.add_computer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.ComputerPostData
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.BANJARMASIN
import com.meretas.itinventory.utils.Statis
import com.meretas.itinventory.utils.Translasi
import kotlinx.android.synthetic.main.activity_add_computer.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class AddComputerActivity : AppCompatActivity() {

    private lateinit var viewModel: AddComputerViewModel
    private lateinit var computer: ComputerPostData

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

    private lateinit var translasi: Translasi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_computer)

        viewModel = ViewModelProviders.of(this).get(AddComputerViewModel::class.java)
        translasi = Translasi()

        //OBSERVER
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame.visibility = View.VISIBLE
            } else {
                pb_frame.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                val name = et_add_name.text.toString()
                Statis.isComputerUpdate = true
                longToast("$name Berhasil ditambahkan")
            }
        })

        viewModel.isCont.observe(this, Observer {
            if (!it){
                finish()
            } else {
                et_add_name.setText("")
            }
        })

        //MENYESUAIKAN LAYOUT, CHOICES LOCATION HANYA UNTUK BANJARMASIN
        tv_add_cabang.text = App.prefs.userBranchSave
        if (App.prefs.userBranchSave == BANJARMASIN){
            //PASS
        } else {
            et_add_lokasi.visibility = View.GONE
        }

        bt_add_computer_and_cont.setOnClickListener { sendDataComputer(true) }
        bt_add_computer_and_finish.setOnClickListener { sendDataComputer(false) }

        et_add_divisi.setOnClickListener { choiceDivisi() }
        et_add_lokasi.setOnClickListener { choiceLokasi() }
        et_add_jenis_pc.setOnClickListener { choiceJenisPC() }
        et_add_seat_manajemen_pc.setOnClickListener { choiceSeatManajement() }
        tv_add_os.setOnClickListener { choiceSistemOperasi() }
        tv_add_processor.setOnClickListener { choiceProcessor() }
        tv_add_ram.setOnClickListener { choiceRam() }
        tv_add_hardisk.setOnClickListener { choicehardisk() }
        tv_add_status.setOnClickListener { choiceStatus() }
    }

    private fun choiceDivisi() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Umum",
            "Operasional",
            "Komersial",
            "Teknik",
            "Keuangan",
            "TIK",
            "SMI",
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

    private fun choiceLokasi() {
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

    private fun choiceJenisPC() {
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

    private fun choiceSeatManajement() {
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

    private fun choiceSistemOperasi() {
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

    private fun choiceProcessor() {
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

    private fun choiceRam() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "2 GB",
            "4 GB DDR3",
            "4 GB DDR4",
            "6 GB",
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

    private fun choicehardisk() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "256 GB",
            "512 GB",
            "1 TB",
            "2 TB",
            "128 GB",
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

    private fun choiceStatus() {
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

    private fun sendDataComputer(isContinue: Boolean) {
        if (et_add_name.text.toString().isNotEmpty() && divisi.isNotEmpty()) {
            computer = ComputerPostData(
                lokasi,
                divisi,
                jenisPerangkat,
                seatManajement,
                sistemOperasi,
                processor,
                ram,
                hardisk,
                statusPC,
                et_add_name.text.toString(),
                et_add_hostname.text.toString(),
                et_add_ip_address.text.toString(),
                et_add_no_inventory.text.toString(),
                et_add_merk_pc.text.toString(),
                et_add_tahun_pc.text.toString(),
                et_add_vga.text.toString(),
                et_add_note.text.toString()
            )

            viewModel.postComputer(computer, isContinue)
        } else {
            toast("Nama User dan Divisi Tidak Boleh Kosong!")
        }
    }
}
