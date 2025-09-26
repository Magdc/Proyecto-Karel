# Sistema de Semáforos para Karel - Documentación (Versión Mejorada)

## Descripción del Sistema

Se ha implementado un sistema de semáforos **con verificación continua** en la clase `TrafficController` para controlar el acceso a secciones críticas del mapa donde las rutas rápidas comparten el mismo camino.

## ✨ **Características Principales**

### 🔄 **Verificación Continua**
Los robots que están en posiciones de semáforo verifican **continuamente** si pueden adquirir el semáforo, evitando bloqueos permanentes.

### ⚡ **Sin Bloqueo de Hilos**
El sistema no usa `wait()` para evitar que los hilos se bloqueen indefinidamente. En su lugar, los robots reintentan la adquisición en cada iteración.

### 🛡️ **Prevención de Deadlocks**
Antes de adquirir un semáforo, se verifica que la posición de destino (liberación) esté libre.

## Secciones Críticas Implementadas

### Sección 1: Posiciones (16,1) hasta (21,1)
- **Entrada Semáforo 1**: Posición (15,1)
- **Entrada Semáforo 2**: Posición (21,2)  
- **Liberación Semáforo 1**: Posición (22,1)
- **Liberación Semáforo 2**: Posición (16,2)

### Sección 2: Posiciones (26,1) hasta (29,1)
- **Entrada Semáforo 1**: Posición (25,1)
- **Entrada Semáforo 2**: Posición (29,2)
- **Liberación Semáforo 1**: Posición (30,1)
- **Liberación Semáforo 2**: Posición (26,2)

### Sección 3: Posiciones (30,5) hasta (30,10)
- **Entrada Semáforo 1**: Posición (30,4)
- **Entrada Semáforo 2**: Posición (29,10)
- **Liberación Semáforo 1**: Posición (30,11)
- **Liberación Semáforo 2**: Posición (29,5)

## Funcionamiento del Sistema Mejorado

### 1. Verificación Continua en Posiciones de Semáforo
```java
if (esPosicionSemaforo) {
    // Intentar continuamente adquirir el semáforo
    boolean pudoMoverse = safeMoveConSemaforo();
    if (!pudoMoverse) {
        // No pudo adquirir el semáforo, retroceder en el índice para reintentar
        i = i - 1;
        // Pequeña pausa para no saturar el procesador
        Thread.sleep(50);
        continue; // Volver a intentar
    }
}
```

### 2. Adquisición Inmediata (Sin Wait)
```java
private boolean intentarAdquirirSemaforo1(int street, int avenue) {
    synchronized (semaforo1) {
        // Verificar que el semáforo no esté ocupado
        if (semaforoOcupado1) {
            return false; // Retorna inmediatamente
        }
        
        // Verificar destino libre
        if (!verificarDestinoLibre(street, avenue)) {
            return false;
        }
        
        // Adquirir semáforo
        semaforoOcupado1 = true;
        return true;
    }
}
```

### 3. Verificación de Disponibilidad
```java
public boolean semaforoDisponible(int street, int avenue)
```
- Permite consultar si un semáforo está disponible sin intentar adquirirlo
- Útil para optimización y debugging

## Mejoras Implementadas

### ✅ **Verificación Continua**
- Los robots en posiciones de semáforo verifican constantemente la disponibilidad
- No se quedan bloqueados esperando indefinidamente

### ✅ **Pausa Anti-Saturación**
- `Thread.sleep(50)` entre reintentos para no saturar el procesador
- Permite que otros hilos tengan oportunidad de ejecutarse

### ✅ **Retorno Inmediato**
- Los métodos de adquisición retornan inmediatamente si no pueden adquirir el semáforo
- No hay esperas bloqueantes

### ✅ **Liberación Automática y Notificación**
- Los semáforos se liberan automáticamente al alcanzar posiciones designadas  
- `notifyAll()` despierta a threads que puedan estar esperando

## Flujo de Ejecución

1. **Robot llega a posición de semáforo** → Detecta que necesita semáforo
2. **Intenta adquirir semáforo** → Verifica si está libre y destino disponible
3. **Si no puede adquirir** → Retrocede en el índice, pausa, reintenta
4. **Si puede adquirir** → Adquiere semáforo, se mueve, actualiza coordenadas
5. **Al llegar a posición de liberación** → Libera semáforo automáticamente

## Archivos de Prueba

### `PruebaSemaforosContinuo.java`
Demuestra el comportamiento de verificación continua con múltiples robots compitiendo por los mismos semáforos.

```bash
javac -cp "KarelJRobot.jar" PruebaSemaforosContinuo.java
java -cp "KarelJRobot.jar;." PruebaSemaforosContinuo
```

## Ventajas del Sistema Mejorado

- 🔄 **Sin bloqueos permanentes**: Los robots siempre tienen oportunidad de avanzar
- ⚡ **Respuesta rápida**: Verificación inmediata sin esperas largas  
- 🛡️ **Robusto**: Previene deadlocks y situaciones de bloqueo mutuo
- 📊 **Observable**: El comportamiento es visible y debuggeable
- 🎯 **Eficiente**: Pausa mínima entre reintentos para optimizar rendimiento