#a simple mouse bot that moves the mouse to random locations on the screen every 3 seconds
import pyautogui as pag
import keyboard
import random
import time

#flag to check if the program should exit
exit_program = False

#function to quit the mouse bot
def on_quit():
    print("exiting...")
    global exit_program
    exit_program = True

#register the hotkey to quit
keyboard.add_hotkey('q', on_quit)

#main loop
print("note: Press 'q' to quit the program.")
while not exit_program:
    x = random.randint(600, 700)
    y = random.randint(200, 300)
    pag.moveTo(x, y, 0.5)
    time.sleep(3)