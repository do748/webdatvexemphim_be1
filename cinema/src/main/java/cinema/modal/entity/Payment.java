package cinema.modal.entity;

import cinema.modal.entity.constant.TypePayment;
import cinema.modal.entity.constant.StatusPayment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "Payment")
public class Payment extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypePayment type;

    @Column(name = "address_tranfer", nullable = false)
    private String address;

    @OneToMany(mappedBy = "payment")
    private List<Booking> bookings;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "provider")
    private String provider;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusPayment status;
}
