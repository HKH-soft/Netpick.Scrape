package ir.netpick.mailmine.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import ir.netpick.mailmine.common.BaseEntity;
import ir.netpick.mailmine.common.enums.RoleEnum;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    public Role() {
    }

    public Role(RoleEnum name) {
        this.name = name;
    }

    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

}
