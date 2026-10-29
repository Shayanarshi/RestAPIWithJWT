package in.ashokit.app.controller;

import in.ashokit.app.entity.Employee;
import in.ashokit.app.exception.ResourceNotFoundException;
import in.ashokit.app.model.Credentials;
import in.ashokit.app.model.JwtResponse;
import in.ashokit.app.service.EmployeeService;
import in.ashokit.app.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api")
//@CrossOrigin
public class EmployeeRestController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    private EmployeeService employeeService;
    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /*
    produces : Defines the media type/mime type that the method
               can produce and send it back to the client in
               the response body
     */
    @GetMapping( value = "/employees", produces = "application/json")
    public ResponseEntity<List<Employee>> returnEmployees() {
        List<Employee> employees = employeeService.findAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping( value = "/employee/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Employee> returnEmployee(@PathVariable("id") Integer id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            throw new ResourceNotFoundException("Employee not found with id : " + id);
        }
        // return  new ResponseEntity<>(employee, HttpStatus.OK);
        return ResponseEntity.ok(employee);
    }

    /*
    consumes : defines the media type/mime type that this method
               can consume from the client in the request body
     */
    @PostMapping(value = "/save", consumes = {"application/json", "application/xml"}, produces = "application/json")
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.save(employee);
            return  new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        }
        catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee) {
        Employee employeeFromDB = employeeService.findById(employee.getEmpno());
        if (employeeFromDB != null) {
            Employee updatedEmployee = employeeService.save(employee);
            return ResponseEntity.ok(updatedEmployee);
        }
        else {
//            return ResponseEntity.badRequest().build();
            throw new ResourceNotFoundException("Employee not found with id : " + employee.getEmpno());
        }
    }

    @PatchMapping(value = "/update/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Employee>  updateEmployeeById(@PathVariable("id") Integer id, @RequestBody Map<String, Object> map) {
        Employee employeeFromDB = employeeService.findById(id);
        if (employeeFromDB != null) {
            Employee updatedEmployee = employeeService.updateSal(id, (Double)map.get("sal"));
            return ResponseEntity.ok(updatedEmployee);
        }
        else {
//            return ResponseEntity.badRequest().build();
            throw new ResourceNotFoundException("Employee not found with id : " + id);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Integer id) {
        Employee employee = employeeService.findById(id);
        if (employee != null) {
            employeeService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else {
//            return ResponseEntity.badRequest().build();
            throw new ResourceNotFoundException("Employee not found with id : " + id);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> clientLogin(@RequestBody Credentials credentials) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        authenticationManager.authenticate(authentication);
        String token = jwtUtil.generateToken(credentials.getUsername());
        JwtResponse  jwtResponse = new JwtResponse();
        jwtResponse.setUsername(credentials.getUsername());
        jwtResponse.setToken(token);
        return ResponseEntity.ok(jwtResponse);
    }

}