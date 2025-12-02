package com.losmergeconflicts.hotelpremier.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "facturas")
@Data  // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor  // Genera constructor sin argumentos (requerido por JPA)
@AllArgsConstructor  // Genera constructor con todos los argumentos
@Builder  // Implementa el patrón Builder para construcción fluida de objetos
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_factura", length = 10)
    private EstadoFactura estado;

    @Column(name = "numero_habitacion", length = 5)
    private String numeroHabitacion;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDateTime fechaHoraSalida;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_factura", length = 1)
    private TipoFactura tipoFactura;

    @Column(nullable = false)
    private Float valorEstadia;

    @Column(nullable = false)
    private Float montoTotal;

    @ManyToOne
    @JoinColumn(name = "id_responsable_de_pago", nullable = false)
    private ResponsableDePago responsableDePago;

    @ManyToOne
    @JoinColumn(name = "id_estadia", nullable = false)
    private Estadia estadia;
}
