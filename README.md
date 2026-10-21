# GeoRegistro - App de Gestión de Incidencias

Aplicación Android desarrollada en Java para el registro, consulta y visualización de incidencias en terreno.

---

##  1. Instrucciones de Creación e Instalación

1. **Abrir el Proyecto:** Abrir Android Studio, seleccionar **Open** y buscar la carpeta raíz `GeoRegistro`.
2. **Versiones de Android:** 
3. ```android {
   namespace = "cl.ipvg.georegistro"
   compileSdk = 34

   defaultConfig {
   applicationId = "cl.ipvg.georegistro"
   minSdk = 24
   targetSdk = 34
   versionCode = 1
   versionName = "1.0"
   }
   // ...
   ```
4. **Sincronización:** Esperar a que Gradle descargue las dependencias y construya el proyecto.
5. **Configuración de Google Maps (API Key):**
    - Abrir el archivo `local.properties` ubicado en la raíz del proyecto.
    - Agregar la clave privada del servicio:
      ```properties
      MAPS_API_KEY=TuClaveAPI_Aqui
      ```

---

##  2. Configuración de Firebase

1. **Creación del Proyecto:** Crear un proyecto en la consola de Firebase vinculado al paquete `cl.ipvg.georegistro`.
2. **Archivo de Configuración:** Descargar el archivo `google-services.json` desde Firebase y pegarlo dentro del directorio `/app` de tu proyecto.
3. **Base de Datos:** Habilitar **Cloud Firestore** en modo de prueba y verificar la creación de la colección `"incidencias"`.

---

##  3. Guía de Pruebas

### Prueba 1: Navegación y Sensor de Proximidad
* Probar los 3 botones principales a Registro, Listado y Mapa.
* Cubrir la parte superior del teléfono (o en el emulador cambiar el estado del sensor `Proximity` a `Near`) para verificar que la pantalla del mapa se abre automáticamente mediante el gesto.

### Prueba 2: Formulario, Permisos y GPS
* Ingresar a Registro y presionar el botón **Obtener GPS**.
* Otorgar el permiso de ubicación (`ACCESS_FINE_LOCATION`) y confirmar que las coordenadas (Latitud, Longitud) se autocompleten.
* Intentar guardar el formulario con campos vacíos para comprobar el bloqueo por validación.
* Llenar Título, Descripción, Categoría y Coordenadas, y presionar **Guardar**.

### Prueba 3: Consulta en Firestore
* Ingresar a **Consultar Listado** y comprobar que la incidencia recién guardada se recupere desde la nube y se despliegue en la lista.

### Prueba 4: Google Maps
* Ingresar a **Ver el Mapa** y verificar que cargue la ubicación y muestre el marcador con la incidencia de Firestore.

---