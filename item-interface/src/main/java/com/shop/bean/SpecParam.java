package com.shop.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_spec_param")
@Data
public class SpecParam implements Serializable {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
    
    // getter和setter ...
}