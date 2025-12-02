package com.losmergeconflicts.hotelpremier.dao;

import com.losmergeconflicts.hotelpremier.entity.Habitacion;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitacionDAO extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findAllByOrderByNombreAsc();

    List<Habitacion> findByTipoHabitacionOrderByNombreAsc(TipoHabitacion tipoHabitacion);
}