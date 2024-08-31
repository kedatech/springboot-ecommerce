package org.esfe.models.dtos.wompi;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentLinkRequest {

    private String idAplicativo;
    private String identificadorEnlaceComercio;
    private double monto;
    private String nombreProducto;
    private FormaPago formaPago;
    private String cantidadMaximaCuotas;
    private InfoProducto infoProducto;
    private Configuracion configuracion;

    @Data
    public static class FormaPago {
        private boolean permitirTarjetaCreditoDebido = false;
        private boolean permitirPagoConPuntoAgricola = false;
        private boolean permitirPagoEnCuotasAgricola = false;
        private boolean permitirPagoEnBitcoin = false;
    }

    @Data
    public static class InfoProducto {
        private String descripcionProducto = "Aquí pagaras tu compra";
        private String urlImagenProducto;
    }

    @Data
    public static class Configuracion {
        private String urlRedirect;
        private boolean esMontoEditable = false;
        private boolean esCantidadEditable = false;
        private int cantidadPorDefecto = 1;
        private int duracionInterfazIntentoMinutos;
        private String urlRetorno;
        private String emailsNotificacion;
        private String urlWebhook;
        private String telefonosNotificacion;
        private boolean notificarTransaccionCliente;
    }
}