#!/usr/bin/python3.8
import firebase_admin
from firebase_admin import credentials, storage
import pyrebase as pb
import numpy as np
import tensorflow as tf
import librosa
import librosa.display
from scipy.io import wavfile as wav
import matplotlib.pyplot as plt
from flask import Flask, jsonify
import os


app = Flask(__name__)

cred = credentials.Certificate("/home/smiths/mysite/pythonconnect-a4e8e-firebase-adminsdk-spbff-09db3794f3.json")
firebase_admin.initialize_app(cred, {'storageBucket': 'pythonconnect-a4e8e.appspot.com'})
config = {
  "apiKey": "AIzaSyApxqOWwfaCMjpE0XwNJUU9DcxM2yQdlHw",
  "authDomain": "pythonconnect-a4e8e.firebaseapp.com",
  "databaseURL": "https://pythonconnect-a4e8e-default-rtdb.firebaseio.com",
  "projectId": "pythonconnect-a4e8e",
  "storageBucket": "pythonconnect-a4e8e.appspot.com",
  "messagingSenderId": "142506704415",
  "appId": "1:142506704415:web:384fbbf3c912469e769f2a",
  "measurementId": "G-1RFDE37KB6"
}

firebase_storage = pb.initialize_app(config)


@app.route('/')
def func():
    return "Welcome to Audial"


@app.route('/predict', methods=['GET'])
def predictSound():
    try:
        #get data from firebase
        store = firebase_storage.storage()
        bucket = storage.bucket()
        files = bucket.list_blobs()
        filename = ""
        for i in files:
            filename = i.name
            break
        blob = bucket.get_blob(filename)
        i.download_to_filename("/home/smiths/sound.wav")
        blob.delete()

        #convert wav file to spectrogram
        path = "/home/smiths/sound.wav"
        audioData, sampleRate = librosa.load(path)
        waveSampleRate, audio = wav.read(path)
        spec = librosa.stft(audioData)
        spec_mag, _ = librosa.magphase(spec)
        melScaleSpec = librosa.feature.melspectrogram(S=spec_mag, sr=waveSampleRate)
        melSpec = librosa.amplitude_to_db(melScaleSpec, ref=np.min)
        librosa.display.specshow(melSpec, sr=sampleRate, x_axis='time', y_axis='mel')
        plt.colorbar(format='%+2.0f dB')
        plt.savefig("/home/smiths/mysite/img.png")
        os.remove(path)
        plt.clf()

        #preprocess the spectrogram
        path = "/home/smiths/mysite/img.png"
        img = tf.keras.preprocessing.image.load_img(path, target_size=(224, 224))
        arr = tf.keras.preprocessing.image.img_to_array(img)
        arr_exp = np.expand_dims(arr, axis=0)
        image = tf.keras.applications.resnet50.preprocess_input(arr_exp)
        os.remove(path)

        #predict the class
        resnetPath = "/home/smiths/mysite/ResnetLatest.tflite"
        itptr = tf.lite.Interpreter(model_path = resnetPath)
        itptr.allocate_tensors()
        inp_details = itptr.get_input_details()
        out_details = itptr.get_output_details()
        inp_details[0]['shape']
        inp = image
        itptr.set_tensor(inp_details[0]['index'], inp)
        itptr.invoke()
        predictions = itptr.get_tensor(out_details[0]['index'])

        list = []
        for j in range(0, 9):
            list.append("{:.8f}".format(float(predictions[0][j])))
        val1 = max(list)

        if float(val1) > 0.20:
            val2 = np.argmax(predictions, axis=-1)[0]
            return jsonify({'result': str(val2)})
        else:
            return jsonify({'result': 'None'})
    except:
        return jsonify({'result': 'Firebase Empty'})


if __name__ == "__main__":
    app.run(debug=True)
