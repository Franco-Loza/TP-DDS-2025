package com.losmergeconflicts.hotelpremier.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habitaciones")
@Data  // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor  // Genera constructor sin argumentos (requerido por JPA)
@AllArgsConstructor  // Genera constructor con todos los argumentos
@Builder  // Implementa el patrón Builder para construcción fluida de objetos
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 5)
    private String nombre;

    @Column(nullable = false)
    private Float precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_habitacion", length = 20)
    private TipoHabitacion tipoHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_habitacion", length = 20)
    private TipoEstadoHabitacion estadoHabitacion;
}
