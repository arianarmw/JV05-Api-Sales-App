# 🛒 API Sales APP

## 📝 Deskripsi  
API ini dibangun menggunakan **Java Spring Boot** dan **PostgreSQL**.
API yang tersedia, yaitu API Login, Register, Master Data Produk, Transaksi Penjualan, dan Laporan.

Skema komponen database tersedia pada link berikut: https://docs.google.com/spreadsheets/d/1C1Q3wVCYLbC10dwd2WXVQNn_w2vs1tqBMRQ7hpfpUFg/edit?usp=sharing

Dokumentasi API tersedia di link Postman berikut: https://app.getpostman.com/join-team?invite_code=c3bc89958fbd2d0410c26086a499b285ece4035451942a0ae060b1e2aedd4c0f&target_code=557827a9ffd2a8724a10fffe25572165

Apabila ingin mengaksesnya, silahkan request untuk akses collection dengan email Anda dan tunggu untuk mendapatkan accept.

---

## 📌 Fitur Utama  
1. **API Login**:  
   - Authority tersedia untuk seluruh pengguna.
   - Autentikasi dilakukan menggunakan username dan password.

2. **API Register**:  
   - Authority hanya tersedia untuk pengguna dengan role ADMIN.
   - Pendaftaran akun ini hanya diberlakukan untuk pengguna dengan role KASIR dan proses pendaftarannya hanya bisa didaftarkan oleh role ADMIN.
   - Akun role KASIR yang didaftarkan akan memiliki status aktivasi isActive FALSE
   - Akun role ADMIN yang hanya bisa aktivasi akun role KASIR.

3. **API Master Data Produk**:  
   - CRUD (Create, Read, Update, Delete) untuk produk.  
   - Menyimpan detail produk seperti nama, harga, dan stok.  

4. **API Transaksi Penjualan**:  
   - Pembuatan transaksi baru.  
   - Pengurangan stok produk secara otomatis.  

5. **API Laporan**:  
   - Menyediakan laporan penjualan berdasarkan tanggal atau produk tertentu.  

---

## 🛠️ Teknologi yang Digunakan  
- **Backend**: Java Spring Boot  
- **Database**: PostgreSQL ver 17.2
- **Autentikasi**: Spring Security dengan JWT  

---

## 🚧 Status  
Dalam pengembangan.

---

## 💡 Cara Menjalankan  
1. Clone repository ini.  
2. Pastikan PostgreSQL terinstal dan jalankan.  
3. Konfigurasikan file `application.properties` dengan kredensial database.  
4. Jalankan aplikasi menggunakan IDE apapun atau perintah `mvn spring-boot:run`.  

---

## 🤝 Kontribusi  
Jika Anda memiliki ide untuk fitur tambahan atau menemukan bug, jangan ragu untuk membuka issue atau pull request! 😊  
