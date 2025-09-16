package likelion13th.shop.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updatedAt;
}

/*
1)
-코드의 재사용성을 높임
-데이터의 수정을 관리하고 추적할 수 있게 함

2)
-없다면 개별적으로 선언해야 하고 정확성이 떨어짐
-@CreationTimestamp, @UpdateTimestamp 어노테이션이 없으면 직접 설정해야 한다는 불편하미 있음 (복잡함과 실수를 줄여줌)
 */