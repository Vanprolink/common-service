package com.ecommerce.common.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // QUAN TRỌNG: Đánh dấu đây là class cha, các trường này sẽ được map vào bảng con
@EntityListeners(AuditingEntityListener.class) // QUAN TRỌNG: Lắng nghe sự kiện save/update để tự điền dữ liệu
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    // updatable = false: Ngày tạo chỉ set 1 lần, không bao giờ bị sửa đổi
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}
