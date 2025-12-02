package com.losmergeconflicts.hotelpremier.dao;

import com.losmergeconflicts.hotelpremier.entity.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EstadiaDAO extends JpaRepository<Estadia, Long> {

    @Query("SELECT e FROM Estadia e WHERE " +
            "e.fechaHoraIngreso <= :fechaHasta AND " +
            "(e.fechaHoraEgreso IS NULL OR e.fechaHoraEgreso >= :fechaDesde)")
    List<Estadia> findEstadiasEnRango(@Param("fechaDesde") LocalDateTime fechaDesde,
                                      @Param("fechaHasta") LocalDateTime fechaHasta);
}