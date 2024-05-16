package com.khahhann.backend.service;

import com.khahhann.backend.model.ImportInvoiceDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ImportInvoiceDetailService {
    public ImportInvoiceDetail addNewInvoiceDetail(ImportInvoiceDetail invoiceDetail);
    public ImportInvoiceDetail updateInvoiceDetail(ImportInvoiceDetail invoiceDetail, UUID id);
    public ResponseEntity<?> deleteInvoiceDetail(UUID id);

}
