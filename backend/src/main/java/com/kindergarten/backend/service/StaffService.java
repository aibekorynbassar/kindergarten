package com.kindergarten.backend.service;

import com.kindergarten.backend.dto.StaffDto;
import com.kindergarten.backend.entity.Staff;
import com.kindergarten.backend.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public List<StaffDto> getAllStaff() {
        return staffRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public StaffDto createStaff(StaffDto staffDto) {
        Staff staff = convertToEntity(staffDto);
        Staff save = staffRepository.save(staff);
        return convertToDto(save);
    }

    public StaffDto updateStaff(Long id, StaffDto staffDto) {
        Staff staff = staffRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Staff not found"));
        staff.setName(staffDto.getName());
        staff.setImageUrl(staffDto.getImageUrl());
        staff.setProfession(staffDto.getProfession());
        Staff updated = staffRepository.save(staff);
        return convertToDto(updated);
    }

    public void deleteStaff(Long id){
        if (!staffRepository.existsById(id)){
            throw new RuntimeException("Staff does not exist");
        }
        staffRepository.deleteById(id);
    }


    private StaffDto convertToDto(Staff staff) {
        StaffDto staffDto = new StaffDto();
        staffDto.setId(staff.getId());
        staffDto.setName(staff.getName());
        staffDto.setImageUrl(staff.getImageUrl());
        staffDto.setProfession(staff.getProfession());
        return staffDto;
    }

    private Staff convertToEntity(StaffDto staffDto) {
        Staff staff = new Staff();
        staff.setId(staff.getId());
        staff.setName(staff.getName());
        staff.setImageUrl(staff.getImageUrl());
        staff.setProfession(staff.getProfession());
        return staff;
    }
}
