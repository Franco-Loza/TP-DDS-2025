# 🚀 Integración Continua (CI) con GitHub Actions

Este proyecto utiliza **GitHub Actions** para ejecutar automáticamente los tests cada vez que se realiza un cambio en el código.

## 📋 ¿Qué es la Integración Continua?

La Integración Continua (CI) es una práctica de desarrollo donde los cambios de código se integran frecuentemente y se verifican automáticamente mediante tests. Esto nos ayuda a:

- ✅ **Detectar errores tempranamente**: Los tests se ejecutan automáticamente antes de integrar código
- ✅ **Mantener la calidad**: Cada cambio es verificado antes de ser fusionado
- ✅ **Aumentar la confianza**: Sabemos que el código funciona antes de enviarlo a producción
- ✅ **Acelerar el desarrollo**: Automatizamos tareas repetitivas

## 🔧 ¿Cómo Funciona Nuestro CI?

### Archivo de Configuración

El workflow de CI está definido en `.github/workflows/ci.yml`. Este archivo le dice a GitHub Actions qué hacer cuando hay cambios en el código.

### ¿Cuándo se Ejecuta?

El CI se ejecuta automáticamente en dos situaciones:

1. **Cuando haces un `push`** a las ramas `develop` o `main`
2. **Cuando creas un Pull Request** hacia `develop` o `main`

### ¿Qué Hace el CI?

Cada vez que se ejecuta, el CI realiza los siguientes pasos:

1. **Descarga el código** del repositorio
2. **Configura Java 21** (la versión que usamos en el proyecto)
3. **Levanta PostgreSQL** en un contenedor para los tests
4. **Ejecuta todos los tests** con Maven
5. **Genera reportes** de los resultados
6. **Notifica si algo falló** ❌ o si todo pasó ✅

## 📊 ¿Cómo Ver los Resultados?

### En GitHub:

1. Ve a tu Pull Request o commit en GitHub
2. Verás un ícono al lado del commit:
   - ✅ **Verde con checkmark**: Todos los tests pasaron
   - ❌ **Rojo con X**: Algún test falló
   - 🟡 **Amarillo con círculo**: Los tests están corriendo
3. Haz clic en "Details" para ver los logs completos

### Ver Logs Detallados:

1. Ve a la pestaña **"Actions"** en GitHub
2. Haz clic en el workflow que quieres ver
3. Expande los pasos para ver los detalles

## 🛠️ Configuración Técnica

### Servicios Utilizados

- **PostgreSQL 16**: Base de datos para los tests
- **JDK 21 (Temurin)**: Versión de Java compatible
- **Maven**: Para compilar y ejecutar tests

### Variables de Entorno

El CI configura automáticamente estas variables para los tests:

```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/hotel_premier_test
SPRING_DATASOURCE_USERNAME: hotel_user
SPRING_DATASOURCE_PASSWORD: hotel_password
SPRING_JPA_HIBERNATE_DDL_AUTO: create-drop
```

### Archivo de Configuración de Tests

Los tests usan `src/test/resources/application.yml` que configura:
- Conexión a PostgreSQL (en lugar de H2)
- Modo `create-drop` para crear y limpiar la BD en cada ejecución
- Logging reducido para tests más rápidos

## 🚨 ¿Qué Hacer si los Tests Fallan?

Si ves una ❌ en tu Pull Request:

1. **No entres en pánico** 😌
2. Haz clic en "Details" para ver qué test falló
3. Lee el mensaje de error
4. Corrige el problema en tu rama local
5. Haz `commit` y `push` de nuevo
6. El CI se ejecutará automáticamente otra vez

## 💡 Buenas Prácticas

- ✅ **Ejecuta los tests localmente** antes de hacer push: `mvn test`
- ✅ **Revisa los resultados del CI** antes de pedir revisión del PR
- ✅ **No ignores los tests que fallan**: Siempre corrige los errores
- ✅ **Escribe tests para nuevo código**: Mantén la cobertura alta

## 🔍 Ejemplo de Workflow

```
1. Desarrollador hace cambios en su rama feature/nueva-funcionalidad
2. Ejecuta tests localmente: mvn test ✅
3. Hace push: git push origin feature/nueva-funcionalidad
4. Crea un Pull Request hacia develop
5. GitHub Actions detecta el PR automáticamente
6. Se ejecutan los tests en un entorno limpio
7. Si todo pasa ✅, el administrador puede aprobar el PR
8. Si algo falla ❌, el desarrollador recibe notificación y corrige
```

## 📁 Archivos Relacionados

- `.github/workflows/ci.yml`: Configuración del workflow de CI
- `hotel-premier/src/test/resources/application.yml`: Configuración para tests
- `hotel-premier/pom.xml`: Dependencias y plugins de Maven

## 🎯 Beneficios para el Equipo

- **Menos bugs en producción**: Los problemas se detectan antes
- **Código más confiable**: Cada cambio es verificado
- **Revisiones más rápidas**: El reviewer sabe que los tests pasaron
- **Menos conflictos**: Los problemas se detectan al integrar, no después

---

**¿Tienes dudas sobre el CI?** Consulta con el administrador del repositorio o revisa los logs en la pestaña Actions de GitHub.
