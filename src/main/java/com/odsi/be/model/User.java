package com.odsi.be.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "_user")
public class User {
    @Id
    Long id;
    String name;
}
