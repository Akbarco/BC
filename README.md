# 📱 Modern Dark Mode Calculator (Jetpack Compose)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android)](https://www.android.com)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A clean, modern, and ergonomic Calculator Android application built with **Jetpack Compose** and **Material 3**, designed from the ground up for a sleek **AMOLED Dark Mode** experience.

---

## ✨ Features

- 🎨 **Sleek AMOLED Dark Mode UI**: Deep slate backgrounds (`#121214`) paired with high-contrast functional color tokens for comfortable, strain-free daily use.
- ⚡ **Dynamic Dual-Line Display**:
  - **Active Typing State**: Shows the active mathematical formula on top in bright white (`#FFFFFF`) with real-time live preview underneath in muted gray (`#8E8E93`).
  - **Calculated State (`=`)**: Cleanly clears the formula and elevates the final answer to crisp white on the main display.
- 🧮 **High-Precision Math Engine**:
  - Powered by `BigDecimal` to eliminate floating-point arithmetic inaccuracies (e.g. `0.1 + 0.2 = 0.3`).
  - Handles operator precedence (**KABATAKU / PEMDAS**: multiplication and division before addition and subtraction).
  - Division by zero protection (*"Tidak bisa dibagi 0"*).
- 📳 **Haptic Feedback & Micro-Interactions**: Subtle press-scaling animations and physical haptic click feedback for every button touch.
- 🖐️ **One-Handed Ergonomic Keypad**: 5-row, 4-column balanced grid featuring quick-access Backspace (`⌫`), All Clear (`AC`), Sign Inversion (`±`), and Percentage (`%`).

---

## 🛠️ Tech Stack & Architecture

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) + [Material 3](https://m3.material.io/)
- **Language**: [Kotlin](https://kotlinlang.org/)
- **Architecture**: MVVM (Model-View-ViewModel) with Unidirectional Data Flow (UDF)
- **State Management**: Kotlin Coroutines `StateFlow`
- **Build System**: Gradle Kotlin DSL (`build.gradle.kts`) with Gradle Version Catalog (`libs.versions.toml`)
- **Testing**: JUnit 4 for unit tests covering arithmetic logic, operator precedence, and edge cases

---

## 📁 Project Structure

```text
app/src/main/java/com/example/bc/
├── logic/
│   ├── CalculatorAction.kt     # Sealed interface for user actions (Number, Op, Calculate, etc.)
│   ├── CalculatorEngine.kt     # Core math evaluation logic with BigDecimal precision
│   ├── CalculatorOperation.kt  # Enum for +, −, ×, ÷
│   ├── CalculatorState.kt      # Immutable state model (expression, liveResult, display getters)
│   └── CalculatorViewModel.kt  # StateFlow management
├── ui/
│   ├── components/
│   │   ├── CalculatorButton.kt  # Animated tactile squircle button with haptics
│   │   └── CalculatorDisplay.kt # Dynamic auto-scaling & scrolling text display
│   ├── theme/
│   │   ├── Color.kt             # Dark palette color tokens
│   │   ├── Theme.kt             # Material 3 Dark theme setup
│   │   └── Type.kt              # Typography styles
│   └── CalculatorScreen.kt      # Responsive 5-row keypad layout
└── MainActivity.kt              # Edge-to-edge Compose host activity
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug / Meerkat (2024.1+) or newer
- **JDK**: Java 17 or Java 21
- **Android SDK**: `minSdk 24` (Android 7.0 Nougat) | `targetSdk 37`

### Installation & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Akbarco/BC.git
   ```
2. Open the project folder in **Android Studio**.
3. Let Gradle sync dependencies automatically.
4. Select an Android Emulator or connected physical device and click **Run (Shift + F10)**.

### Running Unit Tests

To run the full suite of math engine unit tests:
```bash
./gradlew testDebugUnitTest
```

---

## 👤 Author

Developed by **[Akbarco](https://github.com/Akbarco)**
