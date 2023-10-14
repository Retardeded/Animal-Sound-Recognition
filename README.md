# SoundRecognition

Android Studio does the whole setup, so that app runs.

But the actual recognition is done by this server:

https://github.com/Retardeded/AnimalSoundServer

Also this app is suppose to work in local Wi-Fi network, so you need to change variable (private const val BASE_URL = "http://192.168.1.3:8080") to your computer ip,
without changing 8080 port. This variable is there:

https://github.com/Retardeded/SoundRecognition/blob/master/app/src/main/java/com/plcoding/soundrecognition/di/AppModule.kt

Lastly, recognition depends on database, so you have to create your mini data base with some sounds first, that is record and upload them to server through this app,
then you can check how similar they are to sound you want to identify.
Quite useless project.
