![UTN_imagen](https://www.vectorlogo.es/wp-content/uploads/2021/02/logo-vector-universidad-tecnologica-nacional.jpg)

# Food Store - Sistema de Gestión de Pedidos

## Descripción del proyecto
Food Store es una aplicación de consola desarrollada en Java para la gestión de pedidos de comida.
El sistema permite administrar categorías, productos, usuarios y pedidos mediante operaciones CRUD, aplicando Programación Orientada a Objetos, persistencia con JDBC y base de datos MySQL.

El proyecto fue desarrollado como Trabajo Práctico Integrador de la materia Programación 2.

---
## Institución
Universidad Tecnológica Nacional

Tecnicatura Universitaria en Programación

---
## Integrantes:
- Castro, Exequiel
- Cajales, Benjamin
- Piriz, Agustina
- Senatore, Genaro

---

# Tecnologías utilizadas
[![My Skills](https://skillicons.dev/icons?i=java,mysql,git,github&perline=4)](https://skillicons.dev)
- Java 21
- NetBeans
- MySQL
- MySQL Connector/J
- Git y GitHub

---

# Funcionalidades principales
El sistema permite realizar las siguientes operaciones desde un menú de consola:

Categorías
- Listar categorías.
- Crear categorías.
- Editar categorías.
- Eliminar categorías mediante baja lógica.

Productos

- Listar productos.
- Crear productos asociados a una categoría.
- Editar productos.
- Eliminar productos mediante baja lógica.
- Listar productos por categoría.

Usuarios

- Listar usuarios.
- Crear usuarios.
- Editar usuarios.
- Eliminar usuarios mediante baja lógica.
- Validar que el mail sea único.

Pedidos

- Crear pedidos asociados a un usuario.
- Agregar uno o varios detalles al pedido.
- Calcular subtotales y total del pedido.
- Listar pedidos.
- Ver detalles de un pedido.
- Actualizar estado y forma de pago.
- Eliminar pedidos mediante baja lógica.

---

# Arquitectura del proyecto

El proyecto está organizado por capas para separar responsabilidades:

```
src/
└── integrado/
  └── prog2/    
      ├── config/
      ├── dao/
      ├── entities/
      ├── enums/
      ├── exception/
      ├── interfaces/
      ├── service/
      └── ui/
```

Paquetes principales

- "config": contiene la clase de conexión a la base de datos.
- "entities": contiene las clases del modelo de dominio.
- "enums": contiene los enums utilizados por el sistema.
- "interfaces": contiene interfaces como "Calculable".
- "dao": contiene las clases encargadas del acceso a datos mediante JDBC.
- "service": contiene la lógica de negocio y validaciones.
- "ui": contiene el menú de consola.
- "exception": contiene excepciones propias del sistema.

---

# Base de datos

El proyecto utiliza una base de datos MySQL llamada:

`pedidos_db`

Para crear la base de datos y las tablas necesarias, ejecutar el archivo:

`schema.sql`

El script crea las siguientes tablas:

- "categoria"
- "producto"
- "usuario"
- "pedido"
- "detalle_pedido"

También incluye datos iniciales de prueba para categorías, productos y usuarios.

---

# Configuración de conexión
La configuración de conexión se encuentra en el archivo:
`
persistence.xml
`
Ejemplo de configuración:
```
<persistence>
    <properties>
        <property name="jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="jdbc.url" value="jdbc:mysql://localhost:3306/pedidos_db?useSSL=false&amp;serverTimezone=UTC"/>
        <property name="jdbc.user" value="root"/>
        <property name="jdbc.password" value=""/>
    </properties>
</persistence>
```
Si el usuario de MySQL tiene contraseña, modificar esta línea:

`
<property name="jdbc.password" value="TU_CONTRASEÑA"/>
`

---

# Driver MySQL

Para que el proyecto funcione correctamente, debe estar agregado el driver MySQL Connector/J.

Archivo utilizado:

`mysql-connector-j-8.0.33.jar`

En NetBeans se agrega desde:

Project Properties > Libraries > Classpath > Add JAR/Folder

---
# Cómo ejecutar el proyecto

1. Clonar el repositorio:

git clone LINK_DEL_REPOSITORIO

2. Abrir el proyecto en NetBeans.

3. Verificar que esté configurado JDK 21.

4. Ejecutar el archivo "schema.sql" en MySQL Workbench o phpMyAdmin.

5. Revisar usuario y contraseña en "persistence.xml".

6. Agregar el driver MySQL Connector/J si no está agregado.

7. Ejecutar la clase principal:

`Main.java`

---

# Flujo de prueba recomendado

Para verificar el funcionamiento completo del sistema, se recomienda probar:

1. Listar categorías.
2. Crear una categoría.
3. Editar una categoría.
4. Eliminar una categoría.
5. Listar productos.
6. Crear un producto asociado a una categoría.
7. Editar un producto.
8. Eliminar un producto.
9. Listar usuarios.
10. Crear un usuario.
11. Editar un usuario.
12. Eliminar un usuario.
13. Crear un pedido con detalles.
14. Listar pedidos.
15. Ver detalle de pedido.
16. Actualizar estado y forma de pago de un pedido.
17. Eliminar un pedido.

---

# Reglas de negocio implementadas

- No se permite crear productos con precio negativo.
- No se permite crear productos con stock negativo.
- No se permite crear pedidos sin usuario.
- No se permite crear detalles con cantidad menor o igual a cero.
- El mail del usuario debe ser único.
- Las eliminaciones son lógicas mediante el campo "eliminado".
- El total del pedido se calcula a partir de los subtotales de sus detalles.
- La creación de pedidos y detalles utiliza transacciones JDBC para evitar inconsistencias.

---

# Repositorio

Link al repositorio del proyecto:

https://github.com/genarosenatore/TPI-Prog-2-Castro-Exequiel-Cajales-Benjamin-Piriz-Agustina-Senatore-Genaro

---

# Video demostrativo

Link al video demostrativo:

FALTA AGREGAR LINK DEL VIDEO

---

# Documentación académica y técnica

Link o archivo PDF de documentación:

[Ver documentación en PDF](./Documentacion_Academica_Tecnica_TPI_Food_Store.pdf)

---

# Estado del proyecto
Proyecto desarrollado para entrega académica.
El sistema compila, ejecuta desde consola y permite realizar las operaciones principales solicitadas en la consigna.
