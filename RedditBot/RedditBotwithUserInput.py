from flask import Flask, request, render_template
import praw
import config
import time
import os
import requests

#login function
def login():
    print("Logging in")
    r = praw.Reddit(username = config.username,
            password = config.password,
            client_id = config.client_id,
            client_secret = config.secret_id,
            user_agent = "XxTh3PoisonxX845's commentor v0.2")
    print("Logged in")
    return r

#bot functions    
def run_bot(r, commentreplied, subredditName, subredditcommentWord):
    print(f"Currently running bot on subreddit {subredditName} and searching for your word {subredditcommentWord} ")
    
    for comment in r.subreddit(subredditName).comments(limit=25):
        if subredditcommentWord in comment.body and comment.id not in commentreplied and comment.author != r.user.me():
            print(f"String with {subredditcommentWord} found in comment " + comment.id)
            try:
                comment.reply("[How cool. Look at this](https://imgur.com/a/ofz1r1n)")
                print(f"Replied to comment {comment.id}")
                commentreplied.append(comment.id)

                with open("commentreplied.txt", "a") as f:
                    f.write(comment.id + "\n")

            except praw.exceptions.APIException as e:
                print(f"Failed to reply to comment {comment.id}: {e}")
            
    time.sleep(30)  # Increased sleep time to avoid rate limits
    
#function to save comments that have been replied to
def savedcomments():
    if not os.path.isfile("commentreplied.txt"):
        commentreplied = []
    else:
        with open("commentreplied.txt", "r") as f:
            commentreplied = f.read().splitlines()
    return commentreplied

#timelimit for bot (in seconds)
timelimit = 10
r = login()
commentreplied = savedcomments()
subredditName = input("Enter a subreddit name (case sensitive): ")
subredditcommentWord = input("Enter a word to search for in comments pls (case sensitve): ")

#initializing time
start_time = time.time()

#contiuously running bot while true
while True:
    run_bot(r, commentreplied, subredditName, subredditcommentWord)
    
    #checking the elasped time
    elapsed_time = time.time() - start_time
    if elapsed_time > timelimit:
        print("Time limit reached. Stopping the bot.")
        break