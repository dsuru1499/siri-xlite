package siri_xlite.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineDiscoveryCache {

    @Autowired
    LinesDiscoveryRepository repository;

    @Cacheable("lines")
    public List<LinesDiscoveryDocument> findAll() {
        return repository.findAll().toList().blockingGet();
    }

}
