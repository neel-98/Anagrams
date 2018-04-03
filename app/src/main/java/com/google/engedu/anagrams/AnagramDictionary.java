/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashMap<String,ArrayList<String>> lettersToWord = new HashMap<>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<>();
    int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            if (lettersToWord.containsKey(sortedWord)) {
                ArrayList<String> anagrams = lettersToWord.get(sortedWord);
                anagrams.add(word);
                lettersToWord.put(sortedWord, anagrams);
            } else {
                ArrayList<String> anagrams = new ArrayList<String>();
                anagrams.add(word);
                lettersToWord.put(sortedWord, anagrams);
            }

            int length = word.length();
            if (sizeToWords.containsKey(length)) {
                ArrayList<String> words = sizeToWords.get(length);
                words.add(word);
                sizeToWords.put(length, words);
            } else {
                ArrayList<String> words = new ArrayList<String>();
                words.add(word);
                sizeToWords.put(length, words);
            }
        }
    }
    public String sortLetters(String s){
        char[] c = s.toCharArray();
        Arrays.sort(c);
        return String.valueOf(c);
    }

    public boolean isGoodWord(String word, String base) {
        return (wordSet.contains(word) && !word.contains(base));
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char c='a';c<='z';c++){
            String s = word + c;
            s=sortLetters(s);
            if(lettersToWord.containsKey(s)){
                result.addAll(lettersToWord.get(s));
            }
        }
        for(int i=result.size()-1;i>=0;i--){
            if(!isGoodWord(result.get(i),word)){
                result.remove(i);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String randomWord = "";
        ArrayList<String> anagrams = new ArrayList<String>();

        while (anagrams.size() < MIN_NUM_ANAGRAMS) {

            randomWord = "";
            anagrams = new ArrayList<String>();

            while (randomWord.length() != wordLength) {
                randomWord = wordList.get(random.nextInt(wordList.size()));
            }
            anagrams = getAnagramsWithOneMoreLetter(randomWord);
        }

        if (wordLength != MAX_WORD_LENGTH) {
            wordLength++;
        }

        return randomWord;

    }


}

