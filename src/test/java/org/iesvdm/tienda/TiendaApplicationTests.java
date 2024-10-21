package org.iesvdm.tienda;


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

import static java.math.RoundingMode.HALF_UP;
import static java.util.stream.Collectors.toMap;


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
        Assertions.assertNotSame(listaProdsMayus.get("DISCO DURO SATA3 1TB"), listProds.get(0).getNombre());

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
        Assertions.assertNotSame(listaFabsMayus.get("Asus"), listFabs.get(0).getNombre().substring(0, 2).toUpperCase());
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
                .sorted((f1, f2) -> f2.getNombre().compareTo(f1.getNombre()))
                .map(Fabricante::getNombre)
                .toList();

        //compruebo que la lista de fabricantes ordenados de forma descendente no este vacia
        Assertions.assertFalse(lisFabDesc.isEmpty());
        //compruebo que el primer fabricante sea Xiaomi
        Assertions.assertEquals("Xiaomi", lisFabDesc.get(0));
    }

    /**
     * 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
     */
    @Test
    void test7() {
        var listProds = prodRepo.findAll();
        // ordenamos por el nombre de forma ascendente y por el precio de forma descendente
        var listProdsOrd = listProds.stream()
                .sorted((p1, p2) -> {
                    if (p1.getNombre().compareTo(p2.getNombre()) == 0) {
                        return Double.compare(p2.getPrecio(), p1.getPrecio()); //desendente
                    }
                    return p1.getNombre().compareTo(p2.getNombre());
                }).toList();
        listProdsOrd.forEach(System.out::println);
//        System.out.println("El primer producto es: " + listProdsOrd.get(0).getNombre());

        //compruebo que el primer producto sea Disco duro SATA3 1TB
        Assertions.assertEquals("Disco SSD 1 TB", listProdsOrd.get(0).getNombre());

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
        Assertions.assertEquals("Samsung", list.get(0).getNombre());
    }

    /**
     * 10. Lista el nombre y el precio del producto más barato
     */
    @Test
    void test10() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 11. Lista el nombre y el precio del producto más caro
     */
    @Test
    void test11() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
     */
    @Test
    void test12() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
     */
    @Test
    void test13() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 14. Lista los productos que tienen un precio mayor o igual a 400€.
     */
    @Test
    void test14() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 15. Lista todos los productos que tengan un precio entre 80€ y 300€.
     */
    @Test
    void test15() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
     */
    @Test
    void test16() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
     */
    @Test
    void test17() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 18. Lista el nombre y el precio de los productos en céntimos.
     */
    @Test
    void test18() {
        var listProds = prodRepo.findAll();
        //TODO
    }


    /**
     * 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
     */
    @Test
    void test19() {
        var listFabs = fabRepo.findAll();
        //TODOS
    }

    /**
     * 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
     */
    @Test
    void test20() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.
     */
    @Test
    void test21() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€.
     * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente).
     */
    void test22() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos.
     * Ordene el resultado por el nombre del fabricante, por orden alfabético.
     */
    @Test
    void test23() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
     */
    @Test
    void test24() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
     */
    @Test
    void test25() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate
     */
    @Test
    void test26() {
        var listProds = prodRepo.findAll();
        //TODO
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
        //TODO
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
        //TODO
    }

    /**
     * 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
     */
    @Test
    void test29() {
        var listFabs = fabRepo.findAll();
        //TODO
    }

    /**
     * 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
     */
    @Test
    void test30() {
        var listProds = prodRepo.findAll();
        //TODO
    }


    /**
     * 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
     */
    @Test
    void test31() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 32. Calcula la media del precio de todos los productos
     */
    @Test
    void test32() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
     */
    @Test
    void test33() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 34. Calcula la suma de los precios de todos los productos.
     */
    @Test
    void test34() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 35. Calcula el número de productos que tiene el fabricante Asus.
     */
    @Test
    void test35() {
        var listProds = prodRepo.findAll();
        //TODO
    }

    /**
     * 36. Calcula la media del precio de todos los productos del fabricante Asus.
     */
    @Test
    void test36() {
        var listProds = prodRepo.findAll();
        //TODO
    }


    /**
     * 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial.
     * Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
     */
    @Test
    void test37() {
        var listProds = prodRepo.findAll();
        //TODO
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
        //TODO
    }

    /**
     * 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes.
     * El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
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
        //TODO
    }

    /**
     * 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
     */
    @Test
    void test41() {
        var listFabs = fabRepo.findAll();
        //TODO
    }

    /**
     * 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €.
     * Ordenado de mayor a menor número de productos.
     */
    @Test
    void test42() {
        var listFabs = fabRepo.findAll();
        //TODO
    }

    /**
     * 43.Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
     */
    @Test
    void test43() {
        var listFabs = fabRepo.findAll();
        //TODO
    }

    /**
     * 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
     * Ordenado de menor a mayor por cuantía de precio de los productos.
     */
    @Test
    void test44() {
        var listFabs = fabRepo.findAll();
        //TODO
    }

    /**
     * 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante.
     * El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante.
     * El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
     */
    @Test
    void test45() {
        var listFabs = fabRepo.findAll();
        //TODO
    }

    /**
     * 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante.
     * Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
     */
    @Test
    void test46() {
        var listFabs = fabRepo.findAll();
        //TODO
    }

}
