LumiSkin – Backend Portafolio
Backend del sistema de comercio electrónico LumiSkin, desarrollado con arquitectura de microservicios usando Spring Boot, JPA y PostgreSQL.

Proyecto académico – DUOC UC 

Estructura del Repositorio
BackendPortafolio/
├── backend_usuario/       # Microservicio de usuarios y autenticación
├── backend_inventario/    # Microservicio de productos e inventario
├── backend_pago/          # Microservicio de pagos y pedidos
├── Dockerfile             # Configuración para despliegue en Render
└── README.md

Microservicios
backend_usuario
Gestiona el registro, autenticación y roles de los usuarios del sistema.
Responsabilidades:

Registro e inicio de sesión de usuarios
Generación y validación de tokens JWT
Control de roles: CLIENTE y ADMINISTRADOR
Gestión de perfil de usuario

Estructura:
src/main/java/com/backend/backend_usuario/
├── controller/
├── dto/
├── entities/
├── repositories/
├── security/
└── services/
Tecnologías:

Spring Boot 3.5.6
Spring Security + JWT (jjwt 0.11.5)
Spring Data JPA + PostgreSQL
Lombok + Validation
Swagger/OpenAPI (springdoc 2.6.0)

backend_inventario
Gestiona el catálogo de productos, stock e imágenes.
Responsabilidades:

CRUD de productos (crear, leer, actualizar, eliminar)
Gestión de stock e inventario
Exposición del catálogo al frontend

Estructura:
src/main/java/com/inventario/backend_inventario/
├── config/
├── controller/
├── entities/
├── repositories/
└── servicios/

Tecnologías:
Spring Boot 3.5.6
Spring Data JPA + PostgreSQL
Lombok + Validation
Swagger/OpenAPI (springdoc 2.6.0)


backend_pago
Gestiona las ordenes, el proceso de pago con Transbank y el historial de compras.
Responsabilidades:

Creación y gestión de pedidos
Integración con Transbank Webpay (SDK 6.0.0)
Cálculo de totales con IVA
Historial de compras por usuario
Validación JWT para autorización

Estructura:
src/main/java/backend_pago/
├── controller/
├── dto/
├── entities/
├── repositories/
├── security/
├── securityConfig/
└── service/
Tecnologías:

Spring Boot 3.5.7
Spring Security + JWT (jjwt 0.11.5)
Spring Data JPA + PostgreSQL
Transbank SDK Java 6.0.0
Lombok
Swagger/OpenAPI (springdoc 2.6.0)


Requisitos previos
Java 17 (LTS)
Maven 3.8+
PostgreSQL (o acceso a base de datos en Supabase)
IDE recomendado: IntelliJ IDEA o VS Code

cómo ejecutar cada microservicio
1. Clonar el repositorio
bashgit clone https://github.com/CatPino/BackendPortafolio.git
cd BackendPortafolio
2. Configurar la base de datos
En cada microservicio, editar el archivo:
src/main/resources/application.properties
Configurar las variables de conexión:
propertiesspring.datasource.url=jdbc:postgresql://localhost:5432/lumiskin_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update

# JWT (solo backend_usuario y backend_pago)
jwt.secret=tu_clave_secreta
jwt.expiration=86400000

Documentación de la API (Swagger)
Cada microservicio expone su documentación:
Entrar a carpeta backend
correr microservicio ./mvnw spring-boot:run
abrir navegador http://localhost:8080/swagger-ui/index.html

Base de Datos
Todos los microservicios utilizan PostgreSQL como motor de base de datos.

Autenticación
El sistema usa JWT (JSON Web Token) para autenticación sin estado:

El cliente hace login en backend_usuario
Recibe un token JWT
Incluye el token en el header de cada petición:

Authorization: Bearer <token>

backend_pago valida el token antes de procesar pagos


Integración Transbank
El microservicio backend_pago integra Transbank Webpay mediante el SDK oficial:
xml<dependency>
    <groupId>com.github.transbankdevelopers</groupId>
    <artifactId>transbank-sdk-java</artifactId>
    <version>6.0.0</version>
</dependency>

La integración está configurada en modo integración (pruebas). Para producción se requieren credenciales reales de Transbank.


Docker / Despliegue en Render
Cada microservicio incluye un Dockerfile para despliegue en la nube:
bash# Construir imagen
docker build -t lumiskin-usuario .

# Ejecutar contenedor
docker run -p 8080:8080 lumiskin-usuario

Pruebas Unitarias
Las pruebas están ubicadas en src/test/java/ de cada microservicio.
bash# Ejecutar pruebas
./mvnw test
Las pruebas usan JUnit 5 y Mockito para simular dependencias sin necesitar conexión a base de datos.
