# FitMe-Android
Android app written in Kotlin for workout and managing exercises

All exercises are fetched from REST API (https://wger.de/api/v2/) and displayed on the screen, sorted into categories.
User can click on an exercise and see additional information about that exercise.
User can add an exercise to his list of exercises (My Exercises).
User's exercises are divided into a "To Do" list (exercises that haven't been done yet) and a "Done" list (exercises that are already done).
In the "To Do" list user can delete an exercise from the list, choose the time he wants to perform the exercise, and begin the exercise.
After the user begins the exercise, he can start the timer which counts down the time while he is performing the exercise.
In the "Done" list user can see all exercises he has done and his progress for that day.
In the menu, the user can choose when he wants to see a report of his progress for that day. After he selects one report, he will
receive a notification about his progress at a given time.
Each new day, exercises from the "My Exercises" list are deleted and the user can start a new workout plan.

I have used:
•	Broadcast Receiver
•	Bottom Navigation
•	Retrofit, gson format
•	Room
•	SharedPreferences
•	Splash Screen
•	RecylerView
•	ViewPager
•	CardView
•	Intents
•	Dialogs
•	Menu
•	Notification Manager
•	Alarm Manager


