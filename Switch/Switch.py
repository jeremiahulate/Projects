import pygame
import time

# Initialize pygame mixer
pygame.mixer.init()

# Load the sound file
firstsound = pygame.mixer.Sound(r'C:\Users\jeremiah\source\repos\Switch\firstgunshot.mp3')
secondsound = pygame.mixer.Sound(r'C:\Users\jeremiah\source\repos\Switch\secondgunshot.mp3')

count = 100
while True:
    print(f"Fa Ammo: {count}")
    if count > 0:
        count -= 1
        try:
            firstsound.play()
        except pygame.error as e:
            print(f"Error playing sound: {e}")
            break
        time.sleep(0.1)
    else:
        count = 100
        print("reloading...")
        try:
            secondsound.play()
        except pygame.error as e:
            print(f"Error playing sound: {e}")
            break
        time.sleep(5)
        break
