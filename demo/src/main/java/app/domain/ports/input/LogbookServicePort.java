package app.domain.ports.input;

import app.domain.models.Logbook;
import app.domain.models.TypeWorker;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Puerto de entrada (driving port) para la gestión de la bitácora de operaciones.
 */
public interface LogbookServicePort {

    /**
     * Registra una nueva operación en la bitácora.
     *
     * @param operationType     Categoría de la operación (ej: "TRANSFER", "LOAN_APPROVAL").
     * @param userId            ID del usuario que realizó la operación.
     * @param userRole          Rol del trabajador que ejecutó la acción.
     * @param affectedProductId Identificador del producto/entidad afectada.
     * @param description       Descripción detallada de lo ocurrido.
     * @return La entrada de bitácora registrada.
     */
    Logbook registerOperation(String operationType, long userId,
                              TypeWorker userRole, String affectedProductId,
                              String description);

    /**
     * Obtiene todas las operaciones realizadas por un usuario específico.
     *
     * @param userId ID del usuario.
     * @return Lista de entradas de bitácora.
     */
    List<Logbook> getOperationsByUser(long userId);

    /**
     * Obtiene el historial de operaciones sobre un producto/entidad específica.
     *
     * @param affectedProductId ID del producto afectado.
     * @return Lista de entradas de bitácora.
     */
    List<Logbook> getOperationsByProduct(String affectedProductId);

    /**
     * Obtiene operaciones registradas dentro de un rango de fechas.
     *
     * @param from Fecha/hora de inicio.
     * @param to   Fecha/hora de fin.
     * @return Lista de entradas de bitácora en ese rango.
     */
    List<Logbook> getOperationsByDateRange(LocalDateTime from, LocalDateTime to);

    /**
     * Obtiene todas las operaciones de un tipo específico.
     *
     * @param operationType Categoría de operación a filtrar.
     * @return Lista de entradas de bitácora.
     */
    List<Logbook> getOperationsByType(String operationType);
}
