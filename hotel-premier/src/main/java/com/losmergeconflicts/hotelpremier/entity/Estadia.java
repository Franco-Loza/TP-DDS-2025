package com.losmergeconflicts.hotelpremier.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estadias")
@Data  // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor  // Genera constructor sin argumentos (requerido por JPA)
@AllArgsConstructor  // Genera constructor con todos los argumentos
@Builder  // Implementa el patrón Builder para construcción fluida de objetos
public class Estadia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHoraIngreso;

    private LocalDateTime fechaHoraEgreso;

    @Column(name = "fecha_egreso_esperado")
    private LocalDate fechaEgresoEsperado;

    @ManyToOne
    @JoinColumn(name = "huesped_id", nullable = false)
    private Huesped huespedAsignado;

    @OneToMany
    @JoinTable(
        name = "estadia_huespedes_acompaniantes",
        joinColumns = @JoinColumn(name = "estadia_id"),
        inverseJoinColumns = @JoinColumn(name = "huesped_id")
    )
    private List<Huesped> huespedesAcompaniantes;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @OneToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @ManyToMany
    @JoinTable(
        name = "estadias_items_consumo",
        joinColumns = @JoinColumn(name = "estadia_id"),
        inverseJoinColumns = @JoinColumn(name = "item_consumo_id")
    )
    private List<ItemConsumo> itemsConsumo;
}
