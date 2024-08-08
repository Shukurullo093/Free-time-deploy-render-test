package com.time.demo.entity.templates;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class AbsUserEntity extends AbsMainEntity{
    @JoinColumn(updatable = false)
    // @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy
    private Long createdBy;

    //  @ManyToOne(fetch = FetchType.LAZY)
    @LastModifiedBy
    private Long updatedBy;
}
