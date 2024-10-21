package org.iesvdm.tienda;

public class StreamsIntroduction {
    public static void main(String[] args) {
        /**
         * Streams es una secuencia de elementos que soporta operaciones secuenciales y paralelas.
         * Se compone de tres partes:
         * 1. Fuente: Colección, array, generador, etc.
         * 2. Operaciones intermedias: Devuelven un nuevo stream. Se pueden encadenar.
         * 3. Operaciones finales: Devuelven un resultado. No se pueden encadenar.
         *
         * Fuente:
         * 1. Collection.stream() -> Devuelve un stream de la colección.
         * 2. Collection.parallelStream() -> Devuelve un stream paralelo de la colección.
         * 3. Stream.of(T... values) -> Devuelve un stream de los valores.
         * 4. Arrays.stream(T[] array) -> Devuelve un stream del array.
         * 5. Stream.iterate(T seed, UnaryOperator<T> f) -> Devuelve un stream con los valores generados por la función.
         * 6. Stream.generate(Supplier<T> s) -> Devuelve un stream con los valores generados por el supplier.
         *
         * Operaciones intermedias:
         * 1. filter(Predicate<T> predicate) -> Devuelve un stream con los elementos que cumplen el predicado.
         * 2. map(Function<T, R> mapper) -> Devuelve un stream con los elementos transformados.
         * 3. flatMap(Function<T, Stream<R>> mapper) -> Devuelve un stream con los elementos transformados y aplanados.
         * 4. distinct() -> Devuelve un stream con los elementos distintos.
         * 5. sorted() -> Devuelve un stream con los elementos ordenados.
         * 6. peek(Consumer<T> action) -> Devuelve un stream con los elementos que pasan por el consumer.
         * 7. limit(long maxSize) -> Devuelve un stream con los primeros elementos.
         * 8. skip(long n) -> Devuelve un stream sin los primeros elementos.
         *
         * Operaciones finales:
         * 1. forEach(Consumer<T> action) -> Realiza la acción sobre cada elemento.
         * 2. collect(Collector<T, A, R> collector) -> Devuelve un resultado.
         * 3. reduce(BinaryOperator<T> accumulator) -> Devuelve un resultado.
         * 4. count() -> Devuelve el número de elementos.
         * 5. anyMatch(Predicate<T> predicate) -> Devuelve true si algún elemento cumple el predicado.
         * 6. allMatch(Predicate<T> predicate) -> Devuelve true si todos los elementos cumplen el predicado.
         * 7. noneMatch(Predicate<T> predicate) -> Devuelve true si ningún elemento cumple el predicado.
         * 8. findFirst() -> Devuelve el primer elemento.
         * 9. findAny() -> Devuelve cualquier elemento.
         * 10. min(Comparator<T> comparator) -> Devuelve el mínimo.
         * 11. max(Comparator<T> comparator) -> Devuelve el máximo.
         * 12. toArray() -> Devuelve un array.*
         *
         */
    }
}



