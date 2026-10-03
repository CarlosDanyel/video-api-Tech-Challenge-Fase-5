package com.fiapx.api.application.port;
import com.fiapx.api.domain.OutboxEvent;
import java.util.List;
import java.util.UUID;
public interface OutboxStore {
    void save(OutboxEvent event);
    List<OutboxEvent> findPending();
    void markPublished(UUID id);
}
