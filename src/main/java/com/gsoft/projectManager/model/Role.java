package com.gsoft.projectManager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name="Roles")
public class Role {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name="name")
    private Rolename name;

    public Role(Rolename name){
        this.name = name;
    }

    public Rolename getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return name.name();
    }
}
