//package com.webstore.webstore.model.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.NaturalId;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "roles")
//public class Role {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id", unique = true, nullable = false, updatable = false)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    @NaturalId
//    @Column(name = "roleName", length = 60)
//    private RoleName name;
//}
