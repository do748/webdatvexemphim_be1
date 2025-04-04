package cinema.service.Receipt;

import cinema.modal.entity.Receipt;
import cinema.modal.request.ReceiptRequest;

import java.util.List;

public interface ReceiptService {
    List<Receipt> findReceipts();
    Receipt findReceiptById(int id);
    Receipt ceateReceipt(ReceiptRequest request);
    Receipt updateReceipt(int id, ReceiptRequest request);
    Receipt changeStatus(int id, String status);
//    void processIncomeReceipts();
    List<Receipt> findIncome();
    List<Receipt> findSpending();

}
