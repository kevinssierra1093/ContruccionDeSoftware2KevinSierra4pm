package app.domain.services;

import app.domain.models.Logbook;
import app.domain.models.TypeWorker;
import app.domain.ports.input.LogbookServicePort;
import app.domain.ports.output.LogbookRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación para la bitácora de operaciones.
 * Implementa LogbookServicePort. Es usado por todos los demás servicios
 * para registrar cada acción relevante del sistema.
 */
public class LogbookService implements LogbookServicePort {

    private final LogbookRepositoryPort logbookRepository;

    public LogbookService(LogbookRepositoryPort logbookRepository) {
        this.logbookRepository = logbookRepository;
    }

    @Override
    public Logbook registerOperation(String operationType, long userId,
                                     TypeWorker userRole, String affectedProductId,
                                     String description) {
        Logbook entry = new Logbook();
        entry.setOperationType(operationType);
        entry.setTimestamp(LocalDateTime.now());
        entry.setUserId(userId);
        entry.setUserRol(userRole);
        entry.setAffected_Product_ID(affectedProductId);
        entry.setDescription(description);

        return logbookRepository.save(entry);
    }

    @Override
    public List<Logbook> getOperationsByUser(long userId) {
        return logbookRepository.findByUserId(userId);
    }

    @Override
    public List<Logbook> getOperationsByProduct(String affectedProductId) {
        return logbookRepository.findByAffectedProductId(affectedProductId);
    }

    @Override
    public List<Logbook> getOperationsByDateRange(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        return logbookRepository.findByDateRange(from, to);
    }

    @Override
    public List<Logbook> getOperationsByType(String operationType) {
        return logbookRepository.findByOperationType(operationType);
    }
}
