package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dto.EstadiaDTOResponse;
import com.losmergeconflicts.hotelpremier.dto.EstadiaDTORequest;

public interface GestorEstadias {
    EstadiaDTOResponse registrarOcupacion(EstadiaDTORequest request);
}