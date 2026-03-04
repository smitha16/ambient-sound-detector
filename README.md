# Ambient Sound Detector

This is an end-to-end ML project that detects and classifies ambient sounds in real time using deep learning. The model runs on an Android app via TensorFlow Lite, with a Flask backend hosted on PythonAnywhere handling the inference pipeline.

---

## Published Paper

**Smart Sound System Applied for the Extensive Care of People with Hearing Impairment**

Published in the *International Journal of Ambient Systems and Applications (IJASA)*, Vol. 10, No. 1/2/3, September 2022.

DOI: [10.5121/ijasa.2022.10301](https://doi.org/10.5121/ijasa.2022.10301)

The full paper is available at `AmbientSoundDetectorPaper.pdf`.

---

## What This Project Does

This project was built for people with hearing impairment (PWHI) who cannot perceive sounds in their surroundings. The system listens to the environment, identifies what sound is occurring, and alerts the user through their phone.

Here is how it works:
1. A Raspberry Pi 3 with a USB microphone continuously records audio from the surroundings and uploads it to Firebase Storage
2. A Flask server picks up the audio, converts it to a mel spectrogram, and runs it through the ResNet50 TFLite model
3. The result is sent back to the Android app as a JSON response
4. The app shows the detected sound and notifies the user via vibration or torch light

---

## Models Trained and Compared

Six CNN architectures were trained using transfer learning on the FreeSound and Google AudioSet dataset:

| Model | Accuracy | Recall | Precision | F1 Score |
|---|---|---|---|---|
| **ResNet50** | **1.000** | **1.000** | **1.000** | **1.000** |
| Xception | 1.000 | 1.000 | 1.000 | 1.000 |
| MobileNetV1 | 0.902 | 0.905 | 0.908 | 0.892 |
| InceptionResNetV2 | 0.719 | 0.697 | 0.766 | 0.712 |
| InceptionV3 | 0.595 | 0.598 | 0.593 | 0.571 |
| VGG16 | 0.563 | 0.558 | 0.596 | 0.536 |

ResNet50 and Xception both hit 100% accuracy on the test set. ResNet50 was chosen for deployment because it is lighter, faster to load, and uses less memory than Xception.

Training setup: Adam optimizer (lr 0.001), 15 epochs, 224x224 spectrogram input, 80/10/10 train/val/test split, with Time Shift, Pitch Shift, and Time Stretch augmentation applied.

Notebooks for all models are in `ml-notebooks/`.

---

## Android App

The app is built in Java using Android Studio. It polls the Flask server every 10 seconds and displays any detected sounds as cards.

**Sounds detected:**

| # | Sound | # | Sound |
|---|---|---|---|
| 0 | Baby Crying | 5 | Fire Alarm |
| 1 | Dog Barking | 6 | Smoke Detector |
| 2 | Cough | 7 | Sneeze |
| 3 | Dishes and Pots | 8 | Thunder |
| 4 | Doorbell | 9 | Water |

Each sound card shows the name, an image, and a discard button. Once a card is discarded, the same sound can trigger a new notification again. Users can also switch between vibration and torch light as their preferred alert mode.

Tech stack: Java, Android Studio, TensorFlow Lite, Volley, Min SDK Android 7.0+

---

## Flask Server

`server/server.py` is the backend that does all the heavy lifting. It is hosted on PythonAnywhere.

| Method | Route | Description |
|---|---|---|
| GET | `/` | Health check |
| GET | `/predict` | Runs inference and returns the predicted sound class |

When `/predict` is called, the server fetches the latest WAV file from Firebase Storage, converts it to a Log-Mel Spectrogram using Librosa, preprocesses it to 224x224, runs it through `ResnetLatest.tflite`, and returns the class index. If the model confidence is below 20%, it returns `"None"`.

Example response:
```json
{ "result": "0" }
```

Tech stack: Python 3.8, Flask, TensorFlow Lite, Librosa, Firebase Admin SDK, Pyrebase

> The server currently has hardcoded Firebase credentials that should be moved to a `.env` file before pushing to GitHub. Never commit the Firebase service account `.json` file.

Setup:
```bash
cd server/
pip install -r requirements.txt
python server.py
```

---

## Raspberry Pi

`Raspberry/SendFirebase.py` records 5-second audio clips at 48000 Hz in WAV format using `arecord` on a Raspberry Pi 3, then uploads them to Firebase Storage using the `pyrebase` library.

```bash
pip install firebase-admin pyaudio
python Raspberry/SendFirebase.py
```

---

## Getting Started

**Prerequisites:** Android Studio, Python 3.8+, TensorFlow 2.x

**Android app:**
```bash
git clone https://github.com/smitha16/ambient-sound-detector.git
# Open the app/ folder in Android Studio, sync Gradle, and run
```

**ML notebooks:**
```bash
pip install tensorflow librosa audiomentations numpy matplotlib
jupyter notebook
```

---

## Project Structure

```
ambient-sound-detector/
├── app/
│   ├── src/
│   │   ├── main/
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle
│   └── proguard-rules.pro
├── ml-notebooks/
│   ├── analysis/
│   └── training/
├── Raspberry/
│   └── SendFirebase.py
├── server/
│   ├── ResnetLatest.tflite
│   └── server.py
├── AmbientSoundDetectorPaper.pdf
├── INSTALLATION.txt
└── README.md
```

---

## Author

**Smitha S Maganti**
[LinkedIn](https://www.linkedin.com/in/smitha-s-maganti/) | [GitHub](https://github.com/smitha16)
