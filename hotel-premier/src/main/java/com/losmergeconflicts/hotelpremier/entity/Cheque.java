package com.losmergeconflicts.hotelpremier.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cheques")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class Cheque extends MedioDePago {

    @Column(nullable = false, length = 50)
    private String numero; // Representa 'nro_cheque' del diagrama

    @Column(nullable = false, length = 100)
    private String plaza;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCheque tipoCheque;

    @ManyToOne
    @JoinColumn(name = "banco_id", nullable = false)
    private Banco banco;
}
