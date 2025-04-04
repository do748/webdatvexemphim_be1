package cinema.repository;

import cinema.modal.entity.Ticket;
import cinema.modal.entity.constant.StatusTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Transactional(readOnly = true)
    Ticket findByIdAndStatus(int id, StatusTicket status);
}
