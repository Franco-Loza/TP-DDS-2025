package com.losmergeconflicts.hotelpremier.dto;

import java.time.LocalDate;
import java.util.List;

public record ReservaDTOResponse(
    Long id,
    LocalDate fechaIngreso,
    LocalDate fechaEgreso,
    String nombreHuesped,
    String apellidoHuesped,
    String telefonoHuesped,
    List<Long> idsHabitaciones
) {

}
