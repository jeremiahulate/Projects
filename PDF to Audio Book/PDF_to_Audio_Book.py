from pypdf import PdfReader
from gtts import gTTS
from tkinter.filedialog import *
import os

#ask the user to select the PDF file
book = askopenfilename()
#open the PDF file
reader = PdfReader(book)
numofpage = len(reader.pages)

#start full text string
fulltext = ""
#extract the text using for loop
for num in range(0, numofpage):
    page = reader.pages[0]
    text = page.extract_text()
    fulltext += text

# convert the text to MP3 using gTTS
tts = gTTS(text=fulltext, lang='en')
with open ("book.mp3", "wb") as f:
    tts.save("book.mp3")
    f.close()
print("MP3 file has been created successfully.")