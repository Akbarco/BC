# DOKUMENTASI ARSITEKTUR & PANDUAN PRESENTASI
## Kalkulator Modern Jetpack Compose (Dark Mode)

---

## 1. RINGKASAN PROYEK

Aplikasi kalkulator Android modern yang dibangun menggunakan **Jetpack Compose** dan **Material 3**, menerapkan pola arsitektur **MVVM (Model-View-ViewModel)** dengan prinsip **Unidirectional Data Flow (UDF)**. 

### Karakteristik Utama:
- **Tampilan**: AMOLED Dark Mode minimalis dengan visual hierarki yang jelas.
- **Engine Perhitungan**: Presisi tinggi menggunakan `BigDecimal` dan algoritma *Shunting-yard* untuk mendukung urutan operasi aritmatika (KABATAKU / PEMDAS).
- **Interaksi**: Animasi tactile press-scale dan haptic feedback pada tombol.

---

## 2. DIAGRAM ALUR DATA (DATA FLOW: DARI MANA KE MANA)

Alur pergerakan data dalam aplikasi berjalan satu arah (*Unidirectional Data Flow*):

```
+--------------------------------------------------------------------------+
|                                1. VIEW                                   |
|  Pengguna menekan tombol pada CalculatorScreen / CalculatorButton        |
+--------------------------------------------------------------------------+
                                     |
                                     | (Mengirim CalculatorAction, misal: Number(5), Add)
                                     v
+--------------------------------------------------------------------------+
|                             2. VIEWMODEL                                 |
|  CalculatorViewModel menerima event melalui method onAction(action)      |
+--------------------------------------------------------------------------+
                                     |
                                     | (Meneruskan currentState + action)
                                     v
+--------------------------------------------------------------------------+
|                              3. ENGINE                                   |
|  CalculatorEngine mengevaluasi ekspresi matematika                       |
|  - Tokenisasi string                                                     |
|  - Konversi Infix -> Postfix (Shunting-yard)                             |
|  - Eksekusi kalkulasi dengan BigDecimal                                  |
|  - Formatting output (pemisah ribuan & desimal)                          |
+--------------------------------------------------------------------------+
                                     |
                                     | (Mengembalikan CalculatorState baru)
                                     v
+--------------------------------------------------------------------------+
|                         4. STATE EMISSION                                |
|  CalculatorViewModel memperbarui StateFlow<CalculatorState>             |
+--------------------------------------------------------------------------+
                                     |
                                     | (State diamati oleh Compose via collectAsState())
                                     v
+--------------------------------------------------------------------------+
|                             5. RECOMPOSITION                             |
|  CalculatorDisplay & CalculatorScreen merender tampilan terbaru          |
+--------------------------------------------------------------------------+
```

---

## 3. URUTAN PEMBUATAN FILE (JIKA DIBUAT DARI AWAL)

Urutan logis implementasi aplikasi dari dasar:

```
[Tahap 1: Pondasi Desain]
   └── 1. Color.kt                (Definisi konstanta warna Dark Mode)
   └── 2. Theme.kt                (Setup tema Material 3 DarkColorScheme)

[Tahap 2: Model Data & State]
   └── 3. CalculatorOperation.kt  (Enum operator: +, −, ×, ÷)
   └── 4. CalculatorAction.kt     (Sealed interface semua aksi interaksi)
   └── 5. CalculatorState.kt      (Data class penampung state kalkulator)

[Tahap 3: Logika Bisnis & Mesin Hitung]
   └── 6. CalculatorEngine.kt     (Parser, evaluator matematika, formatting)

[Tahap 4: State Management]
   └── 7. CalculatorViewModel.kt  (Jembatan UI dan Engine via StateFlow)

[Tahap 5: Komponen Antarmuka (UI Components)]
   └── 8. CalculatorButton.kt     (Tombol kustom membulat + haptics)
   └── 9. CalculatorDisplay.kt    (Layar teks 2-baris responsif + auto-scroll)

[Tahap 6: Layout Layar Utama]
   └── 10. CalculatorScreen.kt    (Penyusunan grid keypad 5 baris)

[Tahap 7: Entry Point Aplikasi]
   └── 11. MainActivity.kt        (Inisialisasi Activity & Edge-to-Edge)

[Tahap 8: Pengujian & Verifikasi]
   └── 12. CalculatorEngineTest.kt(Unit test fungsionalitas & edge case)
```

---

## 4. PENJELASAN DETAIL PER FILE

Berikut rincian peran, fungsi, input, dan output dari masing-masing file:

### 1. `Color.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/ui/theme/Color.kt`
- **Peran**: Menyimpan seluruh token warna yang digunakan di dalam aplikasi.
- **Variabel Kunci**:
  - `DarkBackground`: Latar belakang utama aplikasi (`#121214`).
  - `DarkSurface`: Warna latar permukaan kartu / keypad (`#1C1C1E`).
  - `NumberBtnBg` & `NumberBtnText`: Warna tombol angka 0-9 dan titik (`#2C2C2E` / `#F5F5F7`).
  - `FunctionBtnBg` & `FunctionBtnText`: Warna tombol fungsi AC, ±, %, ⌫ (`#3A3A3C` / `#E5E5EA`).
  - `OperatorBtnBg` & `OperatorBtnText`: Warna tombol operator matematika (`#FF9F0A` / `#FFFFFF`).
  - `ExpressionColor`: Warna teks rumus/operasi aktif (`#8E8E93`).
  - `ResultColor`: Warna teks hasil akhir (`#FFFFFF`).
  - `ErrorColor`: Warna teks peringatan kesalahan (`#FF453A`).

---

### 2. `Theme.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/ui/theme/Theme.kt`
- **Peran**: Mengonfigurasi tema Material 3 agar default menggunakan palet Dark Mode kalkulator.
- **Fungsi Kunci**:
  - `BCTheme(content: @Composable () -> Unit)`: Wrapper composable yang menerapkan `DarkColorScheme` ke seluruh hierarki antarmuka.

---

### 3. `CalculatorOperation.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/logic/CalculatorOperation.kt`
- **Peran**: Representasi tipe data enum untuk 4 operasi aritmatika dasar.
- **Isi**:
  - `ADD` (`+`, display: `+`)
  - `SUBTRACT` (`-`, display: `−`)
  - `MULTIPLY` (`*`, display: `×`)
  - `DIVIDE` (`/`, display: `÷`)
  - Method pendukung `fromSymbol(symbol: String)` untuk memetakan karakter ke objek enum.

---

### 4. `CalculatorAction.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/logic/CalculatorAction.kt`
- **Peran**: Mendefinisikan seluruh event / aksi yang dapat dipicu oleh pengguna melalui `sealed interface`.
- **Aksi yang Didukung**:
  - `Number(val number: Int)`: Menekan angka 0-9.
  - `Decimal`: Menekan tanda titik desimal `.`.
  - `Clear`: Menekan tombol `AC` (reset total).
  - `Delete`: Menekan tombol backspace `⌫` (hapus 1 digit terakhir).
  - `Operation(val operation: CalculatorOperation)`: Menekan tombol operator (`+`, `−`, `×`, `÷`).
  - `Calculate`: Menekan tombol sama dengan `=`.
  - `ToggleSign`: Menekan tombol `±` (mengubah tanda positif/negatif).
  - `Percentage`: Menekan tombol `%` (konversi ke persen / bagi 100).

---

### 5. `CalculatorState.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/logic/CalculatorState.kt`
- **Peran**: Menyimpan kondisi terkini (*single source of truth*) dari kalkulator.
- **Properti Data**:
  - `expression: String`: String ekspresi matematika yang sedang aktif (contoh: `"888 × 9"`).
  - `liveResult: String`: Nilai hasil kalkulasi.
  - `isEvaluated: Boolean`: Bernilai `true` jika pengguna baru saja menekan `=`.
  - `isError: Boolean`: Menandai apakah kalkulasi menghasilkan kesalahan (contoh: pembagian nol).
  - `errorMessage: String`: Pesan kesalahan yang ditampilkan jika `isError == true`.
- **Computed Getters**:
  - `topDisplay`: Mengembalikan string untuk baris atas (kosong saat selesai evaluasi, menampilkan rumus saat mengetik).
  - `bottomDisplay`: Mengembalikan string untuk baris bawah (hasil evaluasi atau ekspresi yang aktif).

---

### 6. `CalculatorEngine.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/logic/CalculatorEngine.kt`
- **Peran**: Inti komputasi matematika dan logika manipulasi string kalkulator.
- **Fungsi & Komponen Kunci**:
  - `onAction(currentState, action)`: Pure function yang menerima state lama dan action, lalu menghasilkan state baru.
  - `handleNumber()`: Mengatur penambahan digit dan mencegah duplikasi angka nol di depan (`leading zeros`).
  - `handleDecimal()`: Validasi agar satu bilangan tidak memiliki lebih dari satu titik desimal.
  - `handleOperation()`: Mengganti operator secara cerdas jika pengguna menekan operator berturut-turut.
  - `handleDelete()`: Menghapus satu digit atau operator beserta spasinya.
  - `handleCalculate()`: Mengevaluasi ekspresi matematika menjadi hasil final.
  - `handleToggleSign()`: Mengubah tanda positif/negatif pada bilangan terakhir.
  - `handlePercentage()`: Mengalikan bilangan terakhir dengan `0.01` (`/ 100`).
  - `tokenize(expr)`: Memecah string ekspresi menjadi daftar token angka dan operator.
  - `infixToPostfix(tokens)`: Algoritma *Shunting-yard* untuk menyusun token berdasarkan prioritas operator.
  - `evaluatePostfix(postfix)`: Menghitung hasil dari notasi postfix menggunakan stack dan `BigDecimal(MathContext.DECIMAL64)`.
  - `formatBigDecimal(value)`: Menghilangkan nol tak berguna di belakang koma dan memformat angka ribuan (contoh: `1,250.5`).

---

### 7. `CalculatorViewModel.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/logic/CalculatorViewModel.kt`
- **Peran**: ViewModel arsitektur MVVM yang mempertahankan state selama perubahan konfigurasi (misal rotasi layar).
- **Struktur**:
  - `_state: MutableStateFlow<CalculatorState>`: State privat yang dapat dimutasi.
  - `state: StateFlow<CalculatorState>`: Read-only state flow yang diobservasi oleh layer UI.
  - `onAction(action: CalculatorAction)`: Method publik yang dipanggil oleh UI untuk memicu kalkulasi via `CalculatorEngine`.

---

### 8. `CalculatorButton.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/ui/components/CalculatorButton.kt`
- **Peran**: Komponen tombol composable yang reusable dengan efek interaksi sentuhan.
- **Fitur Teknis**:
  - `ButtonType`: Enum pembeda tipe tombol (`NUMBER`, `FUNCTION`, `OPERATOR`, `EQUALS`) untuk menentukan warna latar dan ukuran font.
  - `animateFloatAsState`: Animasi *smooth press scale* (mengecil ke `0.92f` saat ditekan).
  - `LocalHapticFeedback`: Menggetarkan perangkat dengan sensasi fisik klik saat disentuh.
  - `Box` dengan `clip(CircleShape)` untuk bentuk tombol bulat sempurna.

---

### 9. `CalculatorDisplay.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/ui/components/CalculatorDisplay.kt`
- **Peran**: Menampilkan ekspresi dan hasil kalkulasi pada layar.
- **Fitur Teknis**:
  - `Baris Atas`: Menampilkan rumus operasi (warna putih saat mengetik, hilang saat klik `=`).
  - `Baris Bawah`: Menampilkan angka aktif, live preview, atau hasil akhir.
  - `rememberScrollState()` + `LaunchedEffect`: Auto-scroll otomatis ke ujung kanan saat teks semakin panjang.
  - `Dynamic Font Scaling`: Ukuran font otomatis menyesuaikan (dari `56sp` turun ke `44sp` hingga `34sp` jika angka sangat panjang).

---

### 10. `CalculatorScreen.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/ui/CalculatorScreen.kt`
- **Peran**: Halaman utama kalkulator yang menyatukan area display dan grid tombol keypad.
- **Susunan Grid (5 Baris × 4 Kolom)**:
  - Baris 1: `AC` | `±` | `%` | `÷`
  - Baris 2: `7` | `8` | `9` | `×`
  - Baris 3: `4` | `5` | `6` | `−`
  - Baris 4: `1` | `2` | `3` | `+`
  - Baris 5: `0` | `.` | `⌫` | `=`
- Menggunakan `Modifier.weight(1f)` dan `aspectRatio(1f)` agar seluruh tombol simetris di berbagai ukuran resolusi layar HP.

---

### 11. `MainActivity.kt`
- **Lokasi**: `app/src/main/java/com/example/bc/MainActivity.kt`
- **Peran**: Activity utama dan entry point aplikasi.
- **Fungsi**:
  - Memanggil `enableEdgeToEdge()` untuk tampilan layar penuh modern.
  - Menginstansiasi `CalculatorViewModel` via `viewModels()`.
  - Mengamati state dengan `val state by viewModel.state.collectAsState()`.
  - Menghubungkan callback aksi `viewModel::onAction` ke `CalculatorScreen`.
  - Menyediakan `@Preview` untuk melihat kalkulator langsung di panel design Android Studio.

---

### 12. `CalculatorEngineTest.kt`
- **Lokasi**: `app/src/test/java/com/example/bc/CalculatorEngineTest.kt`
- **Peran**: Unit test otomatis untuk menjamin kebenaran logika engine matematika.
- **Skenario Pengujian**:
  - `testBasicAddition`: Memastikan penjumlahan dasar bekerja (`5 + 3 = 8`).
  - `testOperatorPrecedence`: Memastikan prioritas operator benar (`2 + 3 * 4 = 14`, bukan `20`).
  - `testDivisionByZero`: Memastikan pembagian nol ditangani dengan aman tanpa crash (`Tidak bisa dibagi 0`).
  - `testDecimals`: Memastikan kalkulasi desimal akurat (`0.1 + 0.2 = 0.3`).
  - `testToggleSign`: Memastikan fungsi pembalik tanda (`±`).
  - `testPercentage`: Memastikan konversi persen (`50% = 0.5`).
  - `testDeleteBackspace`: Memastikan penghapusan karakter per digit (`123` -> `12`).
  - `testClearAll`: Memastikan reset state ke kondisi awal.

---

## 5. CHEATSHEET & TANYA-JAWAB PRESENTASI

Berikut poin penting yang sering ditanyakan dosen / penguji beserta jawabannya:

| Pertanyaan Penguji | Jawaban Teknis yang Tepat |
| :--- | :--- |
| **Kenapa menggunakan `BigDecimal` dibanding `Double`?** | Tipe data `Double` pada komputer menggunakan floating point IEEE 754 biner yang dapat menyebabkan bug presisi (contoh: `0.1 + 0.2` menghasilkan `0.30000000000000004`). `BigDecimal` menghitung dengan basis desimal eksak sehingga hasil perhitungan keuangan atau matematika selalu 100% akurat. |
| **Bagaimana aplikasi menangani prioritas operator (KABATAKU / PEMDAS)?** | Menggunakan algoritma **Shunting-yard** karya Edsger Dijkstra. Algoritma ini mengubah notasi *Infix* (`2 + 3 × 4`) menjadi notasi *Postfix / Reverse Polish Notation* (`2 3 4 * +`), sehingga perkalian dan pembagian otomatis dieksekusi terlebih dahulu sebelum penjumlahan. |
| **Apa keuntungan arsitektur MVVM + UDF pada Compose?** | Memisahkan UI dan Logika (*Separation of Concerns*). State disimpan di ViewModel dan UI hanya merender data murni secara deklaratif. Saat layar diputar (rotasi), state perhitungan tidak hilang karena ViewModel tidak dihancurkan oleh lifecycle Activity. |
| **Bagaimana penanganan error pembagian dengan nol?** | Di dalam `CalculatorEngine`, pembagi diperiksa terhadap `BigDecimal.ZERO`. Jika terjadi pembagian nol, engine menangkap `ArithmeticException` dan mengembalikan state error bertuliskan `"Tidak bisa dibagi 0"` tanpa menyebabkan aplikasi *Force Close*. |
