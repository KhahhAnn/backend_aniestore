package com.khahhann.backend.service.impl;

import com.khahhann.backend.model.ImportInvoiceDetail;
import com.khahhann.backend.repository.ImportInvoiceDetailRepository;
import com.khahhann.backend.service.ImportInvoiceDetailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImportInvoiceDetailServiceImpl implements ImportInvoiceDetailService {
    private ImportInvoiceDetailRepository importInvoiceDetailRepository;

    @Override
    public ImportInvoiceDetail addNewInvoiceDetail(ImportInvoiceDetail invoiceDetail) {
        ImportInvoiceDetail newInvoiceDetail = new ImportInvoiceDetail();
        newInvoiceDetail.setImportInvoice(invoiceDetail.getImportInvoice());
        newInvoiceDetail.setTotal(invoiceDetail.getTotal());
        newInvoiceDetail.setProduct(invoiceDetail.getProduct());
        newInvoiceDetail.setQuantityImport(invoiceDetail.getQuantityImport());
        newInvoiceDetail.setCreateAt(new Date(System.currentTimeMillis()));
        newInvoiceDetail.setUpdateAt(new Date(System.currentTimeMillis()));
        this.importInvoiceDetailRepository.saveAndFlush(newInvoiceDetail);
        return newInvoiceDetail;
    }

    @Override
    public ImportInvoiceDetail updateInvoiceDetail(ImportInvoiceDetail invoiceDetail, UUID id) {
        ImportInvoiceDetail updateInvoice = this.importInvoiceDetailRepository.getReferenceById(id);
        if(updateInvoice == null) {
            return null;
        }
        updateInvoice.setImportInvoice(invoiceDetail.getImportInvoice() != null ? invoiceDetail.getImportInvoice() : updateInvoice.getImportInvoice());
        updateInvoice.setTotal(invoiceDetail.getTotal() <= 0 ? invoiceDetail.getTotal() : updateInvoice.getTotal());
        updateInvoice.setProduct(invoiceDetail.getProduct() != null ? invoiceDetail.getProduct() : updateInvoice.getProduct());
        updateInvoice.setQuantityImport(invoiceDetail.getImportInvoice() != null? invoiceDetail.getQuantityImport() : updateInvoice.getQuantityImport());
        updateInvoice.setUpdateAt(new Date(System.currentTimeMillis()));
        this.importInvoiceDetailRepository.saveAndFlush(updateInvoice);
        return updateInvoice;
    }

    @Override
    public ResponseEntity<?> deleteInvoiceDetail(UUID id) {
        if(!this.importInvoiceDetailRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Not exists!");
        }
        this.importInvoiceDetailRepository.deleteById(id);
        return ResponseEntity.ok().body("Delete complete!");
    }
}
