'''
Function: decryptLetter

Purpose:
    The `decryptLetter` function is designed to decrypt a single character by
    applying a rotation cipher technique. It rotates a given character by a
    specified value within a predefined character set, which includes numbers,
    lowercase and uppercase alphabets, and common special characters along with
    space.

Parameters:
    - letter (str): A single character string that is to be decrypted.
    - rotationValue (int): An integer value that specifies the rotation
                           offset for the decryption.

Returns:
    - str: A single character string that represents the decrypted character
           after applying the rotation.

Description:
    The function uses a string, `rotationString`, that contains all the characters
    in the sequence that are considered for the rotation cipher. It finds the
    current position of the provided `letter` within `rotationString`. Then, it
    calculates the new position by adding the `rotationValue` to the current
    index and applies modulo 95 to wrap the rotation within the bounds of
    `rotationString`'s length. The function returns the character at the new
    position in the rotation string as the decrypted character.

Example:
    - decryptLetter("e", 2) would return "g".
    - decryptLetter("Z", 4) would return "$".
    - decryptLetter("1", 5) would return "6".
    - decryptLetter("!", 94) would return "Z".

Note:
    The function assumes that `letter` is a valid single character that exists
    in the `rotationString`. If `letter` is not found in `rotationString`,
    the function behavior is not defined. Ensure that `rotationValue` is an
    integer, as other types for rotationValue will result in TypeError.
'''
import argparse
import os
import multiprocessing

#function to create matrix
def generate_matrix(seed_string, length):
    matrix = [['' for _ in range(length)] for _ in range(length)]
    index = 0
    for i in range(length):
        for j in range(length):
            matrix[i][j] = seed_string[index % len(seed_string)]
            index += 1
    return matrix


#function to update the matrix according to the rules
def update_matrix(matrix):
    updated_matrix = [['' for _ in range(len(matrix))] for _ in range(len(matrix))]
    for i in range(len(matrix)):
        for j in range(len(matrix)):
            neighbors = []

            #get the values of neighboring cells
            for x in range(max(0, i - 1), min(len(matrix), i + 2)):
                for y in range(max(0, j - 1), min(len(matrix), j + 2)):
                    if (x, y) != (i, j):
                        neighbors.append(matrix[x][y])

            #count occurrences of 'a', 'b', 'c' in neighbors
            count_a = neighbors.count('a')
            count_b = neighbors.count('b')
            count_c = neighbors.count('c')

            #determine new value based on rules
            current_cell = matrix[i][j]
            if current_cell == 'a':
                if count_a % 2 == 0:
                    updated_matrix[i][j] = 'b'
                else:
                    updated_matrix[i][j] = 'c' if count_a != 0 else 'a'
            elif current_cell == 'b':
                if count_b % 2 == 0:
                    updated_matrix[i][j] = 'c'
                else:
                    updated_matrix[i][j] = 'a' if count_b != 0 else 'b'
            else:  # current_cell == 'c'
                if count_c % 2 == 0:
                    updated_matrix[i][j] = 'a'
                else:
                    updated_matrix[i][j] = 'b' if count_c != 0 else 'c'
    return updated_matrix


#given function
def decryptLetter(letter, rotationValue):
    rotationString  = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ "
    currentPosition = rotationString.find(letter)
    return rotationString[(currentPosition + rotationValue) % 95]

#function to decrypt message
def decrypt_process(encrypted_text, seed_string, start, end, rotationString, result_queue):
    for i in range(start, min(end, len(encrypted_text))):  # range of encrypted_text
        char = encrypted_text[i]
        try:
            rotation_value = int(seed_string[i % len(seed_string)])
        except ValueError:
            rotation_value = 0 
        decrypted_char = decryptLetter(char, rotation_value)
        result_queue.put(decrypted_char)  #stores each decrypted character in the queue

#function to update cell value based on rules
def update_cell_value(cell_value, sum_neighbors):
    prime = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97}
    if cell_value == 'a':
        if sum_neighbors in prime:
            return 'a'
        elif sum_neighbors % 2 == 0:
            return 'b'
        else:
            return 'c'
    elif cell_value == 'b':
        if sum_neighbors in prime:
            return 'b'
        elif sum_neighbors % 2 == 0:
            return 'c'
        else:
            return 'a'
    elif cell_value == 'c':
        if sum_neighbors in prime:
            return 'c'
        elif sum_neighbors % 2 == 0:
            return 'a'
        else:
            return 'b'

#function to simulate steps
def simsteps(matrix, start, end):
    for step in range(start, end):
        next_matrix = [['' for _ in range(len(matrix))] for _ in range(len(matrix))]
        for i in range(len(matrix)):
            for j in range(len(matrix[i])):
                neighbors_sum = 0
                for x in range(max(0, i-1), min(len(matrix), i+2)):
                    for y in range(max(0, j-1), min(len(matrix[i]), j+2)):
                        if (x, y) != (i, j):
                            if matrix[x][y] == 'a':
                                neighbors_sum += 0
                            elif matrix[x][y] == 'b':
                                neighbors_sum += 1
                            elif matrix[x][y] == 'c':
                                neighbors_sum += 2
                next_matrix[i][j] = update_cell_value(matrix[i][j], neighbors_sum)
        matrix = next_matrix
    return matrix

def parallel_simsteps(matrix, num_processes):
    processes = []
    step_range = 100 // num_processes  # Determine the range of steps for each process

    for i in range(num_processes):
        start = i * step_range
        end = start + step_range if i < num_processes - 1 else 100
        process = multiprocessing.Process(target=simsteps, args=(matrix, start, end))
        processes.append(process)
        process.start()

    for process in processes:
        process.join()
#function to decrypt a single character using column summation
def decrypt_character(column_sum, encrypted_char):
    return decryptLetter(encrypted_char, column_sum)

#decrypting the text using the final matrix
def decrypt_using_matrix(encrypted_text, final_matrix):
    decrypted_string = ""
    for col in range(len(final_matrix[0])):
        column_sum = sum(0 if final_matrix[row][col] == 'a' else 1 if final_matrix[row][col] == 'b' else 2 for row in range(len(final_matrix)))
        decrypted_char = decrypt_character(column_sum, encrypted_text[col])
        decrypted_string += decrypted_char
    return decrypted_string


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Vigenère cipher decryption')
    parser.add_argument('-i', dest='input_file', help='Path to input file', required=True)
    parser.add_argument('-s', dest='seed_string', help='Seed string (a, b, c)', required=True)
    parser.add_argument('-o', dest='output_file', help='Path to output file', required=True)
    parser.add_argument('-p', dest='num_processes', help='Number of processes', type=int, default=1)
    args = parser.parse_args()
   
    if not os.path.exists(args.input_file):
        print("Error: Input file path does not exist.")
        exit(1)
        
    #encrypted text and key
    with open(args.input_file, 'r') as file:
        encrypted_text = file.read()
    rotationString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ "
    print("Project :: R#11683434") 
    
    #print encrypted message
    print("Encrypted Message:")
    print(encrypted_text)
    
    #generate the matrix
    seed_string = args.seed_string 
    matrix_length = len(encrypted_text)
    matrix = generate_matrix(seed_string, matrix_length)

    #simulate steps
    final_matrix = simsteps(matrix,0,100)
    decrypted_text = decrypt_using_matrix(encrypted_text, final_matrix)
    print("Decrypted Text:")
    print(decrypted_text)
    
    #write the decrypted text to the output file
    with open(args.output_file, 'w') as output_file:
        output_file.write(decrypted_text)
        

    #multiprocessing
    num_processes = args.num_processes
    iterations_per_process = 100 // num_processes

    #create a queue to store partial results
    result_queue = multiprocessing.Queue()

    #gaher results from the queue
    all_results = []
    while not result_queue.empty():
        all_results.append(result_queue.get())
        

