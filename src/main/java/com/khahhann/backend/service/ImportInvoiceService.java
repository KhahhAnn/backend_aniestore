package com.khahhann.backend.service;

import com.khahhann.backend.model.ImportInvoice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ImportInvoiceService {
    ImportInvoice addNewInvoice(ImportInvoice importInvoice);
    ImportInvoice updateInvoice(ImportInvoice importInvoice, UUID id);
    ResponseEntity<?> deleteInvoice(UUID id);

}
