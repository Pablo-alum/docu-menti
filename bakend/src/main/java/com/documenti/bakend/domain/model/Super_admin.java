package com.documenti.bakend.domain.model;

import com.documenti.bakend.domain.model.base.Padre;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Super_admin
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name ="SuperAdmin")
public class Super_admin extends Padre{
    

    
}
