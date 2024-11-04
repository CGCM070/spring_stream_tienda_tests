package org.iesvdm.tienda;


import lombok.extern.slf4j.Slf4j;
import org.iesvdm.tienda.modelo.Fabricante;
import org.iesvdm.tienda.modelo.Producto;
import org.iesvdm.tienda.repository.FabricanteRepository;
import org.iesvdm.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.toMap;


@Slf4j
@SpringBootTest
class TiendaApplicationTests {

    @Autowired
    FabricanteRepository fabRepo;

    @Autowired
    ProductoRepository prodRepo;

    private static Producto apply(Producto p) {
        p.setPrecio(p.getPrecio() * 1.08);
        return p;
    }

    @Test
    void testAllFabricante() {
        var listFabs = fabRepo.findAll();

        listFabs.forEach(f -> {
            System.out.println(">>" + f + ":");
            f.getProductos().forEach(System.out::println);
        });

        //probando
        List<String> nombres = fabRepo.findAll().stream()
                .map(Fabricante::getNombre)
                .toList();
        Assertions.assertEquals(9, nombres.size());
        Assertions.assertTrue(nombres.contains("Asus"));
        Assertions.assertFalse(listFabs.isEmpty());
    }

    @Test
    void testAllProducto() {
        var listProds = prodRepo.findAll();

        listProds.forEach(p -> {
            System.out.println(">>" + p + ":" + "\nProductos mismo fabricante " + p.getFabricante());
            p.getFabricante().getProductos().forEach(pF -> System.out.println(">>>>" + pF));
        });

    }


    /**
     * 1. Lista los nombres y los precios de todos los productos de la tabla producto
     */
    @Test
    void test1() {
        var listProds = prodRepo.findAll();
        var mapProds = listProds.stream()
                .collect(toMap(Producto::getNombre, Producto::getPrecio));
        mapProds.forEach((k, v) -> System.out.println(k + " - " + v));

        Assertions.assertEquals(11, mapProds.size());
        Assertions.assertTrue(mapProds.containsKey("Monitor 27 LED Full HD"));
        Assertions.assertFalse(listProds.isEmpty());
    }


    /**
     * 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares .
     */
    @Test
    void test2() {
        var listProds = prodRepo.findAll();

        var listProdsDolares = listProds.stream()
                .map(TiendaApplicationTests::apply)
                .toList();

        // recorro mi lista de Prods cogo el precio actual  y hago la suma de todos ellos
        var precioEuros = listProds.stream()
                .map(Producto::getPrecio)
                .reduce(0.0, Double::sum);
        precioEuros = BigDecimal.valueOf(precioEuros).setScale(2, HALF_UP).doubleValue();
        //recorro mi lista de ProdsDolares cogo y hago mi conversion de euros a dolares a 1.08 y hago la suma de todos ellos
        var precioDolares = listProds.stream()
                .map(TiendaApplicationTests::apply)
                .map(Producto::getPrecio)
                .reduce(0.0, Double::sum);

        //  aplico la escala a 2 decimales y hacemos un redondeo half up para la casa
        precioDolares = BigDecimal.valueOf(precioDolares).setScale(2, HALF_UP).doubleValue();
        precioDolares = BigDecimal.valueOf(precioDolares).setScale(2, HALF_UP).doubleValue();

        System.out.println("Precio total en euros: " + precioEuros
                + "\nPrecio total en dolares: " + precioDolares);

        //compruebo que la lista de productos en dolares no este vacia y que el precio en euros sea distinto al precio en dolares
        Assertions.assertEquals(11, listProdsDolares.size());
        Assertions.assertNotSame(precioEuros, precioDolares);

    }

    /**
     * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.
     */

    @Test
    void test3() {
        var listProds = prodRepo.findAll();
        //creo un map con el nombre en mayusculas y el precio del producto
        //donde el nombre en mayusculas es la clave y el precio es el valor
        var listaProdsMayus = listProds.stream()
                .collect(toMap(p -> p.getNombre().toUpperCase(), Producto::getPrecio));
        listaProdsMayus.forEach((k, v) -> System.out.println(k + " - " + v));

        Assertions.assertEquals(11, listaProdsMayus.size());
        //compruebo que el nombre en mayusculas no sea igual al nombre en minusculas
        Assertions.assertNotSame(listaProdsMayus.get("DISCO DURO SATA3 1TB"), listProds.getFirst().getNombre());

    }

    /**
     * 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.
     */
    @Test
    void test4() {
        var listFabs = fabRepo.findAll();
        //creo un map con el nombre del fabricante y los dos primeros caracteres en mayusculas
        //donde el nombre del fabricante es la clave y los dos primeros caracteres en mayusculas es el valor
        var listaFabsMayus = listFabs.stream()
                .collect(toMap(Fabricante::getNombre, f -> f.getNombre().substring(0, 2).toUpperCase()));
        listaFabsMayus.forEach((k, v) -> System.out.println(k + " - " + v));

        Assertions.assertEquals(9, listaFabsMayus.size());
        //compruebo que el nombre del fabricante no sea igual a los dos primeros caracteres en mayusculas
        Assertions.assertNotSame(listaFabsMayus.get("Asus"), listFabs.getFirst().getNombre().substring(0, 2).toUpperCase());
    }

    /**
     * 5. Lista el código de los fabricantes que tienen productos.
     */
    @Test
    void test5() {
        var listFabs = fabRepo.findAll();
        //creo una lista con los codigos de los fabricantes que tienen productos
        //si el producto no esta vacio, mapeo el codigo del fabricante
        var listaFabsCod = listFabs.stream()
                .filter(f -> !f.getProductos().isEmpty())
                .map(Fabricante::getCodigo)
                .toList();
        listaFabsCod.forEach(System.out::println);

        Assertions.assertEquals(7, listaFabsCod.size());
        //compruebo que la lista de codigos de fabricantes no este vacia
        Assertions.assertFalse(listaFabsCod.isEmpty());
    }

    /**
     * 6. Lista los nombres de los fabricantes ordenados de forma descendente.
     */
    @Test
    void test6() {
        var listFabs = fabRepo.findAll();
        // ordenamos por el orden inverso y mapeamos el nombre del fabricante
        var lisFabDesc = listFabs.stream()
//                .sorted((f1, f2) -> f2.getNombre().compareTo(f1.getNombre()))
//                .map(Fabricante::getNombre)
//                .toList();
                .sorted(comparing(Fabricante::getNombre, reverseOrder()))
                .map(Fabricante::getNombre)
                .toList();

        System.out.println(lisFabDesc);
        //compruebo que la lista de fabricantes ordenados de forma descendente no este vacia
        Assertions.assertFalse(lisFabDesc.isEmpty());
//        //compruebo que el primer fabricante sea Xiaomi
        Assertions.assertEquals("Xiaomi", lisFabDesc.getFirst());
    }

    /**
     * 7. Lista los nombres de los productos ordenados en primer lugar
     * por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
     */
    @Test
    void test7() {
        var listProds = prodRepo.findAll();
        // ordenamos por el nombre de forma ascendente y por el precio de forma descendente
        var listProdsOrd = listProds.stream()
//                .sorted((p1, p2) -> {
//                    if (p1.getNombre().compareTo(p2.getNombre()) == 0) {
//                        return Double.compare(p2.getPrecio(), p1.getPrecio()); //descendente
//                    }
//                    return p1.getNombre().compareTo(p2.getNombre());
//                }).toList();

                .sorted(comparing(Producto::getNombre).thenComparing(Producto::getPrecio, reverseOrder())).toList();
        listProdsOrd.forEach(System.out::println);
//        System.out.println("El primer producto es: " + listProdsOrd.get(0).getNombre());

        //compruebo que el primer producto sea Disco duro SATA3 1TB
        Assertions.assertEquals("Disco SSD 1 TB", listProdsOrd.getFirst().getNombre());

    }

    /**
     * 8. Devuelve una lista con los 5 primeros fabricantes.
     */
    @Test
    void test8() {
        var listFabs = fabRepo.findAll();
        //creo una lista con los 5 primeros fabricantes
        var listFabs5 = listFabs.stream()
                .limit(5)
                .toList();
        listFabs5.forEach(System.out::println);

        //compruebo que la lista de los 5 primeros fabricantes no este vacia
        Assertions.assertEquals(5, listFabs5.size());


    }

    /**
     * 9.Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta.
     */
    @Test
    void test9() {
        var listFabs = fabRepo.findAll();
        //creo una lista con los 2 fabricantes a partir del cuarto fabricante
        //salto los 3 primeros fabricantes y con skip y limit cojo los dos siguientes
        var list = listFabs.stream()
                .skip(3)
                .limit(2)
                .toList();
        list.forEach(System.out::println);

        //compruebo que la lista de los 2 fabricantes a partir del cuarto fabricante no este vacia
        Assertions.assertEquals(2, list.size());
        //compruebo que el primer fabricante sea Samsung que es el primer fabricante a partir del cuarto
        Assertions.assertEquals("Samsung", list.getFirst().getNombre());
    }

    /**
     * 10. Lista el nombre y el precio del producto más barato
     */
    @Test
    void test10() {
        var listProds = prodRepo.findAll();
        var prodMasBarato = listProds.stream()
                .min(comparingDouble(Producto::getPrecio))
                .orElse(null);
        System.out.println("El producto más barato es: " + prodMasBarato.getNombre() + " - " + prodMasBarato.getPrecio());
    }

    /**
     * 11. Lista el nombre y el precio del producto más caro
     */
    @Test
    void test11() {
        var listProds = prodRepo.findAll();

        Producto lisProdCaro = null;
        if (!listProds.isEmpty()) {
            lisProdCaro = listProds.stream()
                    .max(comparingDouble(Producto::getPrecio)).get();
        }

        System.out.println(lisProdCaro);
        System.out.println("El producto más caro es: " + lisProdCaro.getNombre() + " - " + lisProdCaro.getPrecio());
    }

    /**
     * 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
     */
    @Test
    void test12() {
        var listProds = prodRepo.findAll();
        var listProdsFab2 = listProds.stream()
                .filter(p -> p.getFabricante().getCodigo() == 2)
                .collect(toMap(Producto::getNombre, Producto::getFabricante));
        System.out.println(listProdsFab2);

        //Compruebo que la lista de productos sea igual a 2
        Assertions.assertEquals(2, listProdsFab2.size());
        //Comprebo que el codigo del fabricante sea igual a 2 - - > sobre el map que quite anteriormente
        Assertions.assertEquals(2, listProdsFab2.values().stream().findFirst().get().getCodigo());


    }

    /**
     * 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
     */
    @Test
    void test13() {
        var listProds = prodRepo.findAll();
        var listProdsMenor120 = listProds.stream()
                .filter(p -> p.getPrecio() <= 120)
                .map(Producto::getNombre)
                .toList();
        listProdsMenor120.forEach(System.out::println);

        //Compruebo que la lista de productos sea igual a 3
        Assertions.assertEquals(3, listProdsMenor120.size());
        //Compruebo que el primer producto sea Disco SSD 1 TB
        Assertions.assertEquals("Disco duro SATA3 1TB", listProdsMenor120.getFirst());
        //Compuebo que el precio del primer producto sea menor o igual a 120
        Assertions.assertTrue(listProds.stream().anyMatch(p -> p.getPrecio() <= 120));
    }

    /**
     * 14. Lista los productos que tienen un precio mayor o igual a 400€.
     */
    @Test
    void test14() {
        var listProds = prodRepo.findAll();
        var listProdsMayor400 = listProds.stream()
                .filter(p -> p.getPrecio() >= 400)
//                .map(Producto::getNombre)
//                .toList();
                .collect(toMap(Producto::getNombre, Producto::getPrecio));
        System.out.println(listProdsMayor400);

        //Compruebo que la lista de productos sea igual a 3
        Assertions.assertEquals(3, listProdsMayor400.size());
        //Compruebo que el primer producto sea Portátil Yoga 520
        Assertions.assertEquals("Portátil Yoga 520", listProdsMayor400.keySet().stream().findFirst().get());
        //Compuebo que el precio del primer producto sea mayor o igual a 400
        Assertions.assertTrue(listProds.stream().anyMatch(p -> p.getPrecio() >= 400));
    }

    /**
     * 15. Lista todos los productos que tengan un precio entre 80€ y 300€.
     */
    @Test
    void test15() {
        var listProds = prodRepo.findAll();
        var listProdsEntre80y300 = listProds.stream()
                .filter(p -> p.getPrecio() >= 80 && p.getPrecio() <= 300)
                .toList();
        listProdsEntre80y300.forEach(System.out::println);

        //Compruebo que la lista de productos sea igual a 7
        Assertions.assertEquals(7, listProdsEntre80y300.size());
        //Compruebo que el precio del primer producto sea mayor o igual a 80 y menor o igual a 300
        Assertions.assertTrue(listProdsEntre80y300.stream().anyMatch(p -> p.getPrecio() >= 80 && p.getPrecio() <= 300));

    }

    /**
     * 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
     */
    @Test
    void test16() {
        var listProds = prodRepo.findAll();
        var listProdMayor200CF6 = listProds.stream()
                .filter(p -> p.getPrecio() > 200 && p.getFabricante().getCodigo() == 6)
                .toList();
        System.out.println(listProdMayor200CF6);

        //Compruebo que la lista de productos sea igual a 1
        Assertions.assertEquals(1, listProdMayor200CF6.size());
        //Compruebo que el primer producto sea GeForce GTX 1080 Xtreme
        Assertions.assertEquals("GeForce GTX 1080 Xtreme", listProdMayor200CF6.getFirst().getNombre());
        //Compuebo que el precio del primer producto sea mayor a 200
        Assertions.assertTrue(listProdMayor200CF6.stream().anyMatch(p -> p.getPrecio() > 200));
        //Compuebo que el codigo del fabricante del primer producto sea igual a 6
        Assertions.assertTrue(listProdMayor200CF6.stream().anyMatch(p -> p.getFabricante().getCodigo() == 6));

    }

    /**
     * 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
     */
    @Test
    void test17() {
        var listProds = prodRepo.findAll();
        Set<Integer> codigos = Set.of(1, 3, 5);
        var listProdsCF135 = listProds.stream()
                .filter(p -> codigos.contains(p.getFabricante().getCodigo()))
                .toList();
        System.out.println(listProdsCF135);

        //Compruebo que la lista de productos sea igual a 5
        Assertions.assertEquals(5, listProdsCF135.size());
        //Compuebo que el codigo del fabricante del primer producto sea igual a 1, 3 o 5
        Assertions.assertTrue(listProdsCF135.stream().anyMatch(p -> codigos.contains(p.getFabricante().getCodigo())));


    }

    /**
     * 18. Lista el nombre y el precio de los productos en céntimos.
     */
    @Test
    void test18() {
        var listProds = prodRepo.findAll();
        var listProdsCentimos = listProds.stream()
                .collect(toMap(Producto::getNombre, p -> p.getPrecio() * 100));
        listProdsCentimos.forEach((k, v) -> System.out.println(k + " - " + v));

        listProdsCentimos.forEach((k, v) -> System.out.println(k + " - " + v));
        //Compruebo que la lista de productos en céntimos no este vacia
        Assertions.assertFalse(listProdsCentimos.isEmpty());
        //Compuebo que el precio del primer producto sea en céntimos utilizando el % 100
        Assertions.assertEquals(0, listProdsCentimos.values().stream().findFirst().get() % 100);
    }


    /**
     * 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
     */
    @Test
    void test19() {
        var listFabs = fabRepo.findAll();
        var listaFabConS = listFabs.stream()
//                .filter(fabricante -> fabricante.getNombre().substring(0, 1).equalsIgnoreCase("S")).toList();
                .filter(fabricante -> fabricante.getNombre().startsWith("S")).toList();
        listaFabConS.forEach(System.out::println);

        //Compruebo que la lista de fabricantes no este vacia
        Assertions.assertFalse(listaFabConS.isEmpty());
        //Compuebo que el primer fabricante empiece por la letra S
        Assertions.assertTrue(listaFabConS.getFirst().getNombre().startsWith("S"));
    }

    /**
     * 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
     */
    @Test
    void test20() {
        var listProds = prodRepo.findAll();
        var listProdsPortatil = listProds.stream()
                .filter(p -> p.getNombre().contains("Portátil"))
                .toList();
        listProdsPortatil.forEach(System.out::println);

        //Compruebo que la lista de productos no este vacia
        Assertions.assertFalse(listProdsPortatil.isEmpty());
        //Compuebo que el primer producto contenga la cadena Portátil en el nombre
        Assertions.assertTrue(listProdsPortatil.getFirst().getNombre().contains("Portátil"));
    }

    /**
     * 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.
     */
    @Test
    void test21() {
        var listProds = prodRepo.findAll();
        var listMonitorMenor215 = listProds.stream()
                .filter(p -> p.getNombre().contains("Monitor") && p.getPrecio() < 215)
                .map(Producto::getNombre)
                .toList();
        listMonitorMenor215.forEach(System.out::println);

        //Compruebo que la lista de productos no este vacia
        Assertions.assertFalse(listMonitorMenor215.isEmpty());
        //Compuebo que el primer producto contenga la cadena Monitor en el
        Assertions.assertTrue(listMonitorMenor215.stream().anyMatch(p -> p.contains("Monitor")
                && listProds.stream().anyMatch(p2 -> p2.getPrecio() < 215)));
    }

    /**
     * 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€.
     * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente).
     */
    @Test
    void test22() {
        var listProds = prodRepo.findAll();
        var listNombrePrecio120 = listProds.stream()
                .filter(p -> p.getPrecio() >= 180)
                .sorted(comparing(Producto::getPrecio, reverseOrder()).thenComparing(Producto::getNombre))
                .map(p -> p.getNombre() + " - " + p.getPrecio())
                .toList();
        System.out.println(listNombrePrecio120);
        listNombrePrecio120.forEach(System.out::println);

        //Compruebo que la lista de productos no este vacia y tenga un total de 7
        Assertions.assertEquals(7, listNombrePrecio120.size());
        //Compuebo que el primer producto sea GeForce GTX 1080 Xtreme
        Assertions.assertEquals("GeForce GTX 1080 Xtreme", listNombrePrecio120.get(0).split(" - ")[0]);
        //Compuebo que el precio del primer producto sea mayor o igual a 180
        Assertions.assertTrue(listNombrePrecio120.stream().anyMatch(p -> Double.parseDouble(p.split(" - ")[1]) >= 180));

    }

    /**
     * 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos.
     * Ordene el resultado por el nombre del fabricante, por orden alfabético.
     */
    @Test
    void test23() {
        var listProds = prodRepo.findAll();
        var listaGeneralOrd = listProds.stream()
                .sorted(comparing(p -> p.getFabricante().getNombre()))
                .map(p -> p.getNombre() + " - " + p.getPrecio() + " - " + p.getFabricante().getNombre())
                .toList();
        listaGeneralOrd.forEach(System.out::println);

        //Compruebo que la lista de productos no este vacia y tenga un total de 11
        Assertions.assertEquals(11, listaGeneralOrd.size());
        //Compuebo que el primer producto sea Monitor 24 LED Full HD
        Assertions.assertEquals("Monitor 24 LED Full HD", listaGeneralOrd.get(0).split(" - ")[0]);


    }

    /**
     * 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
     */
    @Test
    void test24() {

        var listProds = prodRepo.findAll();
        var listProdCaro = listProds.stream()
                .max(comparingDouble(Producto::getPrecio))
                //buscar otra en caso de no encontrar
                .orElse(listProds.getLast());

        System.out.println(listProdCaro);
        System.out.println("Prod : " + listProdCaro.getNombre() + " Precio : " + listProdCaro.getPrecio()
                + " Fab " + listProdCaro.getFabricante().getNombre());

        //Conpruebo que el producto más caro sea GeForce GTX 1080 Xtreme
        Assertions.assertEquals("GeForce GTX 1080 Xtreme", listProdCaro.getNombre());
        // Compruebo el precio del producto más caro
        Assertions.assertEquals(755, listProdCaro.getPrecio());
    }

    /**
     * 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
     */
    @Test
    void test25() {
        var listProds = prodRepo.findAll();
        var listProdCrucialMayor200 = listProds.stream()
                .filter(p -> p.getFabricante().getNombre().equals("Crucial") && p.getPrecio() > 200)
                .toList();
        listProdCrucialMayor200.forEach(System.out::println);

        //Compruebo que la lista tenga 1 producto
        Assertions.assertEquals(1, listProdCrucialMayor200.size());
        //Compruebo que el primer producto sea GeForce GTX 1080 Xtreme
        Assertions.assertEquals("GeForce GTX 1080 Xtreme", listProdCrucialMayor200.getFirst().getNombre());
    }

    /**
     * 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate
     */
    @Test
    void test26() {
        var listProds = prodRepo.findAll();
        Set<String> nombres = Set.of("Asus", "Hewlett-Packard", "Seagate");
        var listProdAsusHewleSegate = listProds.stream()
                .filter(p -> nombres.contains(p.getFabricante().getNombre()))
                .toList();
        listProdAsusHewleSegate.forEach(System.out::println);

        //Compruebo que la lista tenga 5 productos
        Assertions.assertEquals(5, listProdAsusHewleSegate.size());
        //Compruebo que el alguno de los fabricantes sea Asus, Hewlett-Packard o Seagate
        Assertions.assertTrue(listProdAsusHewleSegate.stream().anyMatch(p -> nombres.contains(p.getFabricante().getNombre())));

    }

    /**
     * 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que tengan un precio mayor o igual a 180€.
     * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.
     * El listado debe mostrarse en formato tabla. Para ello, procesa las longitudes máximas de los diferentes campos a presentar y compensa mediante la inclusión de espacios en blanco.
     * La salida debe quedar tabulada como sigue:
     * <p>
     * Producto                Precio             Fabricante
     * -----------------------------------------------------
     * GeForce GTX 1080 Xtreme|611.5500000000001 |Crucial
     * Portátil Yoga 520      |452.79            |Lenovo
     * Portátil Ideapd 320    |359.64000000000004|Lenovo
     * Monitor 27 LED Full HD |199.25190000000003|Asus
     */
    @Test
    void test27() {
        var listProds = prodRepo.findAll();
        var listProdMayor180 = listProds.stream()
                .filter(p -> p.getPrecio() >= 180)
                .sorted(comparing(Producto::getPrecio, reverseOrder()).thenComparing(Producto::getNombre))
                //Con String format
                .map(p -> String.format("%-25s|%2s |%1s", p.getNombre(), p.getPrecio(), p.getFabricante().getNombre()))
//                .map(p -> p.getNombre() + "          " + p.getPrecio() + " "  + p.getFabricante().getNombre())
                .toList();

        System.out.println("Producto        Precio         Fabricante");
        System.out.println("-------------------------------------------");
        listProdMayor180.forEach(System.out::println);
        //Compruebo que la lista tenga 7 productos
        Assertions.assertEquals(7, listProdMayor180.size());

    }

    /**
     * 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos.
     * El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados.
     * SÓLO SE PUEDEN UTILIZAR STREAM, NO PUEDE HABER BUCLES
     * La salida debe queda como sigue:
     * Fabricante: Asus
     * <p>
     * Productos:
     * Monitor 27 LED Full HD
     * Monitor 24 LED Full HD
     * <p>
     * Fabricante: Lenovo
     * <p>
     * Productos:
     * Portátil Ideapd 320
     * Portátil Yoga 520
     * <p>
     * Fabricante: Hewlett-Packard
     * <p>
     * Productos:
     * Impresora HP Deskjet 3720
     * Impresora HP Laserjet Pro M26nw
     * <p>
     * Fabricante: Samsung
     * <p>
     * Productos:
     * Disco SSD 1 TB
     * <p>
     * Fabricante: Seagate
     * <p>
     * Productos:
     * Disco duro SATA3 1TB
     * <p>
     * Fabricante: Crucial
     * <p>
     * Productos:
     * GeForce GTX 1080 Xtreme
     * Memoria RAM DDR4 8GB
     * <p>
     * Fabricante: Gigabyte
     * <p>
     * Productos:
     * GeForce GTX 1050Ti
     * <p>
     * Fabricante: Huawei
     * <p>
     * Productos:
     * <p>
     * <p>
     * Fabricante: Xiaomi
     * <p>
     * Productos:
     */
    @Test
    void test28() {
        var listFabs = fabRepo.findAll();


    }

    /**
     * 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
     */
    @Test
    void test29() {
        var listFabs = fabRepo.findAll();
        var listFabsSinProd = listFabs.stream()
                .filter(f -> f.getProductos().isEmpty())
                .toList();
        listFabsSinProd.forEach(System.out::println);

        //Compruebo que el tamaño de la lista sea 2
        Assertions.assertEquals(2, listFabsSinProd.size());
        //Compruebo que el primer fabricante sea Huawei y el segundo Xiaomi
        Set<String> nombres = Set.of("Huawei", "Xiaomi");
        Assertions.assertTrue(listFabsSinProd.stream().allMatch(f -> nombres.contains(f.getNombre())));

    }

    /**
     * 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
     */
    @Test
    void test30() {
        var listProds = prodRepo.findAll();
        var numProds = listProds.stream()
                .count();
        System.out.println("Número total de productos: " + numProds);

        //Compruebo que el número total de productos sea 11
        Assertions.assertEquals(11, numProds);
    }


    /**
     * 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
     */
    @Test
    void test31() {
        var listProds = prodRepo.findAll();
        var numFabsConProd = listProds.stream()
//                .filter(producto -> !producto.getFabricante().getProductos().isEmpty())
                .map(producto -> producto.getFabricante().codigo)
                .distinct()
                .toList();
        System.out.println("Número de fabricantes con productos: " + numFabsConProd.size());

        //Compruebo que el tamaño de la lista sea 7
        Assertions.assertEquals(7, numFabsConProd.size());

    }

    /**
     * 32. Calcula la media del precio de todos los productos
     */
    @Test
    void test32() {
        var listProds = prodRepo.findAll();
        var mediaPrecio = listProds.stream()
                .mapToDouble(Producto::getPrecio)
                //Reduce , moralria el average aquí .averege.orElse(Trow new exception)
                .reduce(0, (a, b) -> (a + b)) / listProds.size();

        //seteamos la escala a 2 decimales y hacemos un redondeo half up para la casa
        mediaPrecio = BigDecimal.valueOf(mediaPrecio).setScale(2, HALF_UP).doubleValue();
        System.out.println("Media del precio de todos los productos: " + mediaPrecio);

        //Compruebo que la media sea 271.72
        Assertions.assertEquals(271.72, mediaPrecio);


    }

    /**
     * 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
     */
    @Test
    void test33() {
        var listProds = prodRepo.findAll();
        var precioMasBarato = listProds.stream()
                .mapToDouble(Producto::getPrecio)
                .min()
                .orElse(0);
        System.out.println("Precio más barato de todos los productos: " + precioMasBarato);

        //Compruebo que el precio más barato sea 59.99
        Assertions.assertEquals(59.99, precioMasBarato);

    }

    /**
     * 34. Calcula la suma de los precios de todos los productos.
     */
    @Test
    void test34() {
        var listProds = prodRepo.findAll();
        var sumaPrecios = listProds.stream()
                .mapToDouble(Producto::getPrecio)
                .reduce(0, (a, b) -> a + b);

        var sumaPrecioV2 = listProds.stream()
                .mapToDouble(Producto::getPrecio)
                .reduce(0, Double::sum);

        System.out.println("Suma de los precios de todos los productos: " + sumaPrecios);
        System.out.println("Suma de los precios de todos los productos: " + sumaPrecioV2);

        //Compruebo que la suma de los precios sea 2988.96
        Assertions.assertEquals(2988.96, sumaPrecios);
        Assertions.assertEquals(2988.96, sumaPrecioV2);

    }

    /**
     * 35. Calcula el número de productos que tiene el fabricante Asus.
     */
    @Test
    void test35() {
        var listProds = prodRepo.findAll();
        var numProdsAsus = listProds.stream()
                .filter(p -> p.getFabricante().getNombre().equals("Asus"))
                .count();
        System.out.println("Número de productos del fabricante Asus: " + numProdsAsus);

        //Compruebo que el número de productos del fabricante Asus sea 2
        Assertions.assertEquals(2, numProdsAsus);

    }

    /**
     * 36. Calcula la media del precio de todos los productos del fabricante Asus.
     */
    @Test
    void test36() {
        var listProds = prodRepo.findAll();
        var mediaPrecioAsus = listProds.stream()
                .filter(p -> p.getFabricante().getNombre().equals("Asus"))
                .mapToDouble(Producto::getPrecio)
                //sin average
//                .reduce(0, (a, b) -> (a + b)) / listProds.stream()
//                .filter(p -> p.getFabricante().getNombre().equals("Asus"))
//                .count();
                .average()
                .orElseThrow(() -> new RuntimeException("No se ha podido calcular la media"));

        //seteamos la escala a 2 decimales y hacemos un redondeo half up para la casa
        mediaPrecioAsus = BigDecimal.valueOf(mediaPrecioAsus).setScale(2, HALF_UP).doubleValue();
        System.out.println("Media del precio de los productos del fabricante Asus: " + mediaPrecioAsus);

        //Compruebo que la media de los productos del fabricante Asus sea 224.0
        Assertions.assertEquals(224.0, mediaPrecioAsus);
    }


    /**
     * 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial.
     * Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
     */
    @Test
    void test37() {
        var listProds = prodRepo.findAll();

        //planificamos, tengo que hacer un reduce con un array de 4 elementos
        /***
         * [0] va ser el maximo
         * [1] va ser el minimo
         * [2] va ser la suma
         * [3] va ser el contador
         * p sera el precio
         */

        var resultado = listProds.stream()
                .filter(p -> p.getFabricante().getNombre().equals("Crucial"))
                .map(Producto::getPrecio)
               . reduce( new Double [] {0.0 , Double.MAX_VALUE, 0.0, 0.0},
                        (a, b) -> new Double[]{max(a[0], b), min(a[1], b), a[2] + b, a[3] + 1},
                        (a, b) -> new Double[]{max(a[0], b[0]), min(a[1], b[1]), a[2] + b[2], a[3] + b[3]});


                System.out.println("Precio máximo: " + resultado[0]);
                System.out.println("Precio mínimo: " + resultado[1]);
                System.out.println("Precio medio: " + resultado[2] / resultado[3]);
                System.out.println("Número total de productos: " + resultado[3]);
    }

    /**
     * 38. Muestra el número total de productos que tiene cada uno de los fabricantes.
     * El listado también debe incluir los fabricantes que no tienen ningún producto.
     * El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene.
     * Ordene el resultado descendentemente por el número de productos. Utiliza String.format para la alineación de los nombres y las cantidades.
     * La salida debe queda como sigue:
     * <p>
     * Fabricante     #Productos
     * -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
     * Asus              2
     * Lenovo              2
     * Hewlett-Packard              2
     * Samsung              1
     * Seagate              1
     * Crucial              2
     * Gigabyte              1
     * Huawei              0
     * Xiaomi              0
     */
    @Test
    void test38() {
        var listFabs = fabRepo.findAll();
        var res = listFabs.stream()
                .map(f -> f.getNombre() + " - " + f.getProductos().size())
                //metamos un espcion de 13 "" con String.format
                .sorted(comparing(s -> Integer.parseInt(s.split(" - ")[1]), reverseOrder()))
                .toList();
        System.out.println("Fabricante     #Productos");
        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        res.forEach(System.out::println);

    }

    /**
     * 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes.
     * El resultado mostrará el nombre del fabricante junto con los datos que se solicitan.
     * Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
     * Deben aparecer los fabricantes que no tienen productos.
     */
    @Test
    void test39() {
        var listFabs = fabRepo.findAll();
        //TODO
    }

    /**
     * 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€.
     * No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.
     */
    @Test
    void test40() {
        var listFabs = fabRepo.findAll();
    }

    /**
     * 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
     */
    @Test
    void test41() {
        var listFabs = fabRepo.findAll();
        var listFabs2oMas = listFabs.stream()
                .filter(f -> f.getProductos().size() >= 2)
                .map(Fabricante::getNombre)
                .toList();

        listFabs2oMas.forEach(System.out::println);

        //Compruebo que la lista de fabricantes tenga 4
        Assertions.assertEquals(4, listFabs2oMas.size());
        Set<String> nombres = Set.of("Asus", "Lenovo", "Hewlett-Packard", "Crucial");
        //Compruebo que los fabricantes sean Asus, Lenovo, Hewlett-Packard y Crucial
        Assertions.assertTrue(listFabs2oMas.stream().allMatch(nombres::contains));


    }

    /**
     * 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €.
     * Ordenado de mayor a menor número de productos.
     */
    @Test
    void test42() {
        var listFabs = fabRepo.findAll();
        record FabricanteNumProd(String nombre, int numProd) {
        }
        var listFabsPrecioMayor220 = listFabs.stream()
                .map(f -> new FabricanteNumProd(f.getNombre(),
                        (int) f.getProductos().stream().filter(p -> p.getPrecio() >= 220).count()))

                .filter(f -> f.numProd() > 0)
                .sorted(comparingInt(FabricanteNumProd::numProd).reversed())
                .toList();
        listFabsPrecioMayor220.forEach(System.out::println);

        //Compruebo que la lista de fabricantes tenga 3
        Assertions.assertEquals(3, listFabsPrecioMayor220.size());
        //Compruebo que los fabricantes sean Crucial, Lenovo y Asus
        Set<String> nombres = Set.of("Crucial", "Lenovo", "Asus");
        Assertions.assertTrue(listFabsPrecioMayor220.stream().allMatch(f -> nombres.contains(f.nombre())));
    }

    /**
     * 43.Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
     */
    @Test
    void test43() {
        var listFabs = fabRepo.findAll();
        var listFabsSumaMayor1000 = listFabs.stream()

                //usando un reduce dentro de un filter
//                .filter(f -> f.getProductos().stream().mapToDouble(Producto::getPrecio).reduce(0, (v, v1) -> v+v1) > 1000)
                .filter(f -> f.getProductos().stream().mapToDouble(Producto::getPrecio).sum() > 1000)
                .map(Fabricante::getNombre)
                .toList();
        listFabsSumaMayor1000.forEach(System.out::println);
        //Compruebo que la lista de fabricantes tenga 1
        Assertions.assertEquals(1, listFabsSumaMayor1000.size());
        //Compruebo que el fabricante sea Lenovo
        Assertions.assertEquals("Lenovo", listFabsSumaMayor1000.getFirst());
    }

    /**
     * 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
     * Ordenado de menor a mayor por cuantía de precio de los productos.
     */
    @Test
    void test44() {
        var listFabs = fabRepo.findAll();
        var listFabsSumaMayor1000 = listFabs.stream()
//                .sorted(comparingDouble(f -> f.getProductos().stream().map(Producto::getPrecio).reduce(0.0, (aDouble, aDouble2) -> aDouble+aDouble2)))  //MapToDouble y sumamos directamente
                .sorted(comparingDouble(f -> f.getProductos().stream().mapToDouble(Producto::getPrecio).sum()))
                .filter(f -> f.getProductos().stream().mapToDouble(Producto::getPrecio).sum() > 1000)
                .map(Fabricante::getNombre)
                .toList();
        listFabsSumaMayor1000.forEach(System.out::println);

        //Compruebo que la lista de fabricantes tenga 1 Y que el fabricante sea Lenovo
        Assertions.assertEquals(1, listFabsSumaMayor1000.size());
        Assertions.assertEquals("Lenovo", listFabsSumaMayor1000.getFirst());
    }

    /**
     * 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante.
     * El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante.
     * El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
     */
    @Test
    void test45() {
        var listFabs = fabRepo.findAll();
        System.out.println("Nombre del producto - Precio - Nombre del fabricante");
        System.out.println("------------------------------------------------------");
        var nomProdOrd = listFabs.stream()
                .sorted(comparing(Fabricante::getNombre))
                .map(f -> f.getProductos().stream()
                        .max(comparingDouble(Producto::getPrecio))
                        .map(p -> p.getNombre() + " -> " + p.getPrecio() + " --> " + f.getNombre())
                        .orElse(null))
                .toList();
        nomProdOrd.forEach(System.out::println);

        //Comprobamos que la lista de productos no este vacia
        Assertions.assertFalse(nomProdOrd.isEmpty());

    }

    /**
     * 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante.
     * Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
     */
    @Test
    void test46() {
        var listFabs = fabRepo.findAll();
        var listProdOrd = listFabs.stream()
                .sorted(comparing(Fabricante::getNombre))
                .flatMap(f ->f.getProductos().stream()
                        .filter(p ->p.getPrecio() >= f.getProductos().stream()
                                        .mapToDouble(Producto::getPrecio).average().orElse(0.0)
                        )
                .sorted(comparing(Producto::getPrecio,reverseOrder()))
                .map(producto -> producto.getNombre() + "" + producto.getPrecio() + "" + f.getNombre()))
                .toList();

        listProdOrd.forEach(System.out::println);

        //Compruebo que la liene 7
        Assertions.assertEquals(7, listProdOrd.size());
    }

}

