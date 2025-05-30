
package com.twelveshock.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@MongoEntity(collection = "VerificacionContraentrega")
public class  VerificacionContraentrega extends PanacheMongoEntity {

    @JsonProperty("order_id")
    public Long orderId;

    @JsonProperty("ciudadEnvio")
    public String ciudadEnvio;

    @JsonProperty("id_cliente")
    public Long idCliente;

    @JsonProperty("nombre_cliente")
    public String nombreCliente;

    @JsonProperty("saldo")
    public BigDecimal saldo;

    @JsonProperty("precio_envio")
    public BigDecimal precioEnvio;

    @JsonProperty("total")
    public BigDecimal total;

    @JsonProperty("estado")
    public EstadoContraentrega estado = EstadoContraentrega.PENDIENTE;

    @JsonProperty("fecha_creacion")
    public LocalDateTime fechaCreacion = LocalDateTime.now();

    @JsonProperty("fecha_verificacion")
    public LocalDateTime fechaVerificacion;

    @JsonProperty("usuario_verificacion")
    public String usuarioVerificacion;

    @JsonProperty("observaciones")
    public String observaciones;

    public enum EstadoContraentrega {
        PENDIENTE,
        VERIFICADO,
        CANCELADO
    }
}
