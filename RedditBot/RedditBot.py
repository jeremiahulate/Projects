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
            user_agent = "XxTh3PoisonxX845's artist commentor v0.1")
    print("Logged in")
    return r

#bot functions    
def run_bot(r, commentreplied):
    print("Currently running bot")

    for comment in r.subreddit('test').comments(limit=25):
        if "test" in comment.body and comment.id not in commentreplied and not comment.author != r.user.me():
            print("String with \"test\" found in comment " + comment.id)
            comment.reply("[My favorite art](https://imgur.com/a/ofz1r1n)")
            print("Replied to comment " + comment.id)
            commentreplied.append(comment.id)

            with open("commentreplied.txt", "a") as f:
                f.write(comment.id + "\n")
            
    #sleeping for 15 seconds to avoid rate limit
    time.sleep(15)
    
#function to save comments that have been replied to
def savedcomments():
    if not os.path.isfile("commentreplied.txt"):
        commentreplied = []
    else:
        with open("commentreplied.txt", "r") as f:
            commentreplied = f.read()
            commentreplied = commentreplied.split("\n")
            commentreplied = filter(None, commentreplied)
    return commentreplied

#timelimit for bot (in seconds)
timelimit = 10
r = login()
commentreplied = savedcomments()

#initializing time
start_time = time.time()

#contiuously running bot while true
while True:
    run_bot(r, commentreplied)
    
    #checking the elasped time
    elapsed_time = time.time() - start_time
    if elapsed_time > timelimit:
        print("Time limit reached. Stopping the bot.")
        break