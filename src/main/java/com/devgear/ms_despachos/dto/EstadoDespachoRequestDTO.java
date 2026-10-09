package com.devgear.ms_despachos.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadoDespachoRequestDTO(
    @NotBlank String estado
) {}