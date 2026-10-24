package com.mims.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mims.model.Domain;
import com.mims.repository.MimsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class MimsDataServiceTest {

    @Mock
    private MimsRepository repository;

    private MimsDataService service;

    @BeforeEach
    void setUp() {
        service = new MimsDataService(repository, new BCryptPasswordEncoder());
    }

    @Test
    void createsDomainWithAnOracleSequenceId() {
        Domain submitted = new Domain(null, "Biotechnology", "Research", null, null);
        Domain expected = new Domain(7L, "Biotechnology", "Research", null, null);
        when(repository.nextId("MIMS_DOMAINS_SEQ")).thenReturn(7L);
        when(repository.insertDomain(expected)).thenReturn(expected);

        assertEquals(expected, service.createDomain(submitted));
        verify(repository).insertDomain(expected);
    }

    @Test
    void reportsMissingDomainDuringUpdate() {
        when(repository.findDomain(44L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.updateDomain(44L, new Domain(null, "Name", null, null, null)));
    }
}
