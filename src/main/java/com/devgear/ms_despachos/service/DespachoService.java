package com.devgear.ms_despachos.service;

import com.devgear.ms_despachos.dto.DespachoResponseDTO;
import com.devgear.ms_despachos.event.OrdenCreadaEvento;
import com.devgear.ms_despachos.exception.ResourceNotFoundException;
import com.devgear.ms_despachos.model.Despacho;
import com.devgear.ms_despachos.repository.DespachoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class DespachoService {

    private static final Set<String> ESTADOS_VALIDOS = Set.of("PENDIENTE", "EN_TRANSITO", "ENTREGADO");

    private final DespachoRepository despachoRepository;

    public DespachoService(DespachoRepository despachoRepository) {
        this.despachoRepository = despachoRepository;
    }

    // Lo llama el listener de RabbitMQ al recibir el evento "orden creada"
    public void crearDesdeEvento(OrdenCreadaEvento evento) {
        Despacho despacho = new Despacho();
        despacho.setOrdenId(evento.ordenId());
        despacho.setUsuarioId(evento.usuarioId());
        despacho.setEstado("PENDIENTE");
        despacho.setFecha(LocalDateTime.now());
        despachoRepository.save(despacho);
    }

    public List<DespachoResponseDTO> obtenerTodos() {
        return despachoRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public DespachoResponseDTO obtenerPorId(Long id) {
        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despacho no encontrado: " + id));
        return mapToDTO(despacho);
    }

    public DespachoResponseDTO actualizarEstado(Long id, String nuevoEstado) {
        String estado = nuevoEstado == null ? "" : nuevoEstado.trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estado)) {
            throw new IllegalArgumentException(
                    "Estado inválido: '" + nuevoEstado + "'. Valores permitidos: " + ESTADOS_VALIDOS);
        }

        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despacho no encontrado: " + id));
        despacho.setEstado(estado);
        return mapToDTO(despachoRepository.save(despacho));
    }

    private DespachoResponseDTO mapToDTO(Despacho d) {
        return new DespachoResponseDTO(d.getId(), d.getOrdenId(), d.getUsuarioId(), d.getEstado(), d.getFecha());
    }
}