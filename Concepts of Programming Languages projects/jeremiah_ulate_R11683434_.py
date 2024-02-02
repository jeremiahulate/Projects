# -*- coding: utf-8 -*-
"""
Created on Wed Nov 29 22:38:20 2023

@author: jeremiah
"""

import argparse
import os
#import multiprocessing

def generate_matrix(seed_string, length):
    matrix = [['' for _ in range(length)] for _ in range(length)]
    index = 0

    for i in range(length):
        for j in range(length):
            matrix[i][j] = seed_string[index % len(seed_string)]
            index += 1

    return matrix


# Function to update the matrix according to the rules
def update_matrix(matrix):
    updated_matrix = [['' for _ in range(len(matrix))] for _ in range(len(matrix))]
    for i in range(len(matrix)):
        for j in range(len(matrix)):
            neighbors = []

            # Get the values of neighboring cells
            for x in range(max(0, i - 1), min(len(matrix), i + 2)):
                for y in range(max(0, j - 1), min(len(matrix), j + 2)):
                    if (x, y) != (i, j):
                        neighbors.append(matrix[x][y])

            # Count occurrences of 'a', 'b', 'c' in neighbors
            count_a = neighbors.count('a')
            count_b = neighbors.count('b')
            count_c = neighbors.count('c')

            # Determine new value based on rules
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


# Function: decryptLetter (incorporated into the decryption process)
def decryptLetter(letter, rotationValue):
    rotationString  = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ "
    currentPosition = rotationString.find(letter)
    
    return rotationString[(currentPosition + rotationValue) % 95]

# Function for a single iteration of decryption
def single_iteration_decrypt(text, key,rotationString):
    decrypted_text = ""
    key_index = 0

    for char in text:
        rotation_value = rotationString.find(key[key_index])
        decrypted_text += decryptLetter(char, rotation_value)
        key_index = (key_index + 1) % len(key)

    return decrypted_text

# Function for decryption process
def decrypt_process(encrypted_text, seed_string, start, end, decrypt_letter, rotationString, result_queue):
    for i in range(start, end):
        char = encrypted_text[i]
        rotation_value = int(seed_string[i % len(seed_string)])
        decrypted_char = decrypt_letter(char, rotation_value)
        result_queue.put(decrypted_char)  # Store each decrypted character in the queue

# Function to update cell value based on rules
def update_cell_value(cell_value, sum_neighbors):
    if cell_value == 'a':
        if sum_neighbors in [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97]:
            return 'a'
        elif sum_neighbors % 2 == 0:
            return 'b'
        else:
            return 'c'
    elif cell_value == 'b':
        if sum_neighbors in [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97]:
            return 'b'
        elif sum_neighbors % 2 == 0:
            return 'c'
        else:
            return 'a'
    elif cell_value == 'c':
        if sum_neighbors in [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97]:
            return 'c'
        elif sum_neighbors % 2 == 0:
            return 'a'
        else:
            return 'b'

# Function to simulate 100 steps
def simsteps(matrix):
    for step in range(1, 101):
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
"""
seed_string_file_path = 'input_veryshort_seed.txt'
with open(seed_string_file_path, 'r') as seed_file:
    seed_string = seed_file.read().strip()  # Read the seed stri
"""
# Function to decrypt a single character using column summation
def decrypt_character(column_sum, encrypted_char):
    return decryptLetter(encrypted_char, column_sum)

# Decrypting the text using the final matrix
def decrypt_using_matrix(encrypted_text, final_matrix):
    decrypted_string = ""
    for col in range(len(final_matrix)):
        column_sum = sum(0 if final_matrix[row][col] == 'a' else 1 if final_matrix[row][col] == 'b' else 2 for row in range(len(final_matrix)))
        decrypted_char = decrypt_character(column_sum, encrypted_text[col])
        decrypted_string += decrypted_char
    
    return decrypted_string


if __name__ == '__main__':
    # Argument parsing setup
    parser = argparse.ArgumentParser(description='Vigenère cipher decryption')
    parser.add_argument('-i', dest='input_file', help='Path to input file', required=True)
    parser.add_argument('-s', dest='seed_string', help='Seed string (a, b, c)', required=True)
    parser.add_argument('-o', dest='output_file', help='Path to output file', required=True)
    parser.add_argument('-p', dest='num_processes', help='Number of processes', type=int, default=1)
    args = parser.parse_args()

    # Print TTU R# as the first line of output
    print("Project :: R#11683434")  # Replace 'R#' with your specific TTU R#
   
    if not os.path.exists(args.input_file):
        print("Error: Input file path does not exist.")
        exit(1)

    # Validate output directory existence or create if it doesn't exist
    output_directory = os.path.dirname(args.output_file)
    if not os.path.exists(output_directory):
        os.makedirs(output_directory)  # Create the directory if it doesn't exist

        
    # Encrypted text and key
    with open(args.input_file, 'r') as file:
        encrypted_text = file.read()
    
    decryption_key = args.seed_string
"""
    # Split iterations among processes
    num_processes = args.num_processes
    iterations_per_process = 100 // num_processes

    # Create a queue to store partial results
    result_queue = multiprocessing.Queue()

    # Create and start decryption processes
    processes = []
    for i in range(num_processes):
        start = i * iterations_per_process
        end = (i + 1) * iterations_per_process if i < num_processes - 1 else 100
        process = multiprocessing.Process(target=decrypt_process, args=(encrypted_text, decryption_key, start, end, result_queue))
        processes.append(process)
        process.start()

    # Wait for all processes to finish
    for process in processes:
        process.join()

    # Gather results from the queue
    all_results = []
    while not result_queue.empty():
        all_results.extend(result_queue.get())

    # Perform further calculations and decryption based on results
    # Implement the rotational algorithms to determine the original text
    # based on the sums of values for each column of the 100th iteration
    decrypted_text = ""

    # Write the decrypted text to the output file
    with open(args.output_file, 'w') as file:
        file.write(decrypted_text)
"""

"""
if __name__ == '__main__':
    # File paths for inputs
    encrypted_file_path = 'input_veryshort_encrypted.txt'
    seed_string_file_path = 'input_veryshort_seed.txt'
    output_file_path = 'decrypted.txt'  # Output file path

    # Read encrypted text from file
    with open(encrypted_file_path, 'r') as encrypted_file:
        encrypted_text = encrypted_file.read()

    # Read seed string from file
    with open(seed_string_file_path, 'r') as seed_file:
        seed_string = seed_file.read()

    rotationString  = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ "
    # Print TTU R# as the first line of output
    print("Project :: R#11683434")  # Replace 'R#' with your specific TTU R#

    
    # Get the absolute path of the output directory
    current_directory = os.getcwd()# Get current working directory
    output_directory = os.path.join(current_directory, 'output')
    if not os.path.exists(output_directory):
           os.makedirs(output_directory)  # Create the directory if it doesn't exist


    # Generate the matrix
    matrix_length = len(encrypted_text)
    matrix = generate_matrix(seed_string, matrix_length)

    # Simulate 100 steps
    final_matrix = simsteps(matrix)

    # Print the final matrix
    print("Final Matrix (Time Step 100):")
    for row in final_matrix:
        print(" ".join(row))  # Print each row of the final matrix
        # Print the encrypted text
    print("Encrypted Text:")
    print(encrypted_text)
        
    decrypted_text = decrypt_using_matrix(encrypted_text, final_matrix)
    print("Decrypted Text:")
    print(decrypted_text)
        
    # Write the decrypted text to the output file
    with open(output_file_path, 'w') as output_file:
        output_file.write(decrypted_text)

    # Split iterations among processes
    num_processes = 36  # Or the number of processes you want to use
    iterations_per_process = 100 // num_processes

     # Create a queue to store partial results
    result_queue = multiprocessing.Queue()

# Create and start decryption processes
    processes = []
    for i in range(num_processes):
            start = i * iterations_per_process
            end = (i + 1) * iterations_per_process if i < num_processes - 1 else 100
            process = multiprocessing.Process(target=decrypt_process, args=(encrypted_text, seed_string, start, end, rotationString, result_queue))
            processes.append(process)
            process.start()

# Wait for all processes to finish
    for process in processes:
        process.join()

# Gather results from the queue
    all_results = []
    while not result_queue.empty():
        all_results.extend(result_queue.get())
"""