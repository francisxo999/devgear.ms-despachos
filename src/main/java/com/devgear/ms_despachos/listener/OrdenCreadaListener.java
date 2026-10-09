package com.devgear.ms_despachos.listener;

import com.devgear.ms_despachos.config.RabbitMQConfig;
import com.devgear.ms_despachos.event.OrdenCreadaEvento;
import com.devgear.ms_despachos.service.DespachoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrdenCreadaListener {

    private final DespachoService despachoService;

    public OrdenCreadaListener(DespachoService despachoService) {
        this.despachoService = despachoService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DESPACHOS)
    public void onOrdenCreada(OrdenCreadaEvento evento) {
        try {
            despachoService.crearDesdeEvento(evento);
        } catch (Exception ex) {
            System.err.println("No se pudo crear el despacho para la orden " + evento.ordenId() + ": " + ex.getMessage());
        }
    }
}