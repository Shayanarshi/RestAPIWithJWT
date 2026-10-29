package in.ashokit.app.repository;

import in.ashokit.app.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee,Integer> {

    @Query(value = "UPDATE EMP SET SAL = :sal WHERE EMPNO = :empno",nativeQuery = true)
    @Modifying
    @Transactional
    int updateSal(@Param("empno")Integer empno,@Param("sal") Double newSal);
}
