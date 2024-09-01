package org.esfe.models.dtos.wompi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WompiWebhook {

    @JsonProperty("IdCuenta")
    private String idCuenta;

    @JsonProperty("FechaTransaccion")
    private String fechaTransaccion;

    @JsonProperty("Monto")
    private int monto;

    @JsonProperty("ModuloUtilizado")
    private String moduloUtilizado;

    @JsonProperty("FormaPagoUtilizada")
    private String formaPagoUtilizada;

    @JsonProperty("IdTransaccion")
    private String idTransaccion;

    @JsonProperty("ResultadoTransaccion")
    private String resultadoTransaccion;

    @JsonProperty("CodigoAutorizacion")
    private String codigoAutorizacion;

    @JsonProperty("IdIntentoPago")
    private String idIntentoPago;

    @JsonProperty("Cantidad")
    private int cantidad;

    @JsonProperty("EsProductiva")
    private boolean esProductiva;

    @JsonProperty("Aplicativo")
    private Aplicativo aplicativo;

    @JsonProperty("EnlacePago")
    private EnlacePago enlacePago;

    @JsonProperty("cliente")
    private Cliente cliente;

    @Data
    public static class Aplicativo {

        @JsonProperty("Nombre")
        private String nombre;

        @JsonProperty("Url")
        private String url;

        @JsonProperty("Id")
        private String id;
    }

    @Data
    public static class EnlacePago {

        @JsonProperty("Id")
        private int id;

        @JsonProperty("IdentificadorEnlaceComercio")
        private String identificadorEnlaceComercio;

        @JsonProperty("NombreProducto")
        private String nombreProducto;
    }

    @Data
    public static class Cliente {

        @JsonProperty("Nombre")
        private String nombre;

        @JsonProperty("Email")
        private String email;

        @JsonProperty("additionalProp1")
        private String additionalProp1;

        @JsonProperty("additionalProp2")
        private String additionalProp2;
    }
}
