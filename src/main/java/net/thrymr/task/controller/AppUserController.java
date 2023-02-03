package net.thrymr.task.controller;

import com.nimbusds.jose.JOSEException;
import net.thrymr.task.custom.exception.ApiResponse;
import net.thrymr.task.dto.AppUserDto;
import net.thrymr.task.dto.MyModuleDto;
import net.thrymr.task.dto.TicketDto;
import net.thrymr.task.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api")
public class AppUserController {

    @Autowired
    AppService appUserService;


    @PostMapping("/signup")
    public ApiResponse signUp(@RequestBody AppUserDto loginDto){
        return appUserService.signUp(loginDto);
    }
    @PostMapping("/sign-in")
    public ApiResponse signIn(@RequestBody AppUserDto loginDto) throws JOSEException {
        return appUserService.signIn(loginDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/save")
    public ApiResponse save(@RequestBody AppUserDto appUserDto){
        return appUserService.save(appUserDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/save-module")
    public ApiResponse saveModule(@RequestBody List<MyModuleDto> myModuleDto){
        return appUserService.saveModule(myModuleDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/associate/{id}")
    public ApiResponse associate(@PathVariable Long id,@RequestBody AppUserDto appUserList){
        return appUserService.associate(id, appUserList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/associate-ticket/{id}")
    public ApiResponse associateTicket(@PathVariable Long id,@RequestBody List<TicketDto> ticketDto){
        return appUserService.saveTicket(id, ticketDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/update-ticket")
    public ApiResponse updateTicket(@RequestBody TicketDto ticketDtos){
        return appUserService.updateTicket(ticketDtos);
    }







}
