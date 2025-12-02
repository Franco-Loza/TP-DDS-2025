# Sistema de Gestión para el Hotel Premier 🏨

Este repositorio contiene el código fuente y la documentación del Trabajo Práctico para la materia **Diseño de Sistemas de Información** de la UTN Santa Fe.

Este proyecto es el desarrollo de un **sistema de información** para la **gestión de un hotel**.

---
## ✨ Alcances y Funcionalidades Principales

El sistema está diseñado para cubrir las siguientes funcionalidades clave

* **Gestión de Reservas**: Permite crear y cancelar reservas de habitaciones.
* **Gestión de Huéspedes**: Incluye el alta, baja y modificación de datos de huéspedes, acompañantes y responsables de pago.
* **Asignación y Ocupación**: Facilita la asignación de habitaciones a los huéspedes (check-in).
* **Estado de Habitaciones**: Ofrece una visualización clara del estado de las habitaciones (ocupadas, reservadas, libres, fuera de servicio) por rango de fechas.
* **Facturación y Pagos**: Gestiona la facturación de estadías y consumos y el registro de los pagos correspondientes.
* **Reportes**: Genera listados de cheques en cartera e ingresos por fechas y medios de pago.

---
## 🛠️ Tecnologías Utilizadas

* **Backend**: Java 21, Spring Boot, Spring Security, Spring Data JPA.
* **Base de Datos**: PostgreSQL.
* **Frontend**: Thymeleaf para renderizado del lado del servidor, HTML, CSS y JavaScript.
* **Gestión del Proyecto**: Maven.
* **Contenerización**: Docker y Docker Compose.
* **CI/CD**: GitHub Actions para ejecución automática de tests.

---
## 🚀 Guía de Inicio Rápido (Entorno de Desarrollo)

Sigue estos pasos para levantar el proyecto en tu máquina local.

### **1. Prerrequisitos**

* Git
* JDK 21 o superior
* Docker y Docker Compose

### **2. Clonar el Repositorio**

```bash
git clone <URL-del-repositorio>
cd TP-DSI-2025
````

### **3. Levantar el Entorno**

El proyecto está configurado para funcionar con Docker. 

En la raíz del proyecto hay un archivo **.env.example**, este  archivo contiene varibles de entorno. Editar el nombre del archivo a: **.env**

Ejecuta el siguiente comando desde la raíz del repositorio (`TP-DSI-2025/`):

```bash
docker-compose --profile dev up -d --build
```

Este comando hará lo siguiente:

1.  Construirá la imagen de la aplicación Spring Boot.
2.  Levantará un contenedor para la base de datos PostgreSQL.
3.  Levantará un contenedor con pgadmin 4.
4.  Levantará un contenedor para la aplicación, activando el perfil de desarrollo (`dev`).
5.  Aplicará el script `init.sql` para inicializar la base de datos.

### **4. Acceder a la Aplicación**

Una vez que los contenedores estén en funcionamiento, podrás acceder a la aplicación en tu navegador a través de la siguiente URL:

**`http://localhost:8080`**

-----

## 👨‍💻 Equipo de Desarrollo ("Los Merge Conflicts")

  * **Loza, Franco**
  * **Nicle, Santiago** 
  * **Ramseyer, Egon Eugenio** 
  * **Scarpin, Luciano Mateo** 

### **Docente Auxiliar Asignado**

  * **Ramonda, Javier** 

