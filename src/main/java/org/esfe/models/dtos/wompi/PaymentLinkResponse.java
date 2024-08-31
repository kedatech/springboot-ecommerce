package org.esfe.models.dtos.wompi;

import lombok.*;

@Data
public class PaymentLinkResponse {
    private int idEnlace;
    private String urlQrCodeEnlace;
    private String urlEnlace;
    private boolean estaProductivo;
}
