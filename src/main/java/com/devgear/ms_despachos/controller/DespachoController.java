package com.devgear.ms_despachos.controller;

import com.devgear.ms_despachos.dto.DespachoResponseDTO;
import com.devgear.ms_despachos.dto.EstadoDespachoRequestDTO;
import com.devgear.ms_despachos.service.DespachoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despachos")
@PreAuthorize("hasAnyRole('ADMIN','DESPACHADOR')")
public class DespachoController {

    private final DespachoService despachoService;

    public DespachoController(DespachoService despachoService) {
        this.despachoService = despachoService;
    }

    @GetMapping
    public ResponseEntity<List<DespachoResponseDTO>> listar() {
        return ResponseEntity.ok(despachoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespachoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(despachoService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<DespachoResponseDTO> actualizarEstado(
            @PathVariable Long id, @Valid @RequestBody EstadoDespachoRequestDTO request) {
        return ResponseEntity.ok(despachoService.actualizarEstado(id, request.estado()));
    }
}