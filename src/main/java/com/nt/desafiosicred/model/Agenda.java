package com.nt.desafiosicred.model;

import com.nt.desafiosicred.enums.AgendaSessionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true, fluent = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(name = "agenda")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    private AgendaSessionStatus sessionStatus;
}
