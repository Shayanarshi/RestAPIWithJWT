package in.ashokit.app.service;

import in.ashokit.app.entity.Employee;
import in.ashokit.app.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {



    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }


    public Employee save(Employee employee){
        return employeeRepository.save(employee);
    }
    public Employee findById(Integer id ){
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> findAll(){
      return (List<Employee>) employeeRepository.findAll();

    }

    public void deleteById(Integer id) {
        employeeRepository.deleteById(id);
    }

    public Employee updateSal(Integer empno, Double newSal) {
        employeeRepository.updateSal(empno, newSal);
        return employeeRepository.findById(empno).orElse(null);
    }

}
