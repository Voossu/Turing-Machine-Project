# Turing-Machine-Project

Implementation of the rule interpreter of many Turing tape machines. 


## Prerequisites

- JDK 1.8
- IDEA 2019


## Sample Task

Verification of the affiliation of words to the language L={w<sup>R</sup>ww<sup>R</sup>|w∈{0,1}}.


### A set of rules

Counting the number of letters in the word w

    q1 0 λ -> q2 0 λ R N
    q1 1 λ -> q2 1 λ R N
    q1 λ λ -> q4 λ λ L R
    q2 0 λ -> q3 0 λ R N
    q2 1 λ -> q3 1 λ R N
    q2 λ λ -> q7 λ - L R
    q3 0 λ -> q1 0 # R L
    q3 1 λ -> q1 1 # R L
    q3 λ λ -> q7 λ - L R

Copy the word w to 2 TM tape (copying from the end of the word on the first tape, replacing the characters of counting a third of the word on the second tape).

    q4 0 # -> q4 0 0 L R
    q4 1 # -> q4 1 1 L R
    q4 λ λ -> qz λ + R N
    q4 0 λ -> q5 0 λ N L
    q4 1 λ -> q5 1 λ N L

Check the second word from the beginning of the first TM tape.

    q5 0 0 -> q5 0 0 L L
    q5 1 1 -> q5 1 1 L L
    q5 0 1 -> q7 0 - L R
    q5 1 0 -> q7 1 - L R
    q5 0 λ -> q6 0 λ N R
    q5 1 λ -> q6 1 λ N R

Verification of the first word from the beginning of the first TM tape.

    q6 0 0 -> q6 0 0 L R
    q6 1 1 -> q6 1 1 L R
    q6 0 1 -> q7 0 - L R
    q6 1 0 -> q7 1 - L R
    q6 λ λ -> q8 λ + N L

Clear the second tape to the right of the answer and move the carriage of the first tape to the start of the word.

    q7 0 0 -> q7 0 λ L R
    q7 0 1 -> q7 0 λ L R
    q7 1 0 -> q7 1 λ L R
    q7 1 1 -> q7 1 λ L R
    q7 0 # -> q7 0 λ L R
    q7 1 # -> q7 1 λ L R
    q7 λ 0 -> q7 λ λ N R
    q7 λ 1 -> q7 λ λ N R
    q7 λ # -> q7 λ λ N R
    q7 0 λ -> q7 0 λ L L
    q7 1 λ -> q7 1 λ L L
    q7 λ λ -> q7 λ λ N L
    q7 0 - -> q8 0 - L L
    q7 1 - -> q8 1 - L L
    q7 λ - -> q8 λ - N L
    q7 0 + -> q8 0 + L L
    q7 1 + -> q8 1 + L L
    q7 λ + -> q8 λ + N L

Clear the second tape to the left of the answer and move the carriage of the first ribbon to the beginning of the word.

    q8 0 0 -> q8 0 λ L L
    q8 0 1 -> q8 0 λ L L
    q8 1 0 -> q8 1 λ L L
    q8 1 1 -> q8 1 λ L L
    q8 λ 0 -> q8 λ λ N L
    q8 λ 1 -> q8 λ λ N L
    q8 0 λ -> q8 0 λ N R
    q8 1 λ -> q8 1 λ N R
    q8 λ λ -> q8 λ λ R R
    q8 λ - -> q8 λ - R N
    q8 λ + -> q8 λ + R N
    q8 0 - -> qz 0 - N N
    q8 0 + -> qz 0 + N N
    q8 1 - -> qz 1 - N N
    q8 1 + -> qz 1 + N N


### The complexity of the algorithm

![complexity](https://raw.githubusercontent.com/Voossu/Turing-Machine-Project/master/img/complexity.png)


### Demonstration

![True Screen](https://raw.githubusercontent.com/Voossu/Turing-Machine-Project/master/img/screen_true.png)

![False Screen](https://raw.githubusercontent.com/Voossu/Turing-Machine-Project/master/img/screen_false.png)
