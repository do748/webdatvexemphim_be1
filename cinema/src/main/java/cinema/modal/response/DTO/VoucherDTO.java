package cinema.modal.response.DTO;

import cinema.modal.entity.Voucher;
import lombok.Data;

@Data
public class VoucherDTO {
    private String voucher_id;
    private String voucher_name;
    private String voucher_description;
    private String voucher_discount;
    private String voucher_expiry;
    private String voucher_quantity;
    private String voucher_status;

    public VoucherDTO(Voucher voucher) {
        this.voucher_id = String.valueOf(voucher.getId());
        this.voucher_name = voucher.getName();
        this.voucher_description = voucher.getDescription();
        this.voucher_discount = String.valueOf(voucher.getDiscount());
        this.voucher_expiry = String.valueOf(voucher.getExpiry());
        this.voucher_quantity = String.valueOf(voucher.getQuantity());
        this.voucher_status = String.valueOf(voucher.getStatus());
    }
}
