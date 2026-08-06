package com.documenti.bakend.domain.model;

import com.documenti.bakend.domain.model.base.Padre;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Admin
 */
@Entity
@Getter
@Setter
@Table(name = "admin")
public class Admin extends Padre{


    
}
