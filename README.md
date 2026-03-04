# 🔊 Ambient Sound Detector

An end-to-end machine learning project that classifies ambient sounds in real time using deep learning, deployed as an Android mobile application with TensorFlow Lite.

> 📄 **Published Paper** — This project is backed by a peer-reviewed publication. See the `/paper` folder for the full paper.

---

## 📌 Overview

This project tackles the problem of ambient sound classification — detecting sounds like baby crying, dog barking, fire alarms, coughing, doorbells, thunder, and more — using convolutional neural networks trained on audio spectrograms.

The trained model is converted to `.tflite` format and integrated into an Android app for real-time on-device inference, with an optional Raspberry Pi integration for remote alerting via Firebase.

---

## 🧠 ML Models Evaluated

Multiple state-of-the-art CNN architectures were compared for sound classification performance:

| Model | Notebook |
|---|---|
| InceptionV3 | `ml-notebooks/training/Inceptionv3.ipynb` |
| InceptionResNetV2 | `ml-notebooks/training/InceptionResnetV2.ipynb` |
| MobileNet | `ml-notebooks/analysis/Mobilenet_Analysis.ipynb` |
| ResNet50 | `ml-notebooks/analysis/Resnet50_Analysis.ipynb` |
| VGG | `ml-notebooks/analysis/VGG_Analysis.ipynb` |
| Xception | `ml-notebooks/analysis/Xception_Analysis.ipynb` |

Audio augmentation techniques were applied using **Librosa** and **audiomentations** to improve model generalization.

---

## 📱 Android Application

The best-performing model was converted to TensorFlow Lite (`.tflite`) and deployed on Android.

**Sound categories detected:**
- 👶 Baby Crying
- 🐕 Dog Barking
- 🔔 Doorbell
- 🔥 Fire / Smoke Alarm
- 🌩️ Thunder
- 💧 Water
- 🤧 Cough / Sneeze

**Tech Stack:**
- Language: Java
- Framework: Android SDK
- ML Inference: TensorFlow Lite
- Min SDK: Android 7.0+

---

## 🍓 Raspberry Pi Integration

A Raspberry Pi script (`raspberry-pi/SendFirebase.py`) captures audio and sends detection alerts to a Firebase Realtime Database for remote monitoring.

> ⚠️ To use this, add your own Firebase service account credentials (never commit the `.json` file — add it to `.gitignore`).

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest stable)
- Python 3.8+ with Jupyter Notebook
- TensorFlow 2.x

### Run the Android App
```bash
# Clone the repo
git clone https://github.com/YOUR_USERNAME/ambient-sound-detector.git

# Open the `app/` folder in Android Studio
# Sync Gradle → Build → Run on device or emulator
```

### Run ML Notebooks
```bash
pip install tensorflow librosa audiomentations numpy matplotlib

# Open any notebook in ml-notebooks/
jupyter notebook
```

### Raspberry Pi Setup
```bash
pip install firebase-admin pyaudio

# Add your Firebase credentials JSON (do not commit)
python raspberry-pi/SendFirebase.py
```

---

## 📂 Project Structure

```
ambient-sound-detector/
├── app/                    ← Source Code (Java + XML)
├── ml-notebooks/
│   ├── training/           ← Model training notebooks
│   └── analysis/           ← Model comparison & evaluation
├── tflite/                 ← Converted .tflite model
├── raspberry-pi/           ← Firebase alert script
├── paper/                  ← Published research paper (PDF)
├── screenshots/            ← App demo screenshots
├── INSTALLATION.md
└── README.md
```

---

## 📊 Results

Comparative analysis across all architectures is available in the analysis notebooks. The final model achieves strong accuracy on ambient sound classification benchmarks.

---

## 📜 Publication

This work was presented and published as part of a research paper. See `/paper` for the full document.

---


## 🙋 Author

**Smitha Kumar**  
[LinkedIn](https://www.linkedin.com/in/smitha-s-maganti/) | [GitHub](https://github.com/smitha16)
