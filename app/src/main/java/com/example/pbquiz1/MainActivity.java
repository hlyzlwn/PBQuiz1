package com.example.pbquiz1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends Activity {
    EditText etnamapembeli, etkodebarang, etjumlahbarang;
    RadioGroup radiogroup;
    RadioButton rbgold, rbsilver, rbreguler;
    Button btnproses;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etnamapembeli = findViewById(R.id.etnamapembeli);
        etkodebarang = findViewById(R.id.etkodebarang);
        etjumlahbarang = findViewById(R.id.etjumlahbarang);
        radiogroup = findViewById(R.id.radiogroup);
        rbgold = findViewById(R.id.rbgold);
        rbsilver = findViewById(R.id.rbsilver);
        rbreguler = findViewById(R.id.rbreguler);
        btnproses = findViewById(R.id.btnproses);

        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaPembeli = etnamapembeli.getText().toString().trim();
                String kodeBarang = etkodebarang.getText().toString().trim();
                int jumlahbarang = Integer.parseInt(etjumlahbarang.getText().toString().trim());

                double hargaBarang = getHargaBarang(kodeBarang); // Mengambil harga barang
                if (hargaBarang == -1) {
                    Toast.makeText(MainActivity.this, "Kode Barang Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    return;
                }

                double totalHarga = hargaBarang * jumlahbarang;
                double diskonMember = diskonMember(totalHarga);
                double diskonHarga = diskonHarga(totalHarga);

                // Hitung total bayar setelah diskon
                double totalBayar = totalHarga - diskonMember - diskonHarga;

                String namaBarang = getNamaBarang(kodeBarang);
                tampilkanBon(namaPembeli, kodeBarang, namaBarang, hargaBarang, jumlahbarang, totalHarga, diskonMember, diskonHarga, totalBayar);
            }
        });
    }

    private double getHargaBarang(String kodeBarang) {
        switch (kodeBarang) {
            case "IPX":
                return 5725300;
            case "AV4":
                return 9150999;
            case "MP3":
                return 28999999;
            default:
                return -1;
        }
    }

    private String getNamaBarang(String kodeBarang) {
        switch (kodeBarang) {
            case "IPX":
                return "Iphone X";
            case "AV4":
                return "Asus Vivobook 14";
            case "MP3":
                return "MACBOOK PRO M3";
            default:
                return null;
        }
    }

    private double diskonMember(double totalHarga) {
        double diskonMember = 0;
        int selectedRadioButtonId = radiogroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.rbgold) {
            diskonMember = totalHarga * 0.1;
        } else if (selectedRadioButtonId == R.id.rbsilver) {
            diskonMember = totalHarga * 0.05;
        } else if (selectedRadioButtonId == R.id.rbreguler) {
            diskonMember = totalHarga * 0.02;

        }
        return diskonMember;
    }

    private double diskonHarga(double totalHarga) {
        if (totalHarga > 10000000) {
            return 100000;
        }
        return 0;
    }

    private void tampilkanBon(String namaPembeli, String kodeBarang, String namaBarang, double hargaBarang, int jumlahBarang, double totalHarga, double potonganHarga, double diskon, double totalBayar) {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        String hargabarang = decimalFormat.format(hargaBarang);
        String hargaJumlah = decimalFormat.format(jumlahBarang);
        String hargatotal = decimalFormat.format(totalHarga);
        String hargabayar = decimalFormat.format(totalBayar);
        String hargaPotongan = decimalFormat.format(potonganHarga);
        String Diskon = decimalFormat.format(diskon);

        String BonBelanja = "Transaksi Hari Ini : " + "\n\n"
                + "Nama Pembeli: " + namaPembeli + "\n"
                + "Kode Barang: " + kodeBarang + "\n"
                + "Nama Barang: " + namaBarang + "\n\n"
                + "Harga Barang/ea: " + hargabarang + "\n"
                + "Jumlah Barang : " + hargaJumlah + "\n"
                + "Total Harga Barang: " + hargatotal + "\n\n"
                + "Diskon MemberShip : " + hargaPotongan + "\n"
                + "Diskon Barang : " + Diskon + "\n\n"
                + "Total Pembayaran: " + hargabayar;

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("Bon Belanja", BonBelanja);
        startActivity(intent);
    }
}
