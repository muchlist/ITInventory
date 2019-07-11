package com.meretas.itinventory.edit_computer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.data.ComputerPostData
import com.meretas.itinventory.utils.*
import kotlinx.android.synthetic.main.activity_edit_computer.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast


class EditComputerActivity : AppCompatActivity() {

    //CHOICES
    private var divisi: String = ""
    private var lokasi: String = "None"
    private var jenisPerangkat: String? = "Desktop"
    private var seatManajement: Boolean = false
    private var sistemOperasi: String? = "1064"
    private var processorPC: Double = 3.0
    private var ramPC: Double = 4.0
    private var hardiskPC: Int = 1000
    private var statusPC: String? = "Baik"

    lateinit var dataIntent: ComputerListData.Result
    lateinit var computerData: ComputerPostData
    private lateinit var viewModel: EditComputerViewModel
    lateinit var translasi: Translasi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.meretas.itinventory.R.layout.activity_edit_computer)

        dataIntent = intent.getParcelableExtra(INTENT_DETAIL_EDIT_COMPUTER)
        viewModel = ViewModelProviders.of(this).get(EditComputerViewModel::class.java)
        translasi = Translasi()

        //MENEMPELKAN VALUE DARI INTENT
        with(dataIntent) {
            et_edit_name.setText(clientName)
            et_edit_hostname.setText(hostname)
            et_edit_ip_address.setText(ipAddress)
            et_edit_no_inventory.setText(inventoryNumber)
            tv_edit_cabang.setText(branch)
            et_edit_divisi.setText(division)
            divisi = division
            et_edit_lokasi.setText(location)
            lokasi = location
            et_edit_jenis_pc.setText(computerType)
            jenisPerangkat = computerType
            et_edit_merk_pc.setText(merkModel)
            et_edit_tahun_pc.setText(year)
            et_edit_seat_manajemen_pc.setText(seatManagement.toString())
            seatManajement = seatManagement
            tv_edit_os.setText(translasi.osTranslation(operationSystem))
            tv_edit_processor.setText(translasi.processorTranslation(processor))
            processorPC = processor
            tv_edit_ram.setText(translasi.ramTranslation(ram))
            ramPC = ram
            tv_edit_hardisk.setText(translasi.hardiskTranslation(hardisk))
            hardiskPC = hardisk
            et_edit_vga.setText(vgaCard)
            tv_edit_status.setText(status)
            statusPC = status
            et_edit_note.setText(note)

        }

        //MENYESUAIKAN LAYOUT, CHOICES LOCATION HANYA UNTUK BANJARMASIN
        if (App.prefs.userBranchSave == BANJARMASIN){
            //PASS
        } else {
            et_edit_lokasi.visibility = View.GONE
        }

        //CLICK LISTENER CHOICESS
        et_edit_divisi.setOnClickListener { choiceDivisi() }
        et_edit_jenis_pc.setOnClickListener { choiceJenisPC() }
        et_edit_lokasi.setOnClickListener { choiceLokasi() }
        et_edit_seat_manajemen_pc.setOnClickListener { choiceSeatManajement() }
        tv_edit_os.setOnClickListener { choiceSistemOperasi() }
        tv_edit_processor.setOnClickListener { choiceProcessor() }
        tv_edit_ram.setOnClickListener { choiceRam() }
        tv_edit_hardisk.setOnClickListener { choicehardisk() }
        tv_edit_status.setOnClickListener { choiceStatus() }

        //BUTTON EDIT SAVE
        bt_edit_computer_and_cont.setOnClickListener {
            sendDataComputer()
        }

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                longToast("${computerData.namaUser} berhasil dirubah!")
                Statis.isComputerUpdate = true
                val data = Intent()
                data.putExtra(INTENT_EDIT_COMPUTER_RESULT ,computerData)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_edit.visibility = View.VISIBLE
            } else {
                pb_frame_edit.visibility = View.GONE
            }
        })


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
                et_edit_divisi.text = status
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
                et_edit_lokasi.text = status
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
                et_edit_jenis_pc.text = status
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
                et_edit_seat_manajemen_pc.text = status
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
                tv_edit_os.text = status
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
                tv_edit_processor.text = status
                processorPC = translasi.processorTranslationReverse(status)
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
                tv_edit_ram.text = status
                ramPC = translasi.ramTranslationReverse(status)
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
                tv_edit_hardisk.text = status
                hardiskPC = translasi.hardiskTranslationReverse(status)

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
                tv_edit_status.text = status
                statusPC = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun sendDataComputer() {
        if (!et_edit_name.text.toString().isEmpty() && !divisi.isEmpty()) {
            computerData = ComputerPostData(
                lokasi,
                divisi,
                jenisPerangkat,
                seatManajement,
                sistemOperasi,
                processorPC,
                ramPC,
                hardiskPC,
                statusPC,
                et_edit_name.text.toString(),
                et_edit_hostname.text.toString(),
                et_edit_ip_address.text.toString(),
                et_edit_no_inventory.text.toString(),
                et_edit_merk_pc.text.toString(),
                et_edit_tahun_pc.text.toString(),
                et_edit_vga.text.toString(),
                et_edit_note.text.toString()
            )

            viewModel.putComputer(computerData, dataIntent.id)

        } else {
            toast("Nama User dan Divisi Tidak Boleh Kosong!")
        }
    }
}
