from __future__ import unicode_literals

import speech_recognition as sr
from googletrans import Translator
from textblob import TextBlob

r = sr.Recognizer()

with sr.AudioFile('harvard.wav') as source:
    audio = r.record(source)
    try:
        print("Entered 'try', proceeding to transcribe")
        transcript = r.recognize_google(audio)
        print("Transcription finished")
    except:
        print("Something went wrong!")

translator = Translator()
translation = translator.translate(transcript, src='en', dest='en')  # TODO: change source language to Polish
translated_text = translation.text

blob = TextBlob(translated_text)

print("Text: ", transcript)
print("Polarity: ", blob.sentiment.polarity)
print("New scale polarity: ", 0.5 * (blob.sentiment.polarity + 1))
