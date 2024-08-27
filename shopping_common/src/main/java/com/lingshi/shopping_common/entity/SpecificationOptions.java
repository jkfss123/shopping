package com.lingshi.shopping_common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationOptions implements Serializable {
    private Long specId;

    private String[] optionName;
}
