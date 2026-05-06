public class Main {

    public static void main(String[] args) {
        int iterations = 10000;

        System.out.println("=== Запуск тестирования коллекций на " + iterations + " элементов ===\n");

        CollectionBenchmark benchmark = new CollectionBenchmark(iterations);
        benchmark.runAll();
    }
}