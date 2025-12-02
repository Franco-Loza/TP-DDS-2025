package com.losmergeconflicts.hotelpremier.dao;

import com.losmergeconflicts.hotelpremier.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaDAO extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE " +
            "r.fechaIngreso <= :fechaHasta AND r.fechaEgreso >= :fechaDesde")
    List<Reserva> findReservasEnRango(@Param("fechaDesde") LocalDate fechaDesde,
                                      @Param("fechaHasta") LocalDate fechaHasta);

    @Query("SELECT r FROM Reserva r JOIN r.habitaciones h WHERE h.nombre = :nombreHabitacion " +
            "AND :fecha >= r.fechaIngreso AND :fecha <= r.fechaEgreso")
    Optional<Reserva> findReservaPorHabitacionYFecha(@Param("nombreHabitacion") String nombreHabitacion,
                                                     @Param("fecha") LocalDate fecha);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reserva r JOIN r.habitaciones h " +
            "WHERE h.id = :idHabitacion " +
            "AND r.fechaIngreso < :fechaEgreso " +
            "AND r.fechaEgreso > :fechaIngreso")
    boolean existeSolapamiento(@Param("idHabitacion") Long idHabitacion,
                               @Param("fechaIngreso") LocalDate fechaIngreso,
                               @Param("fechaEgreso") LocalDate fechaEgreso);

    @Query("SELECT r FROM Reserva r JOIN r.habitaciones h " +
            "WHERE h.id = :idHabitacion " +
            "AND r.fechaIngreso < :fechaEgreso " +
            "AND r.fechaEgreso > :fechaIngreso")
    Optional<Reserva> findReservaConflictiva(@Param("idHabitacion") Long idHabitacion,
                                             @Param("fechaIngreso") LocalDate fechaIngreso,
                                             @Param("fechaEgreso") LocalDate fechaEgreso);
}