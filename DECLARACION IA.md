# Declaración Inteligencia Artificial

---

## 1. Herramienta 

* **Herramienta de IA:** Modelo Google Gemini.
* **Lenguaje :** Java en Android Studio (Min SDK: API 24 / Target SDK: API 34).
* **Rol de la IA:** Interactivo para resolución de dudas, optimización de estructura de código.

---

## 💬 2. Consultas Realizadas

1. **Gestión de Credenciales (Seguridad):**
    * *Consulta:* ¿Cómo ocultar la API Key de Google Maps utilizando `local.properties` e inyectándola mediante `manifestPlaceholders` en `build.gradle.kts` para no subir claves privadas al repositorio de GitHub?
2. **Ubicación y Permisos en Tiempo de Ejecución (GPS):**
    * *Consulta:* ¿Cómo implementar `FusedLocationProviderClient` solicitando dinámicamente el permiso `ACCESS_FINE_LOCATION` con `ActivityCompat.requestPermissions()` en Java y manejando el evento de permiso denegado?
3. **Manejo de Base de Datos NoSQL:**
    * *Consulta:* ¿Cómo realizar operaciones de lectura y escritura en Firebase Cloud Firestore usando `.addOnFailureListener()` para mostrar notificaciones al usuario mediante `Toast` y evitar que la app colapse sin conexión a internet?
4. **Sensores Nativos y Compatibilidad:**
    * *Consulta:* ¿Cómo capturar el evento del sensor de proximidad (`Sensor.TYPE_PROXIMITY`) para activar una acción gestual y validar previamente si el dispositivo cuenta con el hardware necesario (`sensor != null`)?

---

## 💡 3. Aportes Integrados en el Código Fuente

* **Protección de Credenciales:** Inyección segura de variables de entorno para cumplir con la política de no publicar credenciales sensibles en repositorios públicos.
* **Robustez y Resiliencia:** Manejo asíncrono de excepciones en llamadas a la nube y control defensivo de hardware en la detección de sensores.
* **Estructura Limpia:** Definición del proyecto bajo un estilo de diseño modular y visibilidad a nivel de paquete (*package-private*) en Java.
