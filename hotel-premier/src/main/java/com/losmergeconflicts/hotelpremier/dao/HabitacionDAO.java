package com.losmergeconflicts.hotelpremier.dao;

import com.losmergeconflicts.hotelpremier.entity.Habitacion;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;
import com.losmergeconflicts.hotelpremier.entity.TipoEstadoHabitacion; // <--- AGREGAR IMPORT
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitacionDAO extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findAllByOrderByNombreAsc();

    List<Habitacion> findByTipoHabitacionOrderByNombreAsc(TipoHabitacion tipo);

    List<Habitacion> findByTipoHabitacion(TipoHabitacion tipo);

    @Modifying
    @Query("UPDATE Habitacion h SET h.estadoHabitacion = :nuevoEstado WHERE h.id = :id")
    void actualizarEstado(@Param("id") Long id, @Param("nuevoEstado") TipoEstadoHabitacion nuevoEstado);
}