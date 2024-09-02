package org.esfe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "id_enlace", nullable = false)
    private Integer idEnlace;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = true)
    private Timestamp updatedAt;

    // other properties from wompi
    @Column(name = "url_enlace", nullable = true)
    private String urlEnlace;

    @Column(name = "url_qr_code_enlace", nullable = true)
    private String urlQrCodeEnlace;

    @Column(name = "id_cuenta", nullable = true)
    private String idCuenta;

    @Column(name = "fecha_transaccion", nullable = true)
    private String fechaTransaccion;

    @Column(name = "modulo_utilizado", nullable = true)
    private String moduloUtilizado;

    @Column(name = "forma_pago_utilizada", nullable = true)
    private String formaPagoUtilizada;

    @Column(name = "id_transaccion", nullable = true)
    private String idTransaccion;

    @Column(name = "resultado_transaccion", nullable = true)
    private String resultadoTransaccion;

    @Column(name = "codigo_autorizacion", nullable = true)
    private String codigoAutorizacion;

    @Column(name = "id_intento_pago", nullable = true)
    private String idIntentoPago;


    

    // Uso de @PrePersist para inicializar createdAt y updatedAt cuando se crea el producto
    @PrePersist
    protected void onCreate() {
        Timestamp now = Timestamp.from(Instant.now());
        this.createdAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
