package com.project2.secondProject.service;

import com.project2.secondProject.dto.EmployeeDTO;
import com.project2.secondProject.dto.UserDTO;
import com.project2.secondProject.entity.Employee;
import com.project2.secondProject.entity.User;
import com.project2.secondProject.repo.UserRepo;
import com.project2.secondProject.util.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User saveUser(User user) {
        // Hibernate will auto-generate the ID
        user.setPassword(encoder.encode(user.getPassword()));

        return userRepo.save(user);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepo.findAll();
        return modelMapper.map(userList,new TypeToken<ArrayList<UserDTO>>() {}.getType());
    }

    public String deleteUser(int id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return VarList.RSP_SUCCESS;
        }else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    public String updateUserRole(int id, String role) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(role);
            userRepo.save(user);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

}
