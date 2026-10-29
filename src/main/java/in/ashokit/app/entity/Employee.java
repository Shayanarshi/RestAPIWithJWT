package in.ashokit.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "emp")
public class Employee implements Serializable {

    @Id
    @NotNull(message = "{Employee.empno.notNull}")
    @Positive(message = "{Employee.empno.positive}")
    private Integer empno;

    @NotBlank(message = "{Employee.ename.notBlank}")
    private String ename;

    @NotNull(message = "{Employee.sal.notNull}")
    @Positive(message = "{Employee.sal.positive}")
    private Double sal;

    @NotBlank(message = "{Employee.department.notBlank}")
    private String department;

}
