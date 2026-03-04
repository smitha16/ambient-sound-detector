import pyrebase as pb
import os
import shutil
import time

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

     
def uploadFirebase(path, filename, count):
    storage = firebase_storage.storage()
    storage.child(str(count) + filename).put(path) #link to spectrogram
    print("uploaded")
    os.remove(path)


if __name__ == "__main__":

    source = r"/home/pi/sound_recorder/realtime/source/"
    final = r"/home/pi/sound_recorder/realtime/Data/"
    folder = "/home/pi/sound_recorder/realtime/Data"
    while(True):
        print("8 secs")
        time.sleep(8)
        for file_name in os.listdir(source):
        
            source1 = source + file_name
            destination = final + file_name
            
            if os.path.isfile(source1):
                
                
                shutil.copy(source1, destination)
                print('copied', file_name)
                os.remove(source1)
     
        for count, filename in enumerate(os.listdir(folder)):
            audio_file_path = folder + "/" + filename
            #spec(audio_file_path,count)
            uploadFirebase(audio_file_path, filename, count)
            
           

   

