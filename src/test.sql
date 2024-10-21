use tienda;

-- Test n1 Lista los nombres y los precios de todos los productos de la tabla producto

SELECT nombre, precio FROM producto;

-- Test n2 lista de Producto completa con el precio de euros convertido a dólares .
SELECT nombre,  ROUND(precio * 1.08 ,2) FROM producto;


-- Test n3  Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula
SELECT UPPER(nombre), precio FROM producto;

-- Test n4 Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre
SELECT nombre, UPPER(SUBSTRING(nombre, 1, 2)) as Iniciales FROM fabricante;
