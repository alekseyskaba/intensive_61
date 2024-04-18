import jdk.internal.util.ArraysSupport;

import java.util.*;

/**
 * Реализация списка по домашнему заданию.
 * @author Алексей Скаба
 */
public class ArrayList_AlekseySkaba<E> implements IntensiveList<E>{

    private final int INIT_SIZE = 16;
    private final int CUT_RATE = 4;
    private Object[] array;
    private int size = 0;

    /**
     * Конструктор класса.
     */
    public ArrayList_AlekseySkaba() {
        this.array = new Object[INIT_SIZE];
    }

    /**
     * Проверяет корректность индекса.
     */
    private boolean indexCheck(int index){
        if(index >= 0 && index < size)
            return true;
        else
            return false;
    }

    /**
     * Изменяет размер массива.
     */
    private void resize(int newLength) {
        Object[] newArray = new Object[newLength];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    /**
     * Возвращает размер списка.
     * @return размер списка.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Добавляет элемент в конец списка.
     * @param element Элемент, который хотим добавить.
     */
    @Override
    public void add(E element) {
        if(size == array.length - 1)
            resize(array.length * 2);
        array[size++] = element;
    }

    /**
     * Добавлят элемент по индексу.
     * @param index Индекс, по которому хотим добавить элемент.
     * @param element Элемент, который хотим добавить.
     */
    @Override
    public void add(int index, E element) {
        if(indexCheck(index)){
            if(size == array.length - 1)
                resize(array.length * 2);
            for (int i = size; i > index; i--)
                array[i] = array[i - 1];
            array[index] = element;
        } else
            throw new ArrayIndexOutOfBoundsException("Переданное значение больше размера массива");
    }

    /**
     * Возвращает элемент по индексу.
     * @param index Индекс элемента, который хотим вернуть.
     * @return Возвращает элемент.
     */
    @Override
    public E get(int index) {
        if (indexCheck(index)) {
            return (E) array[index];
        } else
            throw new ArrayIndexOutOfBoundsException("Переданное значение больше размера массива");
    }

    /**
     * Заменяет элемент списка на другой.
     * @param index Индекс элемента, который хотим заменить.
     * @param element Элемент, на который меняем.
     * @return Возвращает элемент, который заменили.
     */
    @Override
    public E set(int index, E element) {
        if (indexCheck(index)) {
            Object returnedElement = array[index];
            array[index] = element;
            return (E)returnedElement;
        } else
            throw new ArrayIndexOutOfBoundsException("Переданное значение больше размера массива");
    }

    /**
     * Удаляет элемент.
     * @param index Индекс элемента, который хотим удалить
     * @return Возвращает удалённый элемент.
     */
    @Override
    public E remove(int index) {
        Object returnedElement = array[index];
        for (int i = index; i< size; i++)
            array[i] = array[i+1];
        array[size] = null;
        size--;
        if (array.length > INIT_SIZE && size < array.length / CUT_RATE)
            resize(array.length/2);
        return (E)returnedElement;
    }

    /**
     * Удаляет все элементы, capacity приводит к дефолтному.
     */
    @Override
    public void clear() {
        Object[] defaultArray = new Object[INIT_SIZE];
        size = 0;
        array = defaultArray;
    }


    /**
     * Быстрая сортировка.
     */
    private void quickSortHelper(Comparator<E> comparator, Object[] sortArr, int low, int high){

        //завершить,если массив пуст или уже нечего делить
        if (sortArr.length == 0 || low >= high) return;

        //выбираем опорный элемент
        int middle = low + (high - low) / 2;
        Object border = sortArr[middle];

        //разделияем на подмассивы и меняем местами
        int i = low, j = high;
        while (i <= j) {
            while (comparator.compare((E)sortArr[i], (E)border) < 0) i++;
            while (comparator.compare((E)sortArr[j], (E)border) > 0) j--;
            if (i <= j) {
                Object swap = sortArr[i];
                sortArr[i] = sortArr[j];
                sortArr[j] = swap;
                i++;
                j--;
            }
        }

        //рекурсия для сортировки левой и правой части
        if (low < j) quickSortHelper(comparator, sortArr, low, j);
        if (high > i) quickSortHelper(comparator, sortArr, i, high);

    }

    /**
     * Метод для вызова быстрой сортировки списка.
     * @param comparator компаратор, с помощью которого будет сортировать.
     */
    @Override
    public void quickSort(Comparator<E> comparator) {
        quickSortHelper(comparator, array, 0, size - 1);
    }

    /**
     * Проверяет, отсортирован ли список.
     * @return true, если отсортирован. false, если не отсортирован.
     */
    @Override
    public boolean isSorted() {
        for (int i = 0; i < size - 1; i++) {
            if (((Comparable)array[i + 1]).compareTo(array[i]) < 0) {
                return false; // It is proven that the array is not sorted.
            }
        }
        return true;
    }

    /**
     * Обрезает список до переданного размера.
     * @param size Размер списка, который хотим получить.
     */
    @Override
    public void split(int size) {
        if (size < this.size) {
            Object[] newArray = new Object[size];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
            this.size = size;
        } else
            throw new ArrayIndexOutOfBoundsException("Список меньше или равен переданному значению.");
    }

    /**
     * main для тестирования методов списка.
     * @param args
     */
    public static void main(String[] args) {
        ArrayList_AlekseySkaba<Integer> testArray = new ArrayList_AlekseySkaba<>();

        for (int i = 0; i < 50; i++) {
            testArray.add((int)(Math.random()*100));
        }

        System.out.println("Размер списка: " + testArray.size);
        System.out.println(testArray.isSorted());

        System.out.println("Перед сортировкой ----------------------");
        for (int i = 0; i < testArray.size; i++){
            System.out.println(testArray.get(i));
        }

        testArray.quickSort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        System.out.println("После сортировки ----------------------");
        for (int i = 0; i < testArray.size; i++){
            System.out.println(testArray.get(i));
        }
        System.out.println(testArray.isSorted());

        testArray.remove(3);
        System.out.println("После удаления элемента --------------------");
        System.out.println("Размер списка: " + testArray.size);
        for (int i = 0; i < testArray.size; i++){
            System.out.println(testArray.get(i));
        }

        System.out.println("После обрезания списка --------------------");
        testArray.split(7);
        System.out.println("Размер списка: " + testArray.size);
        for (int i = 0; i < testArray.size; i++){
            System.out.println(testArray.get(i));
        }
    }
}
