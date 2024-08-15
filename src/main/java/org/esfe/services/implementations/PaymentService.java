package org.esfe.services.implementations;

import org.esfe.models.Payment;
import org.esfe.repository.IPaymentRepository;
import org.esfe.repository.IUserRepository;
import org.esfe.services.interfaces.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;

    @Override
    public Page<Payment> buscarTodosPaginados(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    @Override
    public List<Payment> obtenerTodos() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> buscarPorId(Integer id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Payment createOEditar(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void eliminarPorId(Integer id) {
        paymentRepository.deleteById(id);
    }
}
