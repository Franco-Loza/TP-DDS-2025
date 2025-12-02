package com.losmergeconflicts.hotelpremier.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "responsables_de_pago")
@Getter
@Setter
@NoArgsConstructor  // Genera constructor sin argumentos (requerido por JPA)
@AllArgsConstructor  // Genera constructor con todos los argumentos
@Builder  // Implementa el patrón Builder para construcción fluida de objetos
@EqualsAndHashCode(callSuper = true)  // Incluye los campos de Persona en equals/hashCode
@ToString(callSuper = true)  // Incluye los campos de Persona en toString
public class ResponsableDePago extends Persona {

    @Column(nullable = false, length = 200)
    private String razonSocial;

}
