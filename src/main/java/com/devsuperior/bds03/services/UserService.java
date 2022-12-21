package com.devsuperior.bds03.services;

import com.devsuperior.bds03.dto.RoleDTO;
import com.devsuperior.bds03.dto.UserDTO;
import com.devsuperior.bds03.dto.UserInsertDTO;
import com.devsuperior.bds03.dto.UserUpdateDTO;
import com.devsuperior.bds03.entities.Role;
import com.devsuperior.bds03.entities.User;
import com.devsuperior.bds03.repositories.RoleResository;
import com.devsuperior.bds03.repositories.UserRepository;
import com.devsuperior.bds03.services.exceptions.DataBaseException;
import com.devsuperior.bds03.services.exceptions.ResorceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static Logger logger=  LoggerFactory.getLogger(UserService.class);
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleResository roleRepository;

    public UserService(UserRepository userRepository, RoleResository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){
        Page<User> list=userRepository.findAll(pageable);
        return list.map(x -> new UserDTO(x));
    }
    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        Optional<User> obj=userRepository.findById(id);
        User entity=obj.orElseThrow(
                ()-> new ResorceNotFoundException("Id "+id+" not found"));
        return new UserDTO(entity);
    }

    @Transactional

    public UserDTO insert(UserInsertDTO dto) {

        var user=new User();
        copyDtoToEntity(dto,user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user=userRepository.save(user);
        return new UserDTO(user);


    }
    @Transactional
    public UserDTO upDate(Long id, UserUpdateDTO userDTO){
        try {
            var user= userRepository.getOne(id);
            copyDtoToEntity(userDTO,user);
            user=userRepository.save(user);
            return new UserDTO(user);
        }
        catch (EntityNotFoundException e){
            throw new ResorceNotFoundException("Id " + id + " not found :(");
        }

    }
    @Transactional
    public void deleteById(Long id){

        try {
            userRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResorceNotFoundException("Id "+id+" not found!");
        }
        catch (DataIntegrityViolationException e){
            throw new DataBaseException("Integrity violation");
        }

    }

    private void copyDtoToEntity(UserDTO userDTO,User user){

        user.setEmail(userDTO.getEmail());

        user.getRoles().clear();
        for (RoleDTO roleDTO : userDTO.getRoles()){
            Role role=roleRepository.getOne(roleDTO.getId());
            user.getRoles().add(role);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null){
            logger.error("User not found: "+username);
            throw new UsernameNotFoundException("Email not found!");
        }
        logger.info("User found!"+username);
        return user;

    }



}
