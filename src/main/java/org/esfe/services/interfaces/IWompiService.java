package org.esfe.services.interfaces;

import org.esfe.models.dtos.wompi.PaymentLinkResponse;

public interface IWompiService {
    PaymentLinkResponse generateLink(Integer idEnlace, String urlEnlace);
}

