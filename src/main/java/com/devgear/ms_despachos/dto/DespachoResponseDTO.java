package com.devgear.ms_despachos.dto;

import java.time.LocalDateTime;

public record DespachoResponseDTO(
    Long id,
    Long ordenId,
    String usuarioId,
    String estado,
    LocalDateTime fecha
) {}