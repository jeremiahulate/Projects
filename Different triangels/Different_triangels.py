#ascending triangle
rows1 = 6
for i in range(rows1):
    for j in range(i):
        print(i, end = ' ')
    print(' ')

#descending  triangle
rows2 = 5
a = 0
for i in range(rows2, 0, -1):
    a += 1
    for j in range(1, i + 1):
        print(a, end=' ')
    print('\r')

# upside down triangle using astriks
rows3 = 5
b = 2 * rows3 - 2
for i in range(rows3, -1, -1):
    for j in range(b, 0, -1):
        print(end=" ")
    b = b + 1
    for j in range (0, i + 1):
        print("*", end= " ")
    print("")

#printing an E
n = 7
for i in range(n):
    for j in range(n):
        if i == 0 or i == n-1 or i == n//2 or j == 0:
            print("*", end="")
        else:
            print(" ", end="")
    print()

# printing an A
n = 5
for i in range(n):
    for j in range(n):
        if j == 6 - i or i == 3 and j >= 3 and j <= 7 or j == 4 + i:
            print("+", end="")
        else:
            print (" ", end="")
    print()