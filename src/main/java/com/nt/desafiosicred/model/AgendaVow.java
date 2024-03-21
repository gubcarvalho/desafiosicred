package com.nt.desafiosicred.model;

import com.nt.desafiosicred.enums.AgendaVowAnswer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true, fluent = true)
@Entity
public class AgendaVow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="agenda_id")
    private UUID agendaId;

    private String cpf;

    private AgendaVowAnswer vow;
}
