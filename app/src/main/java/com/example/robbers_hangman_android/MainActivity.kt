package com.example.robbers_hangman_android


import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.os.Handler

import kotlin.random.Random
import kotlin.math.floor

class MainActivity : ComponentActivity() {
    private val wordList = WordList().wordList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextInput = findViewById<EditText>(R.id.editText)
        val editTextOutput = findViewById<EditText>(R.id.editTextOutput)
        val editTextOther = findViewById<EditText>(R.id.editTextOther)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val wordNew = getNewWord()
        val thisWord = GuessWord(wordNew)
        var guessLeft = thisWord.returnGuessLeft()
        val guessLeftInit = guessLeft
        var wordProg = thisWord.returnWordHidden()
        var containsUnderscore = wordProg.contains('_')
        val handler = Handler()

        editTextOutput.setText("Word: $wordProg \n\n\nGuesses left: $guessLeft \nGuess character...")

        submitButton.setOnClickListener {
            val inputUser = editTextInput.text.toString()
            if (inputUser == "reveal") {
                editTextOther.setText("Help called. Word -> $wordNew")
                handler.postDelayed({
                    editTextOther.setText("")
                }, 1000)

            }
            if (!inputUser.isNullOrEmpty()) {
                editTextInput.text.clear()
                var inputChar = inputUser[0]
                thisWord.revealLetter(inputChar)
                guessLeft = thisWord.returnGuessLeft()
                wordProg = thisWord.returnWordHidden()
                containsUnderscore = wordProg.contains('_')

                editTextOutput.setText("Word: $wordProg \nGuesses left: $guessLeft Guess character...")
                if (guessLeft == guessLeftInit - 1 && containsUnderscore) {
                    imageView.setImageResource(R.drawable.hangman_1)
                } else if (guessLeft == guessLeftInit - 2 && containsUnderscore) {
                    imageView.setImageResource(R.drawable.hangman_2)
                } else if (guessLeft == 0 && containsUnderscore) {
                    imageView.setImageResource(R.drawable.hangman_3)
                    editTextOutput.setText("You lost! Big Ounce is dead :( The word was $wordNew")
                } else if (!containsUnderscore) {
                    editTextOutput.setText("You won! Big Ounce lives :) The word is $wordNew")
                }
            }
        }
    }
    private fun getNewWord(): String {
        val randomIndex = Random.nextInt(wordList.size)
        return wordList[randomIndex]
    }
}

class GuessWord(private val word: String) {
    private val wordLenght = word.length
    private var wordHidden = '_'.toString().repeat(wordLenght)
    private val uniqueChars = mutableSetOf<Char>()

    init {
        for (char in word) {
            uniqueChars.add(char)
        }
    }
    private val nbrLetters = uniqueChars.size
    private var guessLeft =  floor(nbrLetters.toDouble() / 3).toInt() + 3

    fun returnGuessLeft(): Int {
        return guessLeft
    }

    fun revealLetter(letter: Char) {
        var charIndex = word.indexOf(letter)
        val wordhiddenCa = wordHidden.toCharArray()
        if (charIndex == -1) {
            guessLeft -= 1
        }
        while (charIndex != -1) {
            if (charIndex >= 0) {
                wordhiddenCa[charIndex] = letter
            }
            charIndex = word.indexOf(letter, charIndex + 1)
        }
        wordHidden = String(wordhiddenCa)
    }

    fun returnWordHidden(): String {
        return wordHidden
    }
}