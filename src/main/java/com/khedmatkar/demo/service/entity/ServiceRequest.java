package com.khedmatkar.demo.service.entity;

import com.khedmatkar.demo.AbstractEntity;
import com.khedmatkar.demo.account.entity.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "service_requests")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ServiceRequest extends AbstractEntity {

    // todo: add ServiceDetails Object (See class diagram)

    @ManyToOne
    private Specialty specialty;
    private String description;
    private String address;

    private Date receptionDate;

    @OneToOne
    private Customer customer;

    @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.REMOVE)
    private List<ServiceRequestSpecialist> specialistHistory;

    @Enumerated(EnumType.STRING)
    private ServiceRequestStatus status;
}
