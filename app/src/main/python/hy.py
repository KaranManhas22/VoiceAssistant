# import wikipedia
# import webbrowser
# import pyjokes
# import datetime
#
# def get_greeting(name="User"):
#     hour = datetime.datetime.now().hour
#     if hour < 12:
#         return f"Good Morning, {name}!"
#     elif hour < 16:
#         return f"Good Afternoon, {name}!"
#     else:
#         return f"Good Evening, {name}!"
#
# def get_time():
#     return datetime.datetime.now().strftime("%H:%M")
#
# def get_date():
#     return datetime.datetime.today().strftime("%Y-%m-%d")
#
# def search_wikipedia(query):
#     try:
#         result = wikipedia.summary(query, sentences=2)
#         return result
#     except wikipedia.exceptions.DisambiguationError:
#         return "There are multiple results. Please be more specific."
#     except Exception:
#         return "I couldn't find anything on Wikipedia."
#
# def get_joke():
#     return pyjokes.get_joke()
#
# def handle_command(command, name="User"):
#     command = command.lower()
#     if "wikipedia" in command:
#         query = command.replace("wikipedia", "").strip()
#         return search_wikipedia(query)
#     elif "time" in command:
#         return f"The time is {get_time()}"
#     elif "date" in command:
#         return f"Today's date is {get_date()}"
#     elif "joke" in command or "laugh" in command:
#         return get_joke()
#     elif "greet" in command or "hello" in command:
#         return get_greeting(name)
#     else:
#         return "Sorry, I didn't understand that command."
