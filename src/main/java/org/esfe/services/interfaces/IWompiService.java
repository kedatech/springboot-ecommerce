package org.esfe.services.interfaces;

import org.esfe.models.dtos.wompi.PaymentLinkResponse;

public interface IWompiService {
    PaymentLinkResponse generateLink(String identificadorEnlaceComercio, Double monto, Integer paymentId);
}
