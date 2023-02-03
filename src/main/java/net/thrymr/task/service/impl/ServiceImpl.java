package net.thrymr.task.service.impl;

import com.nimbusds.jose.JOSEException;
import net.thrymr.task.configuration.JwtTokenUtils;
import net.thrymr.task.custom.exception.ApiResponse;
import net.thrymr.task.dto.AppUserDto;

import net.thrymr.task.dto.MyModuleDto;
import net.thrymr.task.dto.TicketDto;
import net.thrymr.task.entity.AppUser;
import net.thrymr.task.entity.MyModule;
import net.thrymr.task.entity.Ticket;
import net.thrymr.task.enums.Status;
import net.thrymr.task.repository.AppUserRepository;
import net.thrymr.task.repository.ModuleRepository;
import net.thrymr.task.repository.TicketRepository;
import net.thrymr.task.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

import java.util.Optional;


@Service
public class ServiceImpl implements AppService {
    @Autowired
    private BCryptPasswordEncoder passwordConversion;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private TicketRepository ticketRepository;
    @Override
    public ApiResponse signUp(AppUserDto appUserDto) {
        AppUser appUser = appUserDtoTOEntity(appUserDto);
        appUserRepository.save(appUser);
        return new ApiResponse( HttpStatus.OK.value(), "Registration is done");
    }

    public ApiResponse save(AppUserDto appUserDto){
        AppUser appUser = appUserDtoTOEntity(appUserDto);
        appUserRepository.save(appUser);
        return new ApiResponse( HttpStatus.OK.value(), appUserEntityToDto(appUser));

    }
    public ApiResponse saveModule(List<MyModuleDto> myModuleDto) {  //    Service for Admin to add the modules, users
        List<MyModule> myModule = myModuleDto.stream().map(this::myModuleDtoToEntity).toList();
        myModule.stream().map(myModule1 -> moduleRepository.save(myModule1)).toList();
        return new ApiResponse( HttpStatus.OK.value(), myModule.stream().map(this::myModuleEntityToDto).toList());
    }
    public ApiResponse associate(Long id, AppUserDto appUserList) {
        Optional<MyModule> myModule = moduleRepository.findById(id);
        if(myModule.isPresent()) {
            AppUser appUser = appUserRepository.findByEmail(appUserList.getEmail());
            MyModule myModule1 = myModule.get();
            appUser.getModuleList().add(myModule1);
            appUserRepository.save(appUser);
            return new ApiResponse(HttpStatus.OK.value(), appUserEntityToDto(appUser));
        }
        return new ApiResponse(HttpStatus.NOT_FOUND.value(), " Given id is not present : "+ id);
    }

    public ApiResponse saveTicket(Long id, List<TicketDto> ticketList){
        Optional<MyModule> myModule = moduleRepository.findById(id);
        if(myModule.isPresent()){
            List<Ticket> tickets = ticketList.stream().map(this::ticketDtoToEntity).toList();
            MyModule myModule1 = myModule.get();
            myModule1.getTicketList().addAll(tickets);
            updateStatus(myModule1);
            moduleRepository.save(myModule1);

           return new ApiResponse(HttpStatus.OK.value(), myModule.stream().map(this::myModuleEntityToDto).toList());
        }
        return new ApiResponse(HttpStatus.NOT_FOUND.value(), " Given id is not present : "+ id);
    }



    public ApiResponse updateTicket(TicketDto ticketList){
        Ticket tickets = ticketDtoToEntity(ticketList);
        ticketRepository.save(tickets);
        List<MyModule> moduleList = moduleRepository.findAll();
        for(MyModule myModule : moduleList){
           for(Ticket ticket : myModule.getTicketList()){
               if(ticket.getTicketId().equals(tickets.getTicketId())){
                   moduleRepository.save(updateStatus(myModule));
               }
           }
        }

        return new ApiResponse(HttpStatus.OK.value(), ticketEntityToDto(tickets));
    }

    public MyModule updateStatus(MyModule myModule) {
        MyModule myModule1 = myModule;
     long count =   myModule1.getTicketList().stream().map(Ticket::getTicketId).count();
     if(count ==0){
         myModule1.setModuleStatus(Status.WAITING);
     }else if(count >=1){
         myModule1.setModuleStatus(Status.IN_PROGRESS);
     }
     List<Ticket> tickets = myModule1.getTicketList().stream().filter(ticket -> ticket.getTicketStatus() == Status.COMPLETED).toList();
     if(tickets.size() == count){
         myModule1.setModuleStatus(Status.COMPLETED);
     }
        List<Ticket> tickets1 = myModule1.getTicketList().stream().filter(ticket -> ticket.getTicketStatus() == Status.HOLD).toList();

      if(tickets1.size() >= 5){
             myModule.setModuleStatus(Status.HOLD);
     }
        return myModule1;

    }
    public ApiResponse signIn(AppUserDto loginDto) throws JOSEException {    //sign in users
        AppUser apUser = appUserRepository.findByEmail(loginDto.getEmail());
        if (apUser == null) {
            return new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Email id is wrong");
        }
        if (passwordConversion.matches(loginDto.getPassword(), apUser.getPassword())) {
            AppUserDto appUserDto = appUserEntityToDto(apUser);
            appUserDto.setToken(jwtTokenUtils.getToken(apUser));
            return new ApiResponse(HttpStatus.OK.value(), appUserDto);
        } else {
            return new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Password is wrong");
        }
    }
public AppUser appUserDtoTOEntity(AppUserDto appUserDto){
        AppUser appUser = new AppUser();
        if(appUserDto.getUserId() != null){
            appUser.setUserId(appUserDto.getUserId());
        }
        appUser.setName(appUserDto.getName());
        appUser.setEmail(appUserDto.getEmail());
appUser.setPassword(passwordConversion.encode(appUserDto.getPassword()));
        appUser.setMobileNumber(appUserDto.getMobileNumber());
        appUser.setRoleType(appUserDto.getRoleType());
        if(appUserDto.getModuleList() != null) {
            appUser.setModuleList(appUserDto.getModuleList().stream().map(this::myModuleDtoToEntity).toList());
        }
     return appUser;
}

public AppUserDto appUserEntityToDto(AppUser appUser){
        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setUserId(appUser.getUserId());
        appUserDto.setName(appUser.getName());
        appUserDto.setEmail(appUser.getEmail());
        appUserDto.setMobileNumber(appUser.getMobileNumber());
        appUserDto.setRoleType(appUser.getRoleType());
       if (appUser.getModuleList() != null) {
           appUserDto.setModuleList(appUser.getModuleList().stream().map(this::myModuleEntityToDto).toList());
       }
        return appUserDto;
}
public Ticket ticketDtoToEntity(TicketDto ticketDtos){

            Ticket ticket = new Ticket();
            if(ticketDtos.getTicketId() != null){
                ticket.setTicketId(ticketDtos.getTicketId());
            }
            ticket.setTitle(ticketDtos.getTitle());
            ticket.setDescription(ticketDtos.getDescription());
            if(ticketDtos.getTicketStatus() != null ) {
                ticket.setTicketStatus(ticketDtos.getTicketStatus());


            }
        return ticket;
}
public TicketDto ticketEntityToDto(Ticket ticket){

            TicketDto ticket1 = new TicketDto();
            ticket1.setTicketId(ticket.getTicketId());
            ticket1.setTitle(ticket.getTitle());
            ticket1.setDescription(ticket.getDescription());
            ticket1.setTicketStatus(ticket.getTicketStatus());


        return ticket1;
}
public MyModule myModuleDtoToEntity(MyModuleDto myModuleDtos){

    MyModule myModule = new MyModule();
    if(myModuleDtos.getModuleId() != null) {
        myModule.setModuleId(myModuleDtos.getModuleId());
    }
        myModule.setName(myModuleDtos.getName());
        myModule.setPriority(myModuleDtos.getPriority());
        myModule.setCreatedOn(myModuleDtos.getCreatedOn());
            myModule.setModuleStatus(Status.WAITING);//By default, module status will be in waiting
    if(myModuleDtos.getTicketList() != null) {
      myModule.setTicketList(myModuleDtos.getTicketList().stream().map(this::ticketDtoToEntity).toList());

    }
return myModule;
}
    public MyModuleDto myModuleEntityToDto(MyModule myModule){

            MyModuleDto myModuleDto = new MyModuleDto();
            myModuleDto.setModuleId(myModule.getModuleId());
            myModuleDto.setName(myModule.getName());
            myModuleDto.setPriority(myModule.getPriority());
            myModuleDto.setCreatedOn(myModule.getCreatedOn());
            myModuleDto.setModuleStatus(myModule.getModuleStatus());
            if(myModule.getTicketList() != null){
                myModuleDto.setTicketList(myModule.getTicketList().stream().map(this::ticketEntityToDto).toList());
            }

        return myModuleDto;
    }

}