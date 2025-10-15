package com.kindergarten.backend.controller;

import com.kindergarten.backend.dto.StaffDto;
import com.kindergarten.backend.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class StaffController {

    @Autowired
    private StaffService staffService;


    @PostMapping("admin/staff")
    public ResponseEntity<StaffDto> createStaff(@RequestBody StaffDto staffDto){
        try{
            StaffDto staff = staffService.createStaff(staffDto);
            return ResponseEntity.ok(staff);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("admin/staff/{id}")
    public ResponseEntity<StaffDto> updateStaff(@PathVariable Long id, @RequestBody StaffDto staffDto){
        try{
            StaffDto updated = staffService.updateStaff(id, staffDto);
            return ResponseEntity.ok(updated);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }



}
