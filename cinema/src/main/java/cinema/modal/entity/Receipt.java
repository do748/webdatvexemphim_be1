package cinema.modal.entity;

import cinema.modal.entity.constant.StatusReceipt;
import cinema.modal.entity.constant.TypeReceipt;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "receipt")
public class Receipt extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeReceipt type;

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusReceipt status;
}
