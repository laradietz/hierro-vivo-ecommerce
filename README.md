# Hierro Vivo — API + Tienda de E-commerce

API REST y tienda web completas para **Hierro Vivo**, un emprendimiento de
herrería artesanal (portones, rejas, muebles y piezas a medida). Construido
con Java 21 + Spring Boot. **Sin envíos**: todo se retira en el local.

## Dominio

```
Usuarios (con rol ADMIN o CUSTOMER)
    ↓
Categorías
    ↓
Productos (con días estimados de fabricación)
    ↓
Carrito
    ↓
Encargos (pedidos) — con fecha estimada de retiro
    ↓
Pagos
    ↓
Notificaciones
```

A diferencia de un e-commerce tradicional, acá no hay entidad de envío: cada
producto tiene un campo `productionDays` (cuántos días tarda en fabricarse
si no hay stock listo), y al generar un encargo se calcula automáticamente
la **fecha estimada de retiro** según la pieza que más tarda.

## Tecnologías

- Java 21 · Spring Boot 3 (Web, Data JPA, Validation, Security)
- Spring Security + JWT
- PostgreSQL · Maven · Docker / Docker Compose
- Swagger / OpenAPI (springdoc-openapi)
- JUnit 5 + Mockito
- Frontend propio en español: tienda pública (sin login para navegar el
  catálogo), con barra promocional animada, categorías, carrito, encargos,
  WhatsApp y subida de fotos de producto.

## Cómo correrlo

### Con Docker (recomendado)

```bash
docker compose up --build
```

Levanta Postgres + la API. Al arrancar crea automáticamente:
- un usuario **admin** (`admin` / `admin123`)
- un catálogo de ejemplo de herrería (portones, rejas, muebles, herrería
  artística, parrillas, reparaciones)

La tienda queda en `http://localhost:8080`. Swagger UI: `/swagger-ui.html`.

### Local, con tu propio PostgreSQL

1. Creá una base `hierrovivo`.
2. Ajustá `src/main/resources/application.properties` si tu usuario/
   contraseña no son `postgres`/`postgres`.
3. `mvn spring-boot:run` (Hibernate crea las tablas solo).

## Usar la tienda

`http://localhost:8080` — el catálogo se puede navegar **sin iniciar
sesión** (como una tienda real). Se pide login recién al armar el carrito o
generar un encargo.

- **Admin**: `admin` / `admin123` — gestiona Productos (con fotos y días de
  fabricación), Categorías, los carteles de la barra promocional y Todos los Encargos (cambia el estado:
  Pendiente → Confirmado → En fabricación → Listo para retirar → Retirado).
- **Cliente**: se registra desde la tienda, arma su carrito, genera el
  encargo (con notas/especificaciones opcionales: medidas, color,
  terminación), lo paga (simulado) y lo retira en el local cuando está listo.

## WhatsApp

- Botón flotante 💬 visible en toda la tienda, para consultas.
- Al generar un encargo, aparece un botón "Avisar por WhatsApp" con el
  pedido ya redactado.
- **Antes de usarlo en serio**, cambiá el número: en
  `src/main/resources/static/index.html`, buscá `WHATSAPP_NUMBER` (al
  principio del `<script>`) y poné el número real (código de país + área,
  sin `+` ni espacios, ej. `5491112345678`).
- Es un link "click to chat" (gratis) — el cliente tiene que apretar el
  botón. Un aviso 100% automático sin intervención requiere la API oficial
  de WhatsApp Business (de pago, con aprobación de Meta) — fuera del
  alcance de este proyecto.

## Fotos de productos

Desde el panel de administración se puede subir una foto real (JPG/PNG/
WEBP/GIF, hasta 5 MB) para cada producto — no hace falta pegar una URL
externa. Se guardan en una carpeta `uploads/` fuera del `.jar`, servida en
`/uploads/<archivo>`. Con Docker, esa carpeta vive en un volumen
(`hierrovivo_uploads`) para no perderse al reiniciar el contenedor.

## Roles y permisos

| Acción                                       | CUSTOMER | ADMIN |
|-----------------------------------------------|:--------:|:-----:|
| Ver catálogo (sin login)                       |    ✔     |   ✔   |
| Crear/editar productos y categorías            |          |   ✔   |
| Armar su propio carrito y generar encargos     |    ✔     |       |
| Ver/cancelar sus propios encargos              |    ✔     |       |
| Ver todos los encargos                         |          |   ✔   |
| Cambiar el estado de un encargo                |          |   ✔   |
| Pagar un encargo propio                        |    ✔     |       |

Un cliente **no puede** ver, pagar ni cancelar el encargo de otro cliente
(verificado del lado del servidor).

## Tests

```bash
mvn test
```

Tests unitarios con JUnit 5 + Mockito sobre `ProductService` y `CartService`.

## Notas de diseño

- Los controllers **nunca** devuelven entidades de JPA directamente, siempre
  DTOs armados a mano — evita los problemas de serialización "lazy" de
  Hibernate.
- Los enums se guardan como texto simple (`VARCHAR`), no como tipos `ENUM`
  nativos de Postgres.
- Cada operación sobre un encargo o pago verifica que le pertenezca al
  usuario autenticado (o que sea ADMIN).
- El pago está **simulado**: no hay integración real con Mercado Pago,
  Stripe, etc.
