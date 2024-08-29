package com.example.stocktracker

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.Navigation
import com.example.stocktracker.databinding.FragmentHomepageBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

class HomepageFragment : Fragment() {

    private lateinit var _binding: FragmentHomepageBinding
    private val binding get() = _binding // Safe way to access binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)

        // QR kod verilerini gösterme
        binding.logo.setOnClickListener {
            val vt = lastEnteredDataInformationDatabase(requireContext())
            val getData = lastEnteredDataInformationDatabaseDao().getData(vt)
            for (k in getData) {
                Log.e("QRCode: ", k.qrcode)
                Log.e("ItemName: ", k.itemName)
                Log.e("Quantity: ", k.itemQuantity)
            }
        }

        // QR kod oluşturma ve veritabanına kaydetme
        binding.generateqrcode.setOnClickListener {
            showQRCodeCreationDialog(container)
        }

        // QR kod tarama
        binding.scanqrcode.setOnClickListener {
            scanQRCode()
        }

        // Depo sayfasına geçiş
        binding.warehouse.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homepageFragment_to_wareHouse)
        }

        // Zaten bu sayfadasınız mesajı
        binding.scanner.setOnClickListener {
            Snackbar.make(binding.root, "Zaten bu sayfadasınız...", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun showQRCodeCreationDialog(container: ViewGroup?) {
        var stokisim = ""
        var stokadet = ""

        val ad = AlertDialog.Builder(requireContext())
        val tasarim = layoutInflater.inflate(R.layout.alert_tasarim, container, false)
        val edittext = tasarim.findViewById<EditText>(R.id.edittextt)
        ad.setView(tasarim)
        ad.setMessage("Kaydedilecek malzemenin ismini giriniz.")
        ad.setPositiveButton("Ekle") { dialogInterface, i ->
            stokisim = edittext.text.toString()

            val ad2 = AlertDialog.Builder(requireContext())
            val tasarim2 = layoutInflater.inflate(R.layout.alert_tasarim2, container, false)
            val edittext2 = tasarim2.findViewById<EditText>(R.id.edittexttt)
            ad2.setView(tasarim2)
            ad2.setMessage("Kaydedilecek malzemenin stok adetini giriniz.")
            ad2.setPositiveButton("Ekle") { dialogInterface, i ->
                stokadet = edittext2.text.toString()
                generateQRCode(stokisim + stokadet)
                val database = FirebaseDatabase.getInstance()
                val urunler = database.getReference("StockInformation")
                val newProduct = FStockInformation(stokisim + stokadet, stokisim, stokadet.toInt())
                urunler.push().setValue(newProduct)
            }
            ad2.setNegativeButton("İptal") { dialogInterface, i -> }
            ad2.create().show()
        }
        ad.setNegativeButton("İptal") { dialogInterface, i -> }
        ad.create().show()
    }

    private fun generateQRCode(content: String) {
        try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 500, 500)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            binding.qr.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun scanQRCode() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR Code")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Snackbar.make(binding.root, "Tarama iptal edildi", Snackbar.LENGTH_SHORT).show()
            } else {
                handleQRCodeResult(result.contents)
            }
        }
    }

    private fun handleQRCodeResult(contents: String) {
        val database = FirebaseDatabase.getInstance()
        val data = database.getReference("StockInformation")
        data.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                for (p in ds.children) {
                    val getData = p.getValue(FStockInformation::class.java)
                    if (getData != null) {
                        if (contents == getData.qrcode) {
                            val vt = lastEnteredDataInformationDatabase(requireContext())
                            lastEnteredDataInformationDatabaseDao().changeData(
                                vt,
                                getData.qrcode.toString(),
                                getData.stockName.toString(),
                                getData.stockQuantity.toString()
                            )
                            view?.let { Navigation.findNavController(it).navigate(R.id.action_homepageFragment_to_stockStatus) }
                        }
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.root, "Veri yüklenirken hata oluştu.", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
