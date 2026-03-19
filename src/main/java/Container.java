/**
 * Контейнер для хранения целых чисел с автоматическим расширением внутреннего массива.
 * Позволяет добавлять, получать и удалять элементы.
 */
public class Container {
    /** Массив для хранения элементов */
    private int[] data;

    /** Текущее количество элементов */
    private int size;

    /** Начальная ёмкость по умолчанию */
    private static final int DEFAULT_LENGTH = 5;

    /**
     * Создаёт пустой контейнер с начальной ёмкостью по умолчанию.
     */
    public Container() {
        data = new int[DEFAULT_LENGTH];
        size = 0;
    }

    /**
     * Добавляет элемент в конец контейнера.
     * При необходимости внутренний массив автоматически расширяется.
     *
     * @param value добавляемое целое число
     */
    public void add(int value) {
        if (size == data.length)
            increaseCapacity();
        data[size++] = value;
    }

    /**
     * Возвращает элемент по индексу.
     *
     * @param index индекс элемента (начиная с 0)
     * @return значение элемента
     * @throws IndexOutOfBoundsException если индекс выходит за пределы допустимого диапазона
     */
    public int get(int index) {
        checkIndex(index);
        return data[index];
    }

    /**
     * Удаляет элемент по указанному индексу.
     * Элементы, следующие за удалённым, сдвигаются влево.
     *
     * @param index индекс удаляемого элемента
     * @throws IndexOutOfBoundsException если индекс некорректен
     */
    public void remove(int index) {
        checkIndex(index);
        for (int i = index; i < size - 1; i++)
            data[i] = data[i + 1];
        size--;
    }

    /**
     * Возвращает текущее количество элементов в контейнере.
     *
     * @return размер контейнера
     */
    public int size() { return size; }

    /**
     * Проверяет, пуст ли контейнер.
     *
     * @return true, если элементов нет, иначе false
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * Увеличивает ёмкость внутреннего массива.
     * Новая ёмкость вычисляется по формуле:
     * если массив пуст, то 1, иначе data.length * 3 / 2 + 1.
     */
    private void increaseCapacity() {
        int newCapacity = data.length == 0 ? 1 : data.length * 3 / 2 + 1;
        int[] newData = new int[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    /**
     * Проверяет, находится ли индекс в допустимых пределах (0 ≤ index < size).
     * Если нет, выбрасывает исключение {@link IndexOutOfBoundsException}.
     *
     * @param index проверяемый индекс
     * @throws IndexOutOfBoundsException если индекс меньше 0 или не меньше size
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
}