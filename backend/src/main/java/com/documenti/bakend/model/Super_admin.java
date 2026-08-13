package com.documenti.bakend.model;

import com.documenti.bakend.model.base.Padre;

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
