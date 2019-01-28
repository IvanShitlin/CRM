package ru.shitlin.service.view.service;

import ru.shitlin.service.view.dto.DebtorViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DebtorViewService {

    Page<DebtorViewDTO> findAll(Pageable pageable);

}
