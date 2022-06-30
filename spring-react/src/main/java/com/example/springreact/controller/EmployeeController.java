package com.example.springreact.controller;
import com.example.springreact.ems.entities.Employees;
import com.example.springreact.ems.utils.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/")
    public String allEmployees(Model model){
        List<Employees> employees = employeeService.listAllEmployees();
        model.addAttribute("employees", employees);
        return "employees";
    }

    @GetMapping("/addNewEmployee")
    public String addNewEmployee(Model model){
        Employees employee=new Employees();
        model.addAttribute("employee", employee);
        return "employee_form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employees employees){
        employeeService.saveEmployee(employees);
        return "redirect:/";
    }

    @GetMapping("/showUpdateForm/{id}")
    public String updateEmployee(@PathVariable("id") Long id,
                                 Model model){
        Employees employee=employeeService.getById(id);
        model.addAttribute("employee", employee);
        return "update";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id){
        employeeService.deleteById(id);
        return "redirect:/";
    }

//    @GetMapping("/login")
//    public String loginPage(){
//        return "login";
//    }
}
