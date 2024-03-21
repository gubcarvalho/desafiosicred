package com.nt.desafiosicred.repositories;

import com.nt.desafiosicred.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, UUID> {

}
