package com.khahhann.backend.repository;

import com.khahhann.backend.model.ImportInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;
@RepositoryRestResource(path = "import-invoice")
@CrossOrigin("*")
public interface ImportInvoiceRepository extends JpaRepository<ImportInvoice, UUID> {
}
