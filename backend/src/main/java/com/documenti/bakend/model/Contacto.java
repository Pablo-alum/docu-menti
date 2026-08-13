package com.documenti.bakend.model;

import com.documenti.bakend.model.base.Padre;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contacto
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "contacto")
public class Contacto extends Padre{
    private String logo;

}
