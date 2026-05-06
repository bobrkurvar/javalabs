import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс для сравнительного тестирования производительности коллекций {@link ArrayList} и {@link LinkedList}.
 * <p>
 * Позволяет замерить время выполнения базовых операций: добавление в конец,
 * вставка в начало, чтение из середины, удаление с начала и полный перебор элементов.
 * Замеры производятся с помощью {@link System#nanoTime()} для обеспечения высокой точности.
 */
public class CollectionBenchmark {

    /**
     * Количество итераций для каждого теста.
     * Определяет размер заполняемых коллекций и количество выполняемых операций.
     */
    private final int iterations;

    /**
     * Создает новый экземпляр бенчмарка.
     *
     * @param iterations количество операций, которое будет выполнено в каждом тесте
     */
    public CollectionBenchmark(int iterations) {
        this.iterations = iterations;
    }

    /**
     * Запускает полный цикл тестов для обеих коллекций и выводит результаты в консоль
     * в виде форматированной таблицы.
     */
    public void runAll() {
        printHeader();

        // 1. Тест добавления в конец
        testAddEnd(new ArrayList<>(), "ArrayList");
        testAddEnd(new LinkedList<>(), "LinkedList");

        // 2. Тест добавления в начало
        testAddFirst(new ArrayList<>(), "ArrayList");
        testAddFirst(new LinkedList<>(), "LinkedList");

        // Подготовка данных для get, remove и итерации
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

        // 5. Тест перебора всех элементов
        testIteration(arrayList, "ArrayList");
        testIteration(linkedList, "LinkedList");
    }

    /**
     * Выводит заголовок таблицы результатов в консоль.
     */
    private void printHeader() {
        System.out.printf("%-20s | %-15s | %-12s | %-15s%n", "Метод", "Коллекция", "Итерации", "Время (мс)");
        System.out.println("-".repeat(70));
    }

    /**
     * Тестирует производительность добавления элементов в конец списка.
     *
     * @param list     пустой список для тестирования
     * @param listName строковое название типа коллекции для вывода в консоль
     */
    private void testAddEnd(List<Integer> list, String listName) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            list.add(i);
        }
        long end = System.nanoTime();
        printResult("add(E) [в конец]", listName, end - start);
    }

    /**
     * Тестирует производительность вставки элементов в начало списка (индекс 0).
     *
     * @param list     пустой список для тестирования
     * @param listName строковое название типа коллекции для вывода в консоль
     */
    private void testAddFirst(List<Integer> list, String listName) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            list.add(0, i);
        }
        long end = System.nanoTime();
        printResult("add(0, E) [в начало]", listName, end - start);
    }

    /**
     * Тестирует производительность чтения элемента из середины списка.
     *
     * @param list     предварительно заполненный список
     * @param listName строковое название типа коллекции для вывода в консоль
     */
    private void testGet(List<Integer> list, String listName) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            list.get(iterations / 2);
        }
        long end = System.nanoTime();
        printResult("get(index) [середина]", listName, end - start);
    }

    /**
     * Тестирует производительность удаления первого элемента из списка (индекс 0).
     *
     * @param list     предварительно заполненный список
     * @param listName строковое название типа коллекции для вывода в консоль
     */
    private void testRemoveFirst(List<Integer> list, String listName) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            list.remove(0);
        }
        long end = System.nanoTime();
        printResult("remove(0) [с начала]", listName, end - start);
    }

    /**
     * Тестирует производительность последовательного перебора всех элементов коллекции.
     * Используется искусственная нагрузка (суммирование), чтобы JIT-компилятор
     * не оптимизировал (не удалил) пустой цикл.
     *
     * @param list     предварительно заполненный список
     * @param listName строковое название типа коллекции для вывода в консоль
     */
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

    /**
     * Вспомогательный метод для заполнения списка заданным количеством элементов.
     * Перед заполнением список очищается.
     *
     * @param list список, который необходимо заполнить
     */
    private void fillList(List<Integer> list) {
        list.clear();
        for (int i = 0; i < iterations; i++) {
            list.add(i);
        }
    }

    /**
     * Форматирует и выводит результат одного теста в консоль.
     * Переводит время выполнения из наносекунд в миллисекунды.
     *
     * @param methodDescription описание тестируемой операции
     * @param collection        название протестированной коллекции
     * @param timeNano          время выполнения операции в наносекундах
     */
    private void printResult(String methodDescription, String collection, long timeNano) {
        double timeMillis = timeNano / 1_000_000.0;
        System.out.printf("%-20s | %-15s | %-12d | %.3f мс%n", methodDescription, collection, iterations, timeMillis);
    }
}