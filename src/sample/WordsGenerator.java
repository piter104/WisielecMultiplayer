package sample;

import java.util.List;
import java.util.Random;

public class WordsGenerator {
    Random random;
    // zebranie kilku słów z których program miałby losować hasło do zgadywania
    private List<String> words;

    public WordsGenerator() {
        random = new Random();

        words.add("mama");
        words.add("tata");
        words.add("kura");
        words.add("fotel");
        words.add("miska");
        words.add("choinka");
    }

    public String getRandomWord() {
        int randomNumber = random.nextInt(words.size());
        return words.get(randomNumber);
    }
}
