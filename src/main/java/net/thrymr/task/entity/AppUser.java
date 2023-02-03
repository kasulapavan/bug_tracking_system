package net.thrymr.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thrymr.task.enums.RoleType;
import org.hibernate.annotations.ColumnDefault;


import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(columnDefinition = "varchar(20)")

    private String name;
    @Column(name = "email", unique = true)

    private String email;



    private String password;
    @Column(columnDefinition = "varchar(10)")

    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType roleType;


    @OneToMany(targetEntity = MyModule.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private List<MyModule> moduleList;

}
