package com.xuanviet.employeeservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDeletedEvent {

    private String id;
    private String firstName;
    private String lastName;
    private String kin;
    private  Boolean isDisciplined;
}
