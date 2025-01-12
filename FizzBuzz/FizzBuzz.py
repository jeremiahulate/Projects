#if the number is divisible by 3, print Fizz
#if the number is divisible by 5, print Buzz
#if the number is divisible by 3 and 5, print FizzBuzz
#else print the number

for i in range(1, 101):
    x = ""
    if i % 3 == 0:
        x = "Fizz"
    if i % 5 == 0:
        x += "Buzz"
    print(i if not x else x)