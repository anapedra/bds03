package com.devsuperior.bds03.controllers;

import com.devsuperior.bds03.dto.UserDTO;
import com.devsuperior.bds03.dto.UserInsertDTO;
import com.devsuperior.bds03.dto.UserUpdateDTO;
import com.devsuperior.bds03.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable){
        Page<UserDTO>list=userService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        UserDTO userDTO=userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
    @PostMapping
    public ResponseEntity<UserDTO > insert(@RequestBody @Valid UserInsertDTO dto){
        UserDTO  userDTO=userService.insert(dto);
        URI uri= ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(userDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(userDTO);

    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO>upDate(@PathVariable Long id,@RequestBody @Valid UserUpdateDTO dto){
        UserDTO newDto=userService.upDate(id,dto);
        return ResponseEntity.ok().body(newDto);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> dalet(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
