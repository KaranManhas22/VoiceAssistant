import pyttsx3
import date
import speech_recognition as sr
import pyaudio
import wikipedia
import webbrowser
import smtplib
import pywhatkit as kit
import pyjokes
import time
import sys

print("Your Assistant is starting......")

engine = pyttsx3.init('nsss')
voices = engine.getProperty('voices')
engine.setProperty('voice', voices[100].id)
engine.setProperty('rate', 190)

# when you start using the assistant enter your name here
Master = input("Enter your name: ")

def speak(audio):
    engine.say(audio)
    engine.runAndWait()

def list_voices():
    voices = engine.getProperty('voices')
    for index, voice in enumerate(voices):
        print(f"Voice {index}: {voice.name} ({voice.id})")

def set_voice(voice_index):
    voices = engine.getProperty('voices')
    if 0 <= voice_index < len(voices):
        engine.setProperty('voice', voices[voice_index].id)
        speak(f"Voice set to {voices[voice_index].name}")
    else:
        print("Invalid voice index")

# Call this function to list voices
list_voices()

# Allow the user to select a voice
print("Available voices:")
list_voices()
selected_voice = int(input("Enter the index of the voice you want to use: "))
set_voice(selected_voice)

def wish_user():
    hour = int(date.datetime.now().hour)
    if hour >= 0 and hour < 12:
        speak("Good Morning!" + Master)
    elif hour >= 12 and hour < 16:
        speak("Good Afternoon!" + Master)
    else:
        speak("Good Evening" + Master)
    speak("I am your Desktop-Assistant! How may I help you?")

def user_command():
    # takes microphone command and converts to string
    r = sr.Recognizer()
    with sr.Microphone() as source:
        print("I am listening....")
        r.pause_threshold = 1
        audio = r.listen(source)

    try:
        print("Recognising.....")
        query = r.recognize_google(audio, language='en-in')
        print(f"User said: {query}\n")

    except Exception as e:
        print("I am sorry I don't understand, Say that again please...")
        return "None"
    return query

def mailSent(to, content):
    server = smtplib.SMTP('smtp.gmail.com', 587)
    server.ehlo()
    server.starttls()
    # when you start working with the assistant, save this on your device
    server.login('your email', 'your app password')
    # check README.md for creating an app password
    server.sendmail('Your email', to, content)
    server.close()

if __name__ == '__main__':
    wish_user()
    while True:
        query = user_command().lower()

        if 'wikipedia' in query:
            speak('Give me some time, I am looking into Wikipedia...')
            query = query.replace("wikipedia", "")
            try:
                results = wikipedia.summary(query, sentences=5)
                speak("This is what I found!")
                speak(results)
            except wikipedia.exceptions.DisambiguationError as e:
                speak("There are multiple results. Please be more specific.")
            except wikipedia.exceptions.HTTPTimeoutError:
                speak("Sorry, the connection to Wikipedia is taking too long.")
            except wikipedia.exceptions.ConnectionError:
                speak("Sorry, I cannot connect to Wikipedia right now. Please check your internet connection.")
            except wikipedia.exceptions.RedirectError:
                speak("Sorry, I could not find any relevant information.")
            except Exception as e:
                speak("Sorry, I couldn't find anything on Wikipedia.")
                print(e)

        elif 'open youtube' in query:
            webbrowser.open("youtube.com")

        elif 'search google' in query:
            webbrowser.open("google.com")

        elif 'play music' in query:
            webbrowser.open("spotify.com")
            # you can use API as well, with the help of the spotipy module

        elif 'time' in query:
            current_time = date.datetime.now().strftime("%H:%M")
            speak(f"It's {current_time} now")

        elif 'date today' in query:
            date = date.datetime.today()
            speak(f"Today is {date}")

        elif 'send email' in query:
            try:
                speak("Please tell me the content of the email")
                content = user_command()
                speak(content)
                to = input()
                speak(to)
                mailSent(to, content)
                speak(f"Successfully sent the email to {to}")
            except Exception as e:
                print(e)
                speak("Sorry! I was unable to send the mail")

        elif 'send whatsapp message' in query:  # you should be logged in to WhatsApp Web for this
            speak("To whom should I send the message?")
            number = int(input())
            speak("Tell me the message please")
            message = user_command()

            speak("At what time should I send?")
            speak("At what time? (24-hour system)")
            hr = int(input("Hours: "))
            mins = int(input("Minutes: "))
            kit.sendwhatmsg(number, message, hr, mins)
            # this should be in the format ("+91xxxxxxxxxx", "This is message", 15, 20)

        elif 'open facebook' in query:
            webbrowser.open("facebook.com")

        elif 'make me laugh' in query:
            joke = pyjokes.get_joke()
            speak(joke)

        elif 'no thanks' in query:
            speak("Thanks for using me! Have a good day")
            sys.exit()

        time.sleep(5)
        speak("Do you have any other work?")


def get_greeting():
    return "Hello! Welcome to the assistant."

def get_joke():
    return "Why don't scientists trust atoms? Because they make up everything!"
