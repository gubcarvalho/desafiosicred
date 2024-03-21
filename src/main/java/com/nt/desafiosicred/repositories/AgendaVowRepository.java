package com.nt.desafiosicred.repositories;

import com.nt.desafiosicred.model.AgendaVow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgendaVowRepository extends JpaRepository<AgendaVow, UUID> {

    List<AgendaVow> findByAgendaId(UUID agendaId);

    boolean existsByAgendaIdAndCpf(UUID agendaId, String cpf);
}
