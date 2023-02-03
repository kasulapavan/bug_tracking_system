package net.thrymr.task.service;

import com.nimbusds.jose.JOSEException;
import net.thrymr.task.custom.exception.ApiResponse;
import net.thrymr.task.dto.AppUserDto;
import net.thrymr.task.dto.MyModuleDto;
import net.thrymr.task.dto.TicketDto;

import java.util.List;


public interface AppService {


    ApiResponse signUp(AppUserDto appUserDto);
    ApiResponse signIn(AppUserDto loginDto) throws JOSEException;
     ApiResponse save(AppUserDto appUserDto);
     ApiResponse saveModule(List<MyModuleDto> myModuleDto);

     ApiResponse associate(Long id, AppUserDto appUserList);

     ApiResponse saveTicket(Long id, List<TicketDto> ticketList);


     ApiResponse updateTicket(TicketDto ticketList);
}