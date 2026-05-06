import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CollectionBenchmark {

    private final int iterations;

    public CollectionBenchmark(int iterations) {
        this.iterations = iterations;
    }

    public void runAll() {
        printHeader();

        // 1. Тест добавления в конец
        testAddEnd(new ArrayList<>(), "ArrayList");
        testAddEnd(new LinkedList<>(), "LinkedList");

        // 2. Тест добавления в начало
        testAddFirst(new ArrayList<>(), "ArrayList");
        testAddFirst(new LinkedList<>(), "LinkedList");

        // Подготовка данных для get и remove
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        fillList(arrayList);
        fillList(linkedList);

        // 3. Тест чтения из середины
        testGet(arrayList, "ArrayList");
        testGet(linkedList, "LinkedList");

        // 4. Тест удаления с начала
        testRemoveFirst(arrayList, "ArrayList");
        testRemoveFirst(linkedList, "LinkedList");
    }

    private void printHeader() {
        System.out.printf("%-20s | %-15s | %-12s | %-15s%n", "Метод", "Коллекция", "Итерации", "Время (мс)");
        System.out.println("-".repeat(70));
    }

    private void testAddEnd(List<Integer> list, String listName) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            list.add(i);
        }
        long end = System.nanoTime();
        printResult("add(E) [в конец]", listName, end - start);
    }

    private void testAddFirst(List<Integer> list, String listName) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            list.add(0, i);
        }
        long end = System.nanoTime();
        printResult("add(0, E) [в начало]", listName, end - start);
    }

    private void testGet(List<Integer> list, String listName) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            list.get(iterations / 2);
        }
        long end = System.nanoTime();
        printResult("get(index) [середина]", listName, end - start);
    }

    private void testRemoveFirst(List<Integer> list, String listName) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            list.remove(0);
        }
        long end = System.nanoTime();
        printResult("remove(0) [с начала]", listName, end - start);
    }

    // Тест перебора всех элементов
    private void testIteration(List<Integer> list, String listName) {
        long start = System.nanoTime();

        long dummySum = 0; // Искусственная нагрузка, чтобы компилятор не "вырезал" цикл

        // Используем цикл for-each (под капотом он использует Iterator коллекции)
        for (Integer element : list) {
            dummySum += element;
        }

        long end = System.nanoTime();
        printResult("Итерация (for-each)", listName, end - start);
    }

    private void fillList(List<Integer> list) {
        list.clear();
        for (int i = 0; i < iterations; i++) {
            list.add(i);
        }
    }

    private void printResult(String methodDescription, String collection, long timeNano) {
        double timeMillis = timeNano / 1_000_000.0;
        System.out.printf("%-20s | %-15s | %-12d | %.3f мс%n", methodDescription, collection, iterations, timeMillis);
    }
}