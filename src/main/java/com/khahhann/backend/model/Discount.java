    package com.khahhann.backend.model;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Min;
    import jakarta.validation.constraints.NotNull;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.sql.Date;
    import java.util.List;
    import java.util.UUID;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "discount")
    public class Discount {
        @Id
        @Column(name = "id")
        @GeneratedValue
        private UUID id;

        @Column(name = "discount_name", nullable = false)
        @NotNull(message = "Vui lòng nhập tên mã giảm giá!")
        private String discountName;

        @Column(name = "percent_discount", nullable = false)
        @Min(value = 1, message = "Phần trăm giảm giá phải lớn hơn 0!")
        private double percentDiscount;

        @Column(name = "apply_date", nullable = false)
        private Date applyDate;

        @Column(name = "expiry", nullable = false)
        private Date expiry;

        @Column(name = "create_at")
        private java.util.Date createAt;

        @Column(name = "update_at")
        private java.util.Date updateAt;

        @ManyToMany(cascade = {
                CascadeType.DETACH,
                CascadeType.MERGE,
                CascadeType.PERSIST,
                CascadeType.REFRESH
        }, fetch = FetchType.LAZY)
        @JoinTable(
                name = "user_discount",
                joinColumns = @JoinColumn(name = "discount_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        private List<Users> usersList;
    }
