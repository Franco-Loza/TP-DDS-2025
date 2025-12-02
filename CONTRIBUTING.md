#  Guía para el Equipo de Desarrollo y Flujo de Trabajo 🚀

Este documento es nuestra guía de trabajo. Su propósito es que todos sigamos los mismos pasos para escribir código, usar las herramientas y colaborar de forma ordenada. Seguirla es clave para evitar errores y mantener el proyecto saludable.

---
## 1. Configuración Inicial del Entorno (Una sola vez)

Antes de escribir tu primera línea de código, necesitas configurar tu computadora.

### **Paso 1: Instalar las Herramientas Esenciales** 🛠️

Asegúrate de tener instalado el siguiente software:

* **Editor de Código**: [Visual Studio Code](https://code.visualstudio.com/) o [Cursor](https://cursor.sh/).
* **Git**: [Instalador de Git](https://git-scm.com/downloads). Te permitirá versionar el código.
* **Java (JDK)**: Necesitamos la versión **21**. Puedes instalarla desde [Oracle](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html).
* **Docker Desktop**: [Instalador de Docker Desktop](https://www.docker.com/products/docker-desktop/). Nos permite correr la aplicación y la base de datos en contenedores.

### **Paso 2: Clonar el Repositorio** 📂

Para tener una copia del proyecto en tu computadora, sigue estos pasos:

1.  Navega a la carpeta donde guardas tus proyectos (por ejemplo, `Documentos/Proyectos`).
2.  Abre una terminal en esta carpeta. En Windows, te recomendamos usar **Git Bash**, que se instala junto con Git.
3.  Clona el repositorio usando el siguiente comando. Reemplaza `<URL-del-repositorio>` con la URL que encuentras en GitHub.
    ```bash
    git clone <URL-del-repositorio>
    ```
4.  Una vez clonado, entra en la carpeta del proyecto.
    ```bash
    cd TP-DSI-2025
    ```

### **Paso 3: Levantar el Proyecto por Primera Vez** 🐳

Para asegurarte de que todo funciona, vamos a levantar la aplicación con Docker.

1.  **Abre Docker Desktop** y asegúrate de que esté corriendo. Verás el ícono de la ballena en tu barra de tareas.
2.  Abrir tu editor de código (Visual Studio Code o Cursor) y abre el proyecto.
3.  En la raíz del proyecto hay un archivo ".env.example", este  archivo contiene varibles de entorno para que la app usa para conectar la base de datos, pero este archivo solo debe estar en tu PC de forma local. Para que esto funcione, debes editar el nombre del archivo, y el mismo solo debe tener el siguiente nombre:  ".env". Ahora ya tenes configuaradas las variables de entorno, continúa siguiendo los pasos.
4.  Abrir una nueva terminal en tu editor de código, ejecuta el siguiente comando:
    ```bash
    docker-compose --profile dev up -d --build
    ```
    * **¿Qué hace este comando?** La primera vez, descargará las imágenes necesarias (Java, PostgreSQL), construirá la aplicación y creará los contenedores. La primera vez demorará algunos minutos.
5. Podes probar la app ingresando las siguientes direcciones en el navegador:
   ```
   pgadmin: http://localhost:5050/ -> pass: admin
   app: http://localhost:8080/ -> puedes registrar un usuario si quieres
   ```
6.  Cuando quieras detener la aplicación, ejecuta en la consola:
    ```bash
    docker-compose --profile dev down
    ```
    * **¿Qué hace este comando?** Detiene los contenedores (no los elimina).

---
## 2. Flujo de Trabajo para Desarrollar (Para cada nueva tarea)

Este es el ciclo que seguirás cada vez que te asignen una nueva funcionalidad o corrección.

### **Paso 1: Sincronízate y Posiciónate en `develop`**

**Nunca trabajes directamente sobre la rama `main` o `develop`**. La rama `develop` contiene la última versión estable del código en desarrollo.

Ahora seguir los siguientes pasos en la terminal de **Git Bash**.

1.  Asegúrate de estar en la rama `develop`.
    ```bash
    git checkout develop
    ```
2.  Descarga los últimos cambios que otros hayan subido.
    ```bash
    git pull origin develop
    ```

### **Paso 2: Crea tu Propia Rama de Trabajo** 🌿

Por cada tarea nueva, crea una rama nueva. Esto aísla tu trabajo y evita conflictos.

1.  Usa el siguiente comando para crear y cambiarte a tu nueva rama.
    ```bash
    git checkout -b <nombre-de-la-rama>
    ```
2.  **Estrategia para nombrar ramas**: Usaremos un prefijo según el tipo de tarea.
    * `feature/`: Para una nueva funcionalidad. **Ejemplo**: `feature/CU07-facturacion`
    * `fix/`: Para corregir un error. **Ejemplo**: `fix/error-calculo-iva`
    * `docs/`: Para agregar o mejorar documentación. **Ejemplo**: `docs/actualizar-readme`

### **Paso 3: Verifica que Todo Funcione ANTES de Programar**

Antes de tocar algo, levantar el proyecto para confirmar que la base sobre la que vas a trabajar es estable.
```bash
docker-compose --profile dev up -d
````

Si todo levanta correctamente, puedes detenerlo con:

```bash
docker-compose --profile dev down
```

y empezar a programar.

### **Paso 4: ¡A Programar\! Y Haz Commits Frecuentes** 💾

Mientras trabajas, guarda tu progreso en pequeños pasos. **Enfócate solo en los cambios de tu tarea asignada**. No modifiques código que no esté relacionado.

1.  Para guardar tus cambios, primero agrégalos al "área de preparación" (staging).
    ```bash
    # El punto "." significa "todos los archivos que modificaste".
    git add .
    ```
2.  Ahora, crea un "commit", que es una foto de tu progreso con un mensaje descriptivo.
    ```bash
    git commit -m "Tipo(contexto): Mensaje descriptivo"
    ```
3.  **Patrón para mensajes de commit**:
      * **Tipo**: `feat` (nueva funcionalidad), `fix` (corrección), `docs`, `style`, `refactor`, `test`.
      * **Contexto**: El módulo o la parte del sistema en la que trabajaste (ej. `facturacion`, `reservas`).
      * **Ejemplos**:
          * `feat(facturacion): Agrega cálculo de IVA a la factura.`
          * `fix(reservas): Corrige validación de fechas en la reserva.`

### **Paso 5: Sube tu Rama a GitHub** ☁️

Una vez que hayas terminado tu tarea y hecho el último commit, sube tu rama al repositorio remoto.

**¡Pero antes comprobá que todo funciona como debería!**

```bash
git push origin <nombre-de-la-rama>
```

  * **Ejemplo**: `git push origin feature/CU07-facturacion`

### **Paso 6: Crea un Pull Request (PR)**

El Pull Request es una solicitud para incorporar tus cambios a la rama `develop`.

1.  Abre GitHub en tu navegador y ve al repositorio del proyecto.
2.  Verás una notificación para crear un **Pull Request** desde tu rama recién subida. Haz clic en ella. Sino puedes ir a la pesteña **Pull Request**, presionar en **new pull request** y en base poner develop y en compare poner tu rama, luego create **pull request**.
3.  Asegúrate de que la solicitud sea para fusionar tu rama (`feature/...`) en la rama `develop`. Allí podras visualizar los cambios que quieres introducir en la rama develop.
4.  Pon un título claro y una breve descripción de los cambios que hiciste.
5.  Crea el Pull Request y **espera a que el administrador del repositorio lo revise y lo apruebe**.

-----

## ⭐ Reglas de Oro (Recordatorios Importantes)

  * ✅ **SIEMPRE** crea una nueva rama desde `develop` para cada tarea.
  * ✅ **SIEMPRE** haz `git pull` en `develop` antes de crear una nueva rama.
  * ❌ **NUNCA** hagas `push` directamente a `main` o `develop`.
  * 🐳 **VERIFICA** que el proyecto levante con Docker antes de empezar a codificar.
  * 💬 **HAZ** commits pequeños y frecuentes con mensajes claros.

