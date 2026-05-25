package app.domain.ports.output;

import app.domain.models.Logbook;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Puerto de salida (driven port) para la persistencia de la bitácora.
 */
public interface LogbookRepositoryPort {

    /**
     * Guarda una nueva entrada en la bitácora.
     *
     * @param logbook Objeto Logbook a persistir.
     * @return La entrada guardada con su ID asignado.
     */
    Logbook save(Logbook logbook);

    /**
     * Lista todas las entradas de bitácora de un usuario.
     *
     * @param userId ID del usuario.
     * @return Lista de entradas.
     */
    List<Logbook> findByUserId(long userId);

    /**
     * Lista todas las entradas de bitácora sobre un producto/entidad.
     *
     * @param affectedProductId ID del producto afectado.
     * @return Lista de entradas.
     */
    List<Logbook> findByAffectedProductId(String affectedProductId);

    /**
     * Lista entradas de bitácora dentro de un rango de fechas.
     *
     * @param from Fecha/hora de inicio.
     * @param to   Fecha/hora de fin.
     * @return Lista de entradas en ese rango.
     */
    List<Logbook> findByDateRange(LocalDateTime from, LocalDateTime to);

    /**
     * Lista entradas de bitácora por tipo de operación.
     *
     * @param operationType Categoría de operación.
     * @return Lista de entradas.
     */
    List<Logbook> findByOperationType(String operationType);
}
