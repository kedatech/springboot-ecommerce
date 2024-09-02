package org.esfe.services.interfaces;

import org.esfe.models.dtos.wompi.PaymentLinkResponse;

public interface IWompiService {
    PaymentLinkResponse generateLink(Double monto, Integer paymentId);
}
