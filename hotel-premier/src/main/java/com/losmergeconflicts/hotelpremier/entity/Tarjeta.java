package com.losmergeconflicts.hotelpremier.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tarjetas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class Tarjeta extends MedioDePago {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RedDePago redDePago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTarjeta tipo;

    @ManyToOne
    @JoinColumn(name = "banco_id", nullable = false)
    private Banco bancoEmisor;

}
