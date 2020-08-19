package com.demo.one_to_many.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.one_to_many.exception.NotFoundException;
import com.demo.one_to_many.model.Laptop;
import com.demo.one_to_many.model.Student;
import com.demo.one_to_many.repository.LaptopRepository;
import com.demo.one_to_many.repository.StudentRepository;


@RestController
public class LaptopController {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    LaptopRepository laptopRepository;

    /*add only laptop by student id and
     * ekhane kono student er data add korbe na*/
    @PostMapping(value = "students/{sudentid}/laptops")
    public void saveLaptop(@PathVariable Long sudentid, @Valid @RequestBody Laptop laptop) {
        Student stu = studentRepository.getOne(sudentid);
        laptop.setStudent(stu);
        laptopRepository.save(laptop);
    }


    /* get all laptop form database laptop table or
     kono student student table er data ekhane asbe na or
      kotogulo laptop ache tar list dekhabe*/
    @GetMapping(value = "laptops")
    public List<Laptop> getAllLaptop() {
        return laptopRepository.findAll();
    }


    /*get a Laptop by Studentâ€™s ID or  oi student id er under e koiti laptop ache ta dekhabe */
    @GetMapping("/students/{studentId}/laptops")
    public List<Laptop> getLaptopByStudentId(@PathVariable Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new NotFoundException("Student not found!");
        }

        List<Laptop> laptops = laptopRepository.findByStudentId(studentId);
        if (laptops.size() > 0) {
            return laptops;
        } else {
            throw new NotFoundException("Laptop not found!");
        }
    }
    /*
     * @GetMapping(value="student/{id}/laptops") public Laptop
     * getLaptop(@PathVariable Long id) { return laptopRepository.getOne(id); }
     */







    /*Without student table row, Delete only Laptop row*/
    @DeleteMapping("students/{studentId}/laptops/{laptopId}")
    public String deleteLaptop(@PathVariable Long studentId, @PathVariable Long laptopId) {

        if (!studentRepository.existsById(studentId)) {
            throw new NotFoundException("Student not found!");
        }

        return laptopRepository.findById(laptopId)
                .map(laptop -> {
                    laptopRepository.delete(laptop);
                    return "Deleted Successfully!";
                }).orElseThrow(() -> new NotFoundException("Laptop not found!"));
    }
    /*
     * // Without student table row, Delete only Laptop row
     *
     * @DeleteMapping(value="laptops/{id}")
     * public void delete(@PathVariable Long id) {
     * laptopRepository.deleteById(id);
     *  }
     */



    /*
     * // Delete both sutdent and laptop relationship table row
     * @DeleteMapping(value="students/{id}/laptops")
     * public void delete(@PathVariable Long id) {
     * studentRepository.deleteById(id);
     *  }
     */



    /*   student id er under e laptop gulor akta laptop id dhore update korbe
        akane kono student table er data update kora jabe na*/
    @PutMapping("students/{studentId}/laptops/{laptopId}")
    public Laptop updateLaptop(@PathVariable Long studentId, @PathVariable Long laptopId,
                               @Valid @RequestBody Laptop laptopUpdated) {

        if (!studentRepository.existsById(studentId)) {
            throw new NotFoundException("Student not found!");
        }

        return laptopRepository.findById(laptopId)
                .map(laptop -> {
                    laptop.setLaptopName(laptopUpdated.getLaptopName());
                    return laptopRepository.save(laptop);
                }).orElseThrow(() -> new NotFoundException("Laptop not found!"));
    }
    /*
     * @PutMapping(value="laptops/update") public void updateStudent(@RequestBody
     * Laptop laptop) { Laptop lap=laptopRepository.getOne(laptop.getLid());
     * lap.setlName(laptop.getlName()); laptopRepository.save(lap); }
     */
}
