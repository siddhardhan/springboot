package com.springboot.elasticsearch.document;

import com.springboot.elasticsearch.model.EmployeeDepartment;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Data
public class EmployeeDocument {

    private String id;
    private String name;
    private String designation;
    private List<EmployeeDepartment> departmentList;

}