package com.hellozjf.study.studyjackson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Jingfeng Zhou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private Student student;
    private String name;
    private Double money;
    private Object sex;
    private Boolean verified;
    private List<Integer> marks;
}
