package com.example.recordz.service;

import net.codecrete.qrbill.generator.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class QrBillService {

    public byte[] generateSvg(String iban, String nomVendeur, String rue, String noRue,
                              String npa, String ville, BigDecimal montantChf, String message , String idArticle) {
        Bill bill = new Bill();
        bill.setAccount(iban.replace(" ", ""));
        bill.setAmount(montantChf);
        bill.setCurrency("CHF");
        bill.createAndSetCreditorReference("AG" + idArticle);
        Address creditor = new Address();
        creditor.setName(nomVendeur);
        creditor.setStreet(rue);
        creditor.setHouseNo(noRue);
        creditor.setPostalCode(npa);
        creditor.setTown(ville);
        creditor.setCountryCode("CH");
        bill.setCreditor(creditor);

        bill.setUnstructuredMessage(message);

        BillFormat format = bill.getFormat();
        format.setGraphicsFormat(GraphicsFormat.SVG);
        format.setOutputSize(OutputSize.QR_CODE_ONLY);

        return QRBill.generate(bill);
    }
}