package net.thrymr.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thrymr.task.enums.Status;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;
    @Column(columnDefinition = "varchar(10)")
    private String name;
    private  String priority;
    private LocalDate createdOn;

    @Column(name = "module_status")
    @Enumerated(EnumType.STRING)

    private Status moduleStatus;

    @OneToMany(targetEntity = Ticket.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "module_id", referencedColumnName = "moduleId")
    private List<Ticket> ticketList;


}
