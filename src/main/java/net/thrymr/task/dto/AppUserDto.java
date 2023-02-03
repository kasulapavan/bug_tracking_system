package net.thrymr.task.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import net.thrymr.task.entity.Ticket;
import net.thrymr.task.enums.RoleType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {

   private Long userId;

    private String name;

    private String email;

    private String password;

    private String mobileNumber;


    private  RoleType roleType;

    private String Token;

    private List<MyModuleDto> moduleList;








}
