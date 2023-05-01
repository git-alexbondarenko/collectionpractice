package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestCollections {

    // 1 --------------------------------
    @Test
    void testPrintList() {
        //Распечатать содержимое используя for each
        for (HeavyBox box: list) {
            System.out.println(box);
        }
    }


    // 2 --------------------------------
    @Test
    void testChangeWeightOfFirstByOne() {
        //Изменить вес первой коробки на 1.
        HeavyBox box = list.get(0);
        box.setWeight(box.getWeight() + 1);
        assertEquals(new HeavyBox(1,2,3,5), list.get(0));
    }


    // 3 --------------------------------
    @Test
    void testDeleteLast() {
        //Удалить предпоследнюю коробку.
        list.remove(list.size()-2);
        assertEquals(6, list.size());
        assertEquals(new HeavyBox(1,2,3,4), list.get(0));
        assertEquals(new HeavyBox(1,3,3,4), list.get(list.size()-2));
    }


    // 4 --------------------------------
    @Test
    void testConvertToArray() {
        //Получить массив содержащий коробки из коллекции тремя способами и вывести на консоль.
        HeavyBox[] arr = list.toArray(new HeavyBox[0]);

        HeavyBox[] arr2 = list.stream().toArray(HeavyBox[]::new);

        HeavyBox[] arr3 = new HeavyBox[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr3[i] = list.get(i);
        }
        HeavyBox[] testArr = new HeavyBox[]{
                new HeavyBox(1,2,3,4),
                new HeavyBox(3,3,3,4),
                new HeavyBox(2,6,5,3),
                new HeavyBox(2,3,4,7),
                new HeavyBox(1,3,3,4),
                new HeavyBox(1,2,3,4),
                new HeavyBox(1,1,1,1)
        };
        assertArrayEquals(testArr, arr);

        assertArrayEquals(testArr, arr2);

        assertArrayEquals(testArr, arr3);
    }


    // 5 --------------------------------
    @Test
    void testDeleteBoxesByWeight() {
        //удалить все коробки, которые весят 4
        // list = list.stream().filter(box -> box.getWeight() != 4).toList();
        // list.removeIf(next -> next.getWeight() == 4);
        for (Iterator<HeavyBox> iterator = list.iterator(); iterator.hasNext(); ) {
            HeavyBox next = iterator.next();
            if (next.getWeight() == 4) {
                iterator.remove();
            }
        }
        assertEquals(3, list.size());
    }


    // 6 --------------------------------
    @Test
    void testSortBoxesByWeight() {
        //отсортировать коробки по возрастанию веса. При одинаковом весе - по возрастанию объема
        /*list.sort(new Comparator<HeavyBox>() {
            @Override
            public int compare(HeavyBox o1, HeavyBox o2) {
                if (o1.getWeight() > o2.getWeight()) {
                    return 1;
                } else if (o1.getWeight() < o2.getWeight()) {
                    return -1;
                } else {
                    if (o1.getVolume() > o2.getVolume()) {
                        return 1;
                    } else if (o1.getVolume() < o2.getVolume()) {
                        return -1;
                    } else return 0;
                }
            }
        });*/
        list.sort((o1, o2) -> {
            if (o1.getWeight() > o2.getWeight()) {
                return 1;
            } else if (o1.getWeight() < o2.getWeight()) {
                return -1;
            } else {
                return Double.compare(o1.getVolume(), o2.getVolume());
            }
        });

        assertEquals(new HeavyBox(1,1,1,1), list.get(0));
        assertEquals(new HeavyBox(2,3,4,7), list.get(6));
        assertEquals(new HeavyBox(1,2,3,4), list.get(3));
        assertEquals(new HeavyBox(1,3,3,4), list.get(4));
    }


    // 7 --------------------------------
    @Test
    void testClearList() {
        //Удалить все коробки.
        list.clear();
        assertTrue(list.isEmpty());
    }


    // 8 --------------------------------
    @Test
    void testReadAllLinesFromFileToList() {
        //Прочитать все строки в коллекцию
        List<String> lines = reader.lines().toList();
        assertEquals(19, lines.size());
        assertEquals("", lines.get(8));
    }


    // 9 --------------------------------
    @Test
    void testReadAllWordsFromFileToList() {
        //прочитать все строки, разбить на слова и записать в коллекцию
        List<String> words = readAllWordsFromFileToList();
        assertEquals(257, words.size());
    }

    List<String> readAllWordsFromFileToList() {
        List<String> lines = reader.lines().toList();
        List<String> words = new ArrayList<>();

        for (String line : lines) {
            if (!line.isEmpty()) {
                String[] wordsArr = line.split(REGEXP);
                words.addAll(Arrays.asList(wordsArr));
            }
        }
        return words;
    }


    // 10 -------------------------------
    @Test
    void testFindLongestWord() {
        //Найти самое длинное слово
        assertEquals("conversations", findLongestWord());
    }

    private String findLongestWord() {
        /*return readAllWordsFromFileToList().stream().sorted(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() < o2.length()) {
                    return 1;
                } else if (o1.length() > o2.length()) {
                    return -1;
                } else return 0;
            }
        }).toList().get(0);*/
        return readAllWordsFromFileToList().stream().sorted(
                (o1, o2) -> Integer.compare(o2.length(), o1.length())).toList().get(0);
    }


    // 11 -------------------------------
    @Test
    void testAllWordsByAlphabetWithoutRepeat() {
        //Получить список всех слов по алфавиту без повторов
        Set<String> words = new TreeSet<>(readAllWordsFromFileToList().stream().map(String::toLowerCase).toList());
        List<String> result = new ArrayList<>(words);

        assertEquals("alice", result.get(5));
        assertEquals("all", result.get(6));
        assertEquals("without", result.get(134));
        assertEquals(138, result.size());
    }


    // 12 -------------------------------
    @Test
    void testFindMostFrequentWord() {
        //Найти самое часто вcтречающееся слово
        assertEquals("the", mostFrequentWord());
    }

    private String mostFrequentWord() {
        Map<String, Integer> count = new HashMap<>();

        for (String word: readAllWordsFromFileToList()) {
            count.merge(word.toLowerCase(), 1, Integer::sum);
        }
        return Collections.max(count.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }


    // 13 -------------------------------
    @Test
    void testFindWordsByLengthInAlphabetOrder() {
        //получить список слов, длиной не более 5 символов, переведенных в нижний регистр, в порядке алфавита, без повторов
        Set<String> words = new TreeSet<>(readAllWordsFromFileToList().stream().map(String::toLowerCase)
                .filter(word -> word.length() < 6).toList());
        List<String> strings = new ArrayList<>(words);

        assertEquals(94, strings.size());
        assertEquals("a", strings.get(0));
        assertEquals("alice", strings.get(2));
        assertEquals("would", strings.get(strings.size() - 1));
    }







    List<HeavyBox> list;

    static final String REGEXP = "\\W+"; // for splitting into words

    private BufferedReader reader;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of(
                new HeavyBox(1,2,3,4),
                new HeavyBox(3,3,3,4),
                new HeavyBox(2,6,5,3),
                new HeavyBox(2,3,4,7),
                new HeavyBox(1,3,3,4),
                new HeavyBox(1,2,3,4),
                new HeavyBox(1,1,1,1)
        ));
    }

    @BeforeEach
    public void setUpBufferedReader() throws IOException {
        reader = Files.newBufferedReader(
                Paths.get("Text.txt"), StandardCharsets.UTF_8);
    }

    @AfterEach
    public void closeBufferedReader() throws IOException {
        reader.close();
    }
}
