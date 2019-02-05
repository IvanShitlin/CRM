package com.foxminded.hipsterfox.service.view.service;

import com.foxminded.hipsterfox.service.view.dto.DebtorViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DebtorViewService {

    Page<DebtorViewDTO> findAll(Pageable pageable);

}
