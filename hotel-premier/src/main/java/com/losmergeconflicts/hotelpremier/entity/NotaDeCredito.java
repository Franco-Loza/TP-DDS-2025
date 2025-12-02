package com.losmergeconflicts.hotelpremier.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notas_de_credito")
@Data  // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor  // Genera constructor sin argumentos (requerido por JPA)
@AllArgsConstructor  // Genera constructor con todos los argumentos
@Builder  // Implementa el patrón Builder para construcción fluida de objetos
public class NotaDeCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private Float montoTotal;
    
    @OneToMany
    @JoinTable(
        name = "notas_de_credito_facturas_canceladas",
        joinColumns = @JoinColumn(name = "nota_de_credito_id"),
        inverseJoinColumns = @JoinColumn(name = "factura_id")
    )
    private List<Factura> facturasCanceladas;
}
