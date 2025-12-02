package com.losmergeconflicts.hotelpremier.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "huespedes")
@Getter
@Setter
@NoArgsConstructor  // Genera constructor sin argumentos (requerido por JPA)
@AllArgsConstructor  // Genera constructor con todos los argumentos
@EqualsAndHashCode(callSuper = true)  // Incluye los campos de Persona en equals/hashCode
@ToString(callSuper = true)  // Incluye los campos de Persona en toString
public class Huesped extends Persona {

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false, length = 8)
    private String documento;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String ocupacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "posicion_frente_al_iva", length = 30)
    private PosicionFrenteAlIVA posicionFrenteAlIVA;

    @ManyToOne
    @JoinColumn(name = "nacionalidad_id", nullable = false)
    private Nacionalidad nacionalidad;

}
