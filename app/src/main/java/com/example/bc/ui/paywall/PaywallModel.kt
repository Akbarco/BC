package com.example.bc.ui.paywall

data class SubscriptionPlan(
    val id: String,
    val title: String,
    val price: String,
    val originalPrice: String? = null,
    val period: String,
    val badge: String? = null,
    val isPopular: Boolean = false,
    val features: List<String>
)

val SamplePlans = listOf(
    SubscriptionPlan(
        id = "biasa",
        title = "Biasa (Starter)",
        price = "Rp 19.000",
        period = "/ bulan",
        badge = "Pemula",
        isPopular = false,
        features = listOf(
            "Melihat hasil penjumlahan (+)",
            "Kecepatan hitung standar (3G)",
            "Maksimal 5x sama dengan (=) per hari"
        )
    ),
    SubscriptionPlan(
        id = "pro",
        title = "Pro Calculator 🔥",
        price = "Rp 49.000",
        originalPrice = "Rp 99.000",
        period = "/ bulan",
        badge = "PALING LARIS",
        isPopular = true,
        features = listOf(
            "Buka akses perkalian (×) & pembagian (÷)",
            "Hasil instan tanpa buffering",
            "Bebas iklan di antara angka",
            "Akurasi desimal hingga 12 digit"
        )
    ),
    SubscriptionPlan(
        id = "ultra",
        title = "Ultra VIP 💎",
        price = "Rp 149.000",
        originalPrice = "Rp 499.000",
        period = "/ tahun",
        badge = "SULTAN ONLY",
        isPopular = false,
        features = listOf(
            "Akses Unlimited Tombol Sama Dengan (=)",
            "Dukungan AI Einstein Quantum Math",
            "Kalkulus, Trigonometri & Santet Matrix",
            "WhatsApp pribadi CEO saat salah hitung"
        )
    )
)
