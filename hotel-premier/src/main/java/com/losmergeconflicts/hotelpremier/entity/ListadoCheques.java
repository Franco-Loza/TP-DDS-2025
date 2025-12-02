package com.losmergeconflicts.hotelpremier.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor  // Genera constructor sin argumentos (requerido por JPA)
@AllArgsConstructor  // Genera constructor con todos los argumentos
@EqualsAndHashCode(callSuper = true) 
@ToString(callSuper = true) 
public class ListadoCheques extends Listado {
    private List<Pago> listadoCheques;
}
