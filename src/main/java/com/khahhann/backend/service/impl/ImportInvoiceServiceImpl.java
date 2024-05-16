package com.khahhann.backend.service.impl;

import com.khahhann.backend.model.ImportInvoice;
import com.khahhann.backend.repository.ImportInvoiceRepository;
import com.khahhann.backend.service.ImportInvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImportInvoiceServiceImpl implements ImportInvoiceService {
    private ImportInvoiceRepository importInvoiceRepository;
    @Override
    public ImportInvoice addNewInvoice(ImportInvoice importInvoice) {
        ImportInvoice newInvoice = new ImportInvoice();
        newInvoice.setImportDate(importInvoice.getImportDate() != null ? importInvoice.getImportDate() :new Date(System.currentTimeMillis()));
        newInvoice.setInvoiceName(importInvoice.getInvoiceName());
        newInvoice.setTotalPrice(importInvoice.getTotalPrice());
        newInvoice.setImportInvoiceDetails(importInvoice.getImportInvoiceDetails());
        newInvoice.setCreateAt(new java.util.Date(System.currentTimeMillis()));
        newInvoice.setUpdateAt(new java.util.Date(System.currentTimeMillis()));
        this.importInvoiceRepository.saveAndFlush(newInvoice);
        return newInvoice;
    }

    @Override
    public ImportInvoice updateInvoice(ImportInvoice importInvoice, UUID id) {
        ImportInvoice updateInvoice = this.importInvoiceRepository.getReferenceById(id);
        if(updateInvoice == null) {
            return null;
        }
        updateInvoice.setImportDate(importInvoice.getImportDate() != null ? importInvoice.getImportDate() : updateInvoice.getImportDate());
        updateInvoice.setInvoiceName(importInvoice.getInvoiceName() != null ? importInvoice.getInvoiceName() : updateInvoice.getInvoiceName());
        updateInvoice.setTotalPrice(importInvoice.getTotalPrice() <= 0  ? importInvoice.getTotalPrice() : updateInvoice.getTotalPrice());
        updateInvoice.setImportInvoiceDetails(importInvoice.getImportInvoiceDetails() != null ? importInvoice.getImportInvoiceDetails() : updateInvoice.getImportInvoiceDetails());
        updateInvoice.setUpdateAt(new java.util.Date(System.currentTimeMillis()));
        this.importInvoiceRepository.saveAndFlush(updateInvoice);
        return updateInvoice;
    }

    @Override
    public ResponseEntity<?> deleteInvoice(UUID id) {
        if(!this.importInvoiceRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Not exists!");
        }
        this.importInvoiceRepository.deleteById(id);
        return ResponseEntity.ok().body("Delete complete!");
    }
}
