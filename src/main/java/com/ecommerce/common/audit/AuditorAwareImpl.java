package com.ecommerce.common.audit;

import com.ecommerce.common.utils.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Gọi hàm tiện ích chúng ta vừa viết
        return Optional.of(SecurityUtils.getCurrentUser());
    }
}
