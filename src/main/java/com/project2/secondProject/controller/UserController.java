package com.project2.secondProject.controller;

import com.project2.secondProject.dto.EmployeeDTO;
import com.project2.secondProject.dto.ResponseDTO;
import com.project2.secondProject.dto.UserDTO;
import com.project2.secondProject.entity.User;
import com.project2.secondProject.repo.UserRepo;
import com.project2.secondProject.service.JWTService;
import com.project2.secondProject.service.UserService;
import com.project2.secondProject.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private UserRepo  userRepo;


    @PostMapping("/register")
    public User Register(@RequestBody User user) {

        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public String Login(@RequestBody User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }else  {
            return "login  fail";
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or missing token"));
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUserName(token);

        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid token"));
        }

        // Fetch user from DB
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        // ✅ Return JSON with username & role
        Map<String, String> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("role", user.getRole());

        return ResponseEntity.ok(userData);
    }


    @PostMapping("/logout")
    public String logout() {
        // For JWT, logout just means deleting token client-side
        return "Logged out successfully";
    }

    @GetMapping("/getAllUser")
    public ResponseEntity getAllUsers(){
        try {
            List<UserDTO> userDTOList = userService.getAllUsers();
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage(" success");
            responseDTO.setContent(userDTOList);
            return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
        }catch (Exception ex){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity deleteUser(@PathVariable int id){
        try {
            String res = userService.deleteUser(id);
            if(res.equals("00")){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("success");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            }  else  {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No user available for this id");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){

            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUserRole/{id}")
    public ResponseEntity updateUserRole(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            String newRole = request.get("role");
            String res = userService.updateUserRole(id, newRole);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Role updated successfully");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
                responseDTO.setContent(null);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
